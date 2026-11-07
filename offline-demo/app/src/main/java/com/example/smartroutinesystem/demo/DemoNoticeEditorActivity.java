package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.widget.EditText;
import com.example.smartroutinesystem.R;

public class DemoNoticeEditorActivity extends BaseDemoActivity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!editor()) return; show(R.layout.activity_notice_input,true);
        EditText title=findViewById(R.id.editTextNoticeTitle), body=findViewById(R.id.editTextNoticeBody);
        findViewById(R.id.buttonSubmit).setOnClickListener(v->{
            try { data.addNotice(title.getText().toString(),body.getText().toString()); title.setText(""); body.setText(""); toast("Saved locally. No push notification sent."); }
            catch (IllegalArgumentException|SecurityException e) { toast(e.getMessage()); }
        });
    }
}
