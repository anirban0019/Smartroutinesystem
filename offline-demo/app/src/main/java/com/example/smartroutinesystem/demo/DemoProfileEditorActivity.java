package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.widget.EditText;
import com.example.smartroutinesystem.R;

public class DemoProfileEditorActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!ready()) return; show(R.layout.activity_update_profile,true);
        DemoStore.Person p=data.person();
        EditText name=findViewById(R.id.editTextFullName), phone=findViewById(R.id.editTextPhoneNumber);
        name.setText(p.name); phone.setText(p.phone);
        fixed(R.id.editTextDepartment,p.department); fixed(R.id.editTextRollNumber,p.roll); fixed(R.id.editTextSeries,p.series);
        findViewById(R.id.buttonUpdate).setOnClickListener(v->{
            try { data.updateProfile(name.getText().toString(),phone.getText().toString()); toast("Sample profile updated locally"); finish(); }
            catch (IllegalArgumentException e) { toast(e.getMessage()); }
        });
    }
    private void fixed(int id,String value) { EditText field=findViewById(id); field.setText(value); field.setEnabled(false); }
}
