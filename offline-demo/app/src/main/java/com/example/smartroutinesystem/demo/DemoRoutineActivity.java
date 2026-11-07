package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.smartroutinesystem.GridAdapter;
import com.example.smartroutinesystem.R;
import com.example.smartroutinesystem.ViewData;
import java.util.List;

public class DemoRoutineActivity extends BaseDemoActivity {
    private Spinner day,time,section;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_show_routine,false);
        ((TextView)findViewById(R.id.welcome)).setText("Sample timetable");
        ((TextView)findViewById(R.id.deptTV)).setText("Department: CSE");
        ((TextView)findViewById(R.id.seriesTV)).setText("Series: 19");
        day=findViewById(R.id.daySpinner); time=findViewById(R.id.timeSpinner); section=findViewById(R.id.sectionSpinner);
        bind(day,R.array.Days); bind(time,R.array.AllTimes); bind(section,R.array.sec);
        AdapterView.OnItemSelectedListener change=new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p,View v,int position,long id) { render(); }
            @Override public void onNothingSelected(AdapterView<?> p) { }
        };
        day.setOnItemSelectedListener(change); time.setOnItemSelectedListener(change); section.setOnItemSelectedListener(change);
        findViewById(R.id.button).setOnClickListener(v->render()); render();
    }
    private void bind(Spinner spinner,int array) {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spinner.setAdapter(adapter);
    }
    private void render() {
        if (day.getSelectedItem()==null || time.getSelectedItem()==null || section.getSelectedItem()==null) return;
        List<ViewData> rows=data.routine(section.getSelectedItem().toString(),day.getSelectedItem().toString(),time.getSelectedItem().toString());
        GridView grid=findViewById(R.id.gridView); grid.setNumColumns(getResources().getConfiguration().screenWidthDp>=600?3:2);
        grid.setAdapter(new GridAdapter(this,rows)); grid.setVisibility(rows.isEmpty()?View.GONE:View.VISIBLE);
        TextView empty=findViewById(R.id.NoClass); empty.setText("No sample class for this selection."); empty.setVisibility(rows.isEmpty()?View.VISIBLE:View.GONE);
    }
}
