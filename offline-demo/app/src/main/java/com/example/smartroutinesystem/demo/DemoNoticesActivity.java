package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartroutinesystem.NoticeAdapter;
import com.example.smartroutinesystem.R;

public class DemoNoticesActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_show_notice,false);
        RecyclerView list=findViewById(R.id.recyclerview); list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new NoticeAdapter(data.notices()));
    }
}
