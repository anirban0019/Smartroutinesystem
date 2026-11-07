package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.widget.TextView;
import com.example.smartroutinesystem.R;

public class DemoAdminActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!administrator()) return; show(R.layout.activity_admin_home,true);
        ((TextView)findViewById(R.id.welcomeText)).setText("Welcome Demo Administrator");
        findViewById(R.id.btn_make_cr).setOnClickListener(v->open(DemoMakeCrActivity.class));
        findViewById(R.id.btn_remove_cr).setOnClickListener(v->open(DemoRemoveCrActivity.class));
    }
}
