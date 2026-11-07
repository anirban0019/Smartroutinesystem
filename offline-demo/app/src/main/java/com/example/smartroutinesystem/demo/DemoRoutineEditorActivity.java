package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.smartroutinesystem.R;
import com.example.smartroutinesystem.ViewData;
import java.util.List;

public class DemoRoutineEditorActivity extends BaseDemoActivity {
    private Spinner day,time;
    private EditText teacher,course,room;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!editor()) return; show(R.layout.activity_routine_input,true);
        ((TextView)findViewById(R.id.deptTextView)).setText("Department: CSE");
        ((TextView)findViewById(R.id.batchTextView)).setText("Series: 19");
        ((TextView)findViewById(R.id.sectionTextView)).setText("Section: A · local sample changes only");
        day=findViewById(R.id.daySpinner); time=findViewById(R.id.timeSpinner);
        teacher=findViewById(R.id.teacherEditText); course=findViewById(R.id.courseEditText); room=findViewById(R.id.roomEditText);
        bind(day,R.array.Days); bind(time,R.array.time);
        AdapterView.OnItemSelectedListener change=new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> p,View v,int position,long id) { populate(); }
            @Override public void onNothingSelected(AdapterView<?> p) { }
        };
        day.setOnItemSelectedListener(change); time.setOnItemSelectedListener(change);
        findViewById(R.id.saveButton).setOnClickListener(v->{
            try { data.saveClass(day(),time(),teacher.getText().toString(),course.getText().toString(),room.getText().toString()); toast("Sample class saved locally"); }
            catch (IllegalArgumentException|SecurityException e) { toast(e.getMessage()); }
        });
        findViewById(R.id.deleteClassButton).setOnClickListener(v->confirm("Remove sample class?",()->{
            try { data.deleteClass(day(),time()); populate(); toast("Sample class removed"); } catch (SecurityException e) { toast(e.getMessage()); }
        }));
        findViewById(R.id.deleteRoutineButton).setOnClickListener(v->confirm("Remove sample section routine?",()->{
            try { data.deleteRoutine(); populate(); toast("Sample routine removed"); } catch (SecurityException e) { toast(e.getMessage()); }
        }));
        populate();
    }
    private String day() { return day.getSelectedItem().toString(); }
    private String time() { return time.getSelectedItem().toString(); }
    private void bind(Spinner spinner,int array) {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spinner.setAdapter(adapter);
    }
    private void populate() {
        if (day.getSelectedItem()==null || time.getSelectedItem()==null) return;
        List<ViewData> rows=data.routine("A",day(),time()); ViewData row=rows.isEmpty()?null:rows.get(0);
        teacher.setText(row==null?"":row.getName()); course.setText(row==null?"":row.getCourse()); room.setText(row==null?"":row.getRoom());
    }
}
