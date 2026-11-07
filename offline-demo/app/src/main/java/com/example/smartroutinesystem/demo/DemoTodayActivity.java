package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartroutinesystem.R;
import com.example.smartroutinesystem.RoutineAdapter;

public class DemoTodayActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_home,false);
        ((TextView)findViewById(R.id.tv1)).setText("Sample day: Saturday");
        ((TextView)findViewById(R.id.tv2)).setText("CSE · Series 19 · Section A");
        RecyclerView list=findViewById(R.id.recyclerview); list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(new RoutineAdapter(this,data.routine("A","Saturday","All Day")));
    }
}
