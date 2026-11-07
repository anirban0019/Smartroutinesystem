package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.example.smartroutinesystem.R;

public class DemoMakeCrActivity extends BaseDemoActivity {
    private DemoStore.Person selected;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!administrator()) return; show(R.layout.activity_make_cr,true);
        EditText roll=findViewById(R.id.editTextRollNo); roll.setHint("Sample roll: 1900001");
        findViewById(R.id.buttonMakeCR).setVisibility(View.GONE);
        findViewById(R.id.buttonSearch).setOnClickListener(v->{
            selected=data.findStudent(roll.getText().toString());
            if (selected==null) {
                for (int id:new int[]{R.id.tv1,R.id.tv2,R.id.tv3,R.id.tv4,R.id.tv5,R.id.textViewStudentData}) ((TextView)findViewById(id)).setText("");
                ((TextView)findViewById(R.id.tv1)).setText("Sample student not found"); findViewById(R.id.buttonMakeCR).setVisibility(View.GONE); return;
            }
            ((TextView)findViewById(R.id.tv1)).setText("Name: "+selected.name);
            ((TextView)findViewById(R.id.tv2)).setText("Department: "+selected.department);
            ((TextView)findViewById(R.id.tv3)).setText("Roll Number: "+selected.roll);
            ((TextView)findViewById(R.id.tv4)).setText("Series: "+selected.series);
            ((TextView)findViewById(R.id.tv5)).setText("Section: "+selected.section);
            ((TextView)findViewById(R.id.textViewStudentData)).setText("CR: "+(selected.cr?"Yes":"No"));
            findViewById(R.id.buttonMakeCR).setVisibility(View.VISIBLE);
        });
        findViewById(R.id.buttonMakeCR).setOnClickListener(v->{
            if (selected==null) return;
            final String id=selected.id;
            confirm("Assign sample CR?",()->{data.setCr(id,true); ((TextView)findViewById(R.id.textViewStudentData)).setText("CR: Yes"); toast("Sample CR assigned locally");});
        });
    }
}
