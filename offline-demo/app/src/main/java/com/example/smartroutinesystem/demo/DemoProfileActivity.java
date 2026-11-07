package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.widget.TextView;
import com.example.smartroutinesystem.R;

public class DemoProfileActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_profile,true);
        ((TextView)findViewById(R.id.btn_change)).setText("Edit sample profile");
        findViewById(R.id.btn_change).setOnClickListener(v->open(DemoProfileEditorActivity.class));
    }
    @Override protected void onResume() {
        super.onResume(); if (!data.selected() || findViewById(R.id.tv1)==null) return;
        DemoStore.Person p=data.person();
        ((TextView)findViewById(R.id.tv1)).setText("Name: "+p.name);
        ((TextView)findViewById(R.id.tv2)).setText("Department: "+p.department);
        ((TextView)findViewById(R.id.tv3)).setText("Roll Number: "+p.roll);
        ((TextView)findViewById(R.id.tv4)).setText("Phone: "+p.phone);
        ((TextView)findViewById(R.id.tv5)).setText("Series: "+p.series);
        ((TextView)findViewById(R.id.tv6)).setText("Section: "+p.section+" · "+p.role());
    }
}
