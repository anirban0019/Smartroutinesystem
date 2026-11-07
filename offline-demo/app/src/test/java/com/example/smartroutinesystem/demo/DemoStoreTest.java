package com.example.smartroutinesystem.demo;

import com.example.smartroutinesystem.ViewData;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DemoStoreTest {
    private DemoStore data;
    @Before public void setup() { data=new DemoStore(); }
    @Test public void initialDataCoversAllWorkingDaysAndSections() {
        for (String section:new String[]{"A","B","C"}) for (String day:DemoStore.DAYS) assertEquals(3,data.routine(section,day,"All Day").size());
        assertEquals(2,data.notices().size());
    }
    @Test public void studentCanReadButCannotEditOrAssignRoles() {
        data.select(DemoStore.STUDENT); assertFalse(data.canEdit()); assertFalse(data.isAdmin());
        assertThrows(SecurityException.class,()->data.saveClass("Saturday","8.00 AM","A","B","C"));
        assertThrows(SecurityException.class,()->data.addNotice("A","B"));
        assertThrows(SecurityException.class,()->data.setCr(DemoStore.CR,false));
        assertThrows(SecurityException.class,()->data.deleteClass("Saturday","8.00 AM"));
        assertThrows(SecurityException.class,()->data.deleteRoutine());
    }
    @Test public void crChangesRoutineAndNoticesLocally() {
        data.select(DemoStore.CR); data.saveClass("Saturday","8.00 AM"," Demo Teacher "," Updated Course "," Demo Room ");
        ViewData row=data.routine("A","Saturday","8.00 AM").get(0);
        assertEquals("Demo Teacher",row.getName()); assertEquals("Updated Course",row.getCourse());
        data.addNotice(" Demo notice "," Demo body "); assertEquals("Demo notice",data.notices().get(0).getTitle());
        assertThrows(SecurityException.class,()->data.setCr(DemoStore.STUDENT,true));
    }
    @Test public void timetableFilteringAndSlotOrderingRemainCorrect() {
        data.select(DemoStore.CR); data.saveClass("Saturday","2.30 PM","A","Afternoon","B");
        assertEquals("2.30 PM",data.routine("A","Saturday","All Day").get(3).getTime());
        assertEquals(1,data.routine("A","Saturday","2.30 PM").size());
        assertTrue(data.routine("A","Thursday","All Day").isEmpty());
        assertEquals(3,data.routine("B","Saturday","All Day").size());
    }
    @Test public void invalidChangesDoNotMutateData() {
        data.select(DemoStore.CR);
        assertThrows(IllegalArgumentException.class,()->data.saveClass("Friday","8.00 AM","A","B","C"));
        assertThrows(IllegalArgumentException.class,()->data.saveClass("Saturday","bad","A","B","C"));
        assertThrows(IllegalArgumentException.class,()->data.saveClass("Saturday","8.00 AM"," ","B","C"));
        assertThrows(IllegalArgumentException.class,()->data.addNotice(" ","B"));
        assertEquals(3,data.routine("A","Saturday","All Day").size()); assertEquals(2,data.notices().size());
    }
    @Test public void deletingOneClassAndSectionNeverDeletesOtherSections() {
        data.select(DemoStore.CR); data.deleteClass("Saturday","8.00 AM");
        assertEquals(2,data.routine("A","Saturday","All Day").size());
        data.deleteRoutine(); assertTrue(data.routine("A","Monday","All Day").isEmpty());
        assertEquals(3,data.routine("B","Monday","All Day").size());
    }
    @Test public void adminFindsAssignsAndRevokesCrStatus() {
        data.select(DemoStore.ADMIN); assertEquals(DemoStore.STUDENT,data.findStudent("1900001").id);
        assertNull(data.findStudent("unknown")); data.setCr(DemoStore.STUDENT,true);
        assertEquals(2,data.representatives("CSE","19","A").size());
        data.select(DemoStore.STUDENT); assertTrue(data.canEdit());
        data.select(DemoStore.ADMIN); data.setCr(DemoStore.STUDENT,false);
        data.select(DemoStore.STUDENT); assertFalse(data.canEdit());
    }
    @Test public void adminAndUnknownAccountsCannotBePromotedAsCr() {
        assertThrows(IllegalArgumentException.class,()->data.select("unknown"));
        data.select(DemoStore.ADMIN);
        assertThrows(IllegalArgumentException.class,()->data.setCr(DemoStore.ADMIN,true));
        assertThrows(IllegalArgumentException.class,()->data.setCr("unknown",true));
        assertFalse(data.canEdit());
    }
    @Test public void editingProfileCannotChangeClassOrRole() {
        data.select(DemoStore.STUDENT); data.updateProfile("New Sample Name","Demo phone");
        assertEquals("New Sample Name",data.person().name); assertEquals("1900001",data.person().roll);
        assertEquals("A",data.person().section); assertFalse(data.person().cr); assertFalse(data.person().admin);
    }
    @Test public void displayObjectsDoNotAllowMutationOfTheStore() {
        data.routine("A","Saturday","All Day").get(0).setCourse("Mutated display");
        data.notices().clear();
        assertEquals("Database Systems",data.routine("A","Saturday","All Day").get(0).getCourse());
        assertEquals("Sample lab reminder",data.notices().get(0).getTitle());
    }
    @Test public void resetRestoresDataAndClearsAccountSelection() {
        data.select(DemoStore.CR); data.deleteRoutine(); data.addNotice("X","Y"); data.reset();
        assertFalse(data.selected()); assertThrows(IllegalStateException.class,()->data.person());
        assertEquals(3,data.routine("A","Saturday","All Day").size()); assertEquals(2,data.notices().size());
    }
}
