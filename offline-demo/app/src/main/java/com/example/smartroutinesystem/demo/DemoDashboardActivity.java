package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.smartroutinesystem.R;

public class DemoDashboardActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_main,true);
        findViewById(R.id.btn_show_routine).setOnClickListener(v->open(DemoRoutineActivity.class));
        findViewById(R.id.btn_home).setOnClickListener(v->open(DemoTodayActivity.class));
        findViewById(R.id.btn_show_notice).setOnClickListener(v->open(DemoNoticesActivity.class));
        findViewById(R.id.btn_rtn).setOnClickListener(v->open(DemoRoutineEditorActivity.class));
        findViewById(R.id.btn_test).setOnClickListener(v->open(DemoNoticeEditorActivity.class));
    }
    @Override protected void onResume() {
        super.onResume(); if (!data.selected() || findViewById(R.id.user_details)==null) return;
        DemoStore.Person p=data.person(); ((TextView)findViewById(R.id.user_details)).setText("Welcome " + p.name);
        ((TextView)findViewById(R.id.userIdCheck)).setText("CSE · Series 19 · Section A\nSample role: " + p.role());
        findViewById(R.id.btn_rtn).setVisibility(data.canEdit()?View.VISIBLE:View.GONE);
        findViewById(R.id.btn_test).setVisibility(data.canEdit()?View.VISIBLE:View.GONE);
    }
}
