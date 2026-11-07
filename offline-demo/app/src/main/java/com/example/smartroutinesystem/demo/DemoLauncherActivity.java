package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DemoLauncherActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout body=new LinearLayout(this); body.setOrientation(LinearLayout.VERTICAL);
        int pad=(int)(24*getResources().getDisplayMetrics().density); body.setPadding(pad,pad,pad,pad);
        TextView title=new TextView(this); title.setText("Explore Smart Routine"); title.setTextSize(26); body.addView(title);
        TextView help=new TextView(this);
        help.setText("Offline demo using fictional sample data. Choose a sample account to explore the original app screens.\n\nNo sign-in, Firebase connection or push notification is used. Changes last only while this app process runs. Reset demo restores the examples.");
        help.setTextSize(16); help.setPadding(0,pad,0,pad); body.addView(help);
        add(body,"Student",DemoStore.STUDENT); add(body,"CR (Class Representative)",DemoStore.CR); add(body,"Admin",DemoStore.ADMIN);
        Button reset=new Button(this); reset.setText("Reset demo"); reset.setOnClickListener(v->confirm("Reset demo?",()->{data.reset();toast("Sample data restored");})); body.addView(reset);
        ScrollView scroll=new ScrollView(this); scroll.addView(body,new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)); setContentView(scroll);
    }
    private void add(LinearLayout parent,String label,String id) {
        Button button=new Button(this); button.setText(label); button.setMinHeight((int)(48*getResources().getDisplayMetrics().density));
        button.setOnClickListener(v->{data.select(id);open(data.isAdmin()?DemoAdminActivity.class:DemoDashboardActivity.class);}); parent.addView(button);
    }
}
