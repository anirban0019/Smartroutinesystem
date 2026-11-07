package com.example.smartroutinesystem.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseDemoActivity extends AppCompatActivity {
    protected final DemoStore data=DemoStore.INSTANCE;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Smart Routine Demo");
            getSupportActionBar().setSubtitle("OFFLINE DEMO · SAMPLE DATA");
            getSupportActionBar().setDisplayHomeAsUpEnabled(!(this instanceof DemoLauncherActivity));
        }
    }
    protected void show(int layout, boolean scroll) {
        setContentView(layout);
        if (scroll) {
            ViewGroup content=findViewById(android.R.id.content);
            View original=content.getChildAt(0); content.removeView(original);
            ScrollView container=new ScrollView(this); container.setFillViewport(true);
            container.addView(original,new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            content.addView(container);
        }
    }
    protected void open(Class<?> screen) { startActivity(new Intent(this,screen)); }
    protected void chooseAccount() {
        startActivity(new Intent(this,DemoLauncherActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
    protected boolean ready() { if (!data.selected()) { chooseAccount(); return false; } return true; }
    protected boolean editor() { if (!ready()) return false; if (!data.canEdit()) { toast("Choose a sample CR account to edit"); finish(); return false; } return true; }
    protected boolean administrator() { if (!ready()) return false; if (!data.isAdmin()) { toast("Choose the sample administrator"); finish(); return false; } return true; }
    protected void toast(String text) { Toast.makeText(this,text,Toast.LENGTH_SHORT).show(); }
    protected void confirm(String title,Runnable action) {
        new AlertDialog.Builder(this).setTitle(title).setMessage("This changes sample data only. Reset demo restores the examples.")
            .setPositiveButton("Confirm",(dialog,which)->action.run()).setNegativeButton("Cancel",null).show();
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        if (data.selected()) menu.add(0,1,0,"Profile");
        menu.add(0,2,1,"Switch sample account"); menu.add(0,3,2,"Reset demo"); return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) { finish(); return true; }
        if (item.getItemId()==1) { open(DemoProfileActivity.class); return true; }
        if (item.getItemId()==2) { chooseAccount(); return true; }
        if (item.getItemId()==3) { confirm("Reset demo?",()->{data.reset();chooseAccount();}); return true; }
        return super.onOptionsItemSelected(item);
    }
}
