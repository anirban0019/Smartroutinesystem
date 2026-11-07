package com.example.smartroutinesystem.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.ParcelFileDescriptor;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.example.smartroutinesystem.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Arrays;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DemoSmokeTest {
    @Before public void reset() { DemoStore.INSTANCE.reset(); }
    private ActivityScenario<DemoLauncherActivity> launch() {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        return ActivityScenario.launch(new Intent(context,DemoLauncherActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
    private void capture(String name) throws Exception {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        // AGP removes installed APKs and their app files after connected tests.
        // Capture directly into shell-owned storage, outside those app files.
        // executeShellCommand accepts one command, not shell operators.
        String target="/data/local/tmp/smart-routine-demo/"+name+".png";
        shell("mkdir -p /data/local/tmp/smart-routine-demo");
        shell("screencap -p "+target);
        assertTrue(shell("ls -l "+target).contains(name+".png"));
    }
    private String shell(String command) throws Exception {
        try (ParcelFileDescriptor output=InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(command);
             ParcelFileDescriptor.AutoCloseInputStream input=new ParcelFileDescriptor.AutoCloseInputStream(output)) {
            java.io.ByteArrayOutputStream result=new java.io.ByteArrayOutputStream();
            byte[] buffer=new byte[1024]; int count;
            while ((count=input.read(buffer))!=-1) result.write(buffer,0,count);
            return result.toString("UTF-8");
        }
    }
    @Test public void studentReadsOriginalRoutineNoticeAndProfileScreens() throws Exception {
        try (ActivityScenario<DemoLauncherActivity> ignored=launch()) {
            onView(withText("Student")).perform(click());
            onView(withId(R.id.btn_rtn)).check(matches(withEffectiveVisibility(Visibility.GONE)));
            onView(withText("OFFLINE DEMO · SAMPLE DATA")).check(matches(isDisplayed())); capture("01-student-dashboard");
            onView(withId(R.id.btn_show_routine)).perform(scrollTo(),click());
            onView(withText("Database Systems")).check(matches(isDisplayed())); capture("02-timetable"); pressBack();
            onView(withId(R.id.btn_home)).perform(scrollTo(),click());
            onView(withText("Sample day: Saturday")).check(matches(isDisplayed())); capture("03-sample-day"); pressBack();
            onView(withId(R.id.btn_show_notice)).perform(scrollTo(),click());
            onView(withText("Sample lab reminder")).check(matches(isDisplayed())); capture("04-notices"); pressBack();
            openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
            onView(withText("Profile")).perform(click()); onView(withText("Name: Demo Student")).check(matches(isDisplayed())); capture("05-profile");
            onView(withId(R.id.btn_change)).perform(scrollTo(),click());
            onView(withId(R.id.editTextFullName)).perform(replaceText("Updated Demo Student"));
            closeSoftKeyboard(); onView(withId(R.id.buttonUpdate)).perform(scrollTo(),click());
            onView(withText("Name: Updated Demo Student")).check(matches(isDisplayed()));
        }
    }
    @Test public void crEditsRoutineAndPublishesLocalNotice() throws Exception {
        try (ActivityScenario<DemoLauncherActivity> ignored=launch()) {
            onView(withText("CR (Class Representative)")).perform(click());
            onView(withId(R.id.btn_rtn)).perform(scrollTo(),click());
            onView(withId(R.id.courseEditText)).perform(scrollTo(),replaceText("Offline Demo Course")); closeSoftKeyboard();
            onView(withId(R.id.saveButton)).perform(scrollTo(),click());
            assertEquals("Offline Demo Course",DemoStore.INSTANCE.routine("A","Saturday","8.00 AM").get(0).getCourse()); capture("06-routine-editor"); pressBack();
            onView(withId(R.id.btn_test)).perform(scrollTo(),click());
            onView(withId(R.id.editTextNoticeTitle)).perform(replaceText("Sample workshop"));
            onView(withId(R.id.editTextNoticeBody)).perform(replaceText("Demo data: a practice workshop for the sample class.")); closeSoftKeyboard();
            capture("07-notice-editor"); onView(withId(R.id.buttonSubmit)).perform(scrollTo(),click()); pressBack();
            onView(withId(R.id.btn_show_notice)).perform(scrollTo(),click()); onView(withText("Sample workshop")).check(matches(isDisplayed()));
        }
    }
    @Test public void adminAssignsAndRemovesSampleCr() throws Exception {
        try (ActivityScenario<DemoLauncherActivity> ignored=launch()) {
            onView(withText("Admin")).perform(click()); capture("08-admin-dashboard");
            onView(withId(R.id.btn_make_cr)).perform(scrollTo(),click());
            onView(withId(R.id.editTextRollNo)).perform(replaceText("1900001")); closeSoftKeyboard();
            onView(withId(R.id.buttonSearch)).perform(scrollTo(),click());
            onView(withId(R.id.buttonMakeCR)).perform(scrollTo(),click()); onView(withText("Confirm")).perform(click());
            onView(withText("CR: Yes")).check(matches(isDisplayed())); capture("09-assign-cr"); pressBack();
            onView(withId(R.id.btn_remove_cr)).perform(scrollTo(),click());
            onView(allOf(withId(R.id.removeCrButton),withParent(hasDescendant(withText("1900001"))))).perform(click());
            onView(withText("Confirm")).perform(click()); capture("10-remove-cr");
            assertFalse(DemoStore.INSTANCE.findStudent("1900001").cr);
        }
    }
    @Test public void installedDemoHasNoNetworkPermissionOrFirebaseSdk() throws Exception {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.smartroutinesystem.demo",context.getPackageName());
        PackageInfo info=context.getPackageManager().getPackageInfo(context.getPackageName(),PackageManager.GET_PERMISSIONS);
        if (info.requestedPermissions!=null) {
            assertFalse(Arrays.asList(info.requestedPermissions).contains("android.permission.INTERNET"));
            assertFalse(Arrays.asList(info.requestedPermissions).contains("android.permission.ACCESS_NETWORK_STATE"));
        }
        assertThrows(ClassNotFoundException.class,()->Class.forName("com.google.firebase.FirebaseApp"));
    }
}
