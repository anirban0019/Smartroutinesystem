package com.example.smartroutinesystem.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.smartroutinesystem.R;
import java.util.List;

public class DemoRemoveCrActivity extends BaseDemoActivity {
    private Spinner department,series,section;
    private RecyclerView list;
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state); if (!administrator()) return; show(R.layout.activity_remove_cr,false);
        department=findViewById(R.id.deptSpinner); series=findViewById(R.id.seriesSpinner); section=findViewById(R.id.sectionSpinner);
        bind(department,R.array.Depts); bind(series,R.array.Series); bind(section,R.array.sec);
        list=findViewById(R.id.crRecyclerView); list.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.searchCrButton).setOnClickListener(v->render()); render();
    }
    private void bind(Spinner spinner,int array) {
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); spinner.setAdapter(adapter);
    }
    private void render() {
        List<DemoStore.Person> rows=data.representatives(department.getSelectedItem().toString(),series.getSelectedItem().toString(),section.getSelectedItem().toString());
        list.setAdapter(new CrList(rows)); if (rows.isEmpty()) toast("No sample CR matches this class");
    }
    private final class CrList extends RecyclerView.Adapter<Row> {
        private final List<DemoStore.Person> rows;
        CrList(List<DemoStore.Person> rows) { this.rows=rows; }
        @NonNull @Override public Row onCreateViewHolder(@NonNull ViewGroup parent,int type) { return new Row(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cr,parent,false)); }
        @Override public void onBindViewHolder(@NonNull Row row,int position) {
            DemoStore.Person p=rows.get(position);
            ((TextView)row.itemView.findViewById(R.id.nameTextView)).setText(p.name);
            ((TextView)row.itemView.findViewById(R.id.rollTextView)).setText(p.roll);
            ((TextView)row.itemView.findViewById(R.id.deptTextView)).setText("Department: "+p.department);
            ((TextView)row.itemView.findViewById(R.id.seriesTextView)).setText("Series: "+p.series);
            ((TextView)row.itemView.findViewById(R.id.sectionTextView)).setText("Section: "+p.section);
            row.itemView.findViewById(R.id.removeCrButton).setOnClickListener(v->confirm("Remove sample CR?",()->{data.setCr(p.id,false);render();}));
        }
        @Override public int getItemCount() { return rows.size(); }
    }
    private static final class Row extends RecyclerView.ViewHolder { Row(View view) { super(view); } }
}
