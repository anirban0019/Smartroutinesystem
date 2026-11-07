package com.example.smartroutinesystem.demo;

import com.example.smartroutinesystem.Notice;
import com.example.smartroutinesystem.ViewData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Fictional, process-local data. No accounts, credentials, database or network. */
public final class DemoStore {
    public static final DemoStore INSTANCE = new DemoStore();
    public static final String STUDENT = "demo-student", CR = "demo-cr", ADMIN = "demo-admin";
    public static final List<String> DAYS = Collections.unmodifiableList(Arrays.asList("Saturday", "Sunday", "Monday", "Tuesday", "Wednesday"));
    public static final List<String> TIMES = Collections.unmodifiableList(Arrays.asList("8.00 AM", "8.50 AM", "9.40 AM", "10.50 AM", "11.40 AM", "12.30 PM", "2.30 PM", "3.20 PM", "4.10 PM"));

    public static final class Person {
        public final String id, name, roll, email, phone;
        public final String department = "CSE", series = "19", section = "A";
        public final boolean cr, admin;
        Person(String id, String name, String roll, String email, String phone, boolean cr, boolean admin) {
            this.id=id; this.name=name; this.roll=roll; this.email=email; this.phone=phone; this.cr=cr; this.admin=admin;
        }
        public String role() { return admin ? "Admin" : cr ? "CR" : "Student"; }
    }
    private final Map<String, Person> people = new LinkedHashMap<>();
    private final Map<String, ViewData> classes = new LinkedHashMap<>();
    private final List<Notice> notices = new ArrayList<>();
    private String current;

    public DemoStore() { reset(); }
    public void reset() {
        current=null; people.clear(); classes.clear(); notices.clear();
        people.put(STUDENT, new Person(STUDENT, "Demo Student", "1900001", "student@example.test", "Not provided (demo)", false, false));
        people.put(CR, new Person(CR, "Demo Class Representative", "1900002", "cr@example.test", "Not provided (demo)", true, false));
        people.put("demo-student-b", new Person("demo-student-b", "Demo Student B", "1900003", "student-b@example.test", "Not provided (demo)", false, false));
        people.put(ADMIN, new Person(ADMIN, "Demo Administrator", "DEMO-ADMIN", "admin@example.test", "Not provided (demo)", false, true));
        String[] courses={"Database Systems", "Computer Networks", "Software Engineering"};
        for (String section : Arrays.asList("A", "B", "C")) for (String day : DAYS) for (int slot=0;slot<3;slot++) {
            classes.put(key(section, day, TIMES.get(slot)), new ViewData("Demo Instructor " + (char)('A'+slot), courses[slot], "Room " + (201+slot), TIMES.get(slot)));
        }
        notices.add(new Notice("Demo Class Representative", "Sample lab reminder", "Demo data: bring your lab notebook for the practice session. This is not a real class announcement."));
        notices.add(new Notice("Demo Class Representative", "Sample routine update", "Demo data: review the timetable for CSE, series 19, section A. No notification has been sent."));
    }
    public void select(String id) { if (!people.containsKey(id)) throw new IllegalArgumentException("Unknown sample account"); current=id; }
    public boolean selected() { return current != null; }
    public Person person() { if (current == null) throw new IllegalStateException("Choose a sample account"); return people.get(current); }
    public boolean canEdit() { return selected() && person().cr && !person().admin; }
    public boolean isAdmin() { return selected() && person().admin; }
    private void requireCr() { if (!canEdit()) throw new SecurityException("Select a sample CR account first"); }
    private void requireAdmin() { if (!isAdmin()) throw new SecurityException("Select the sample administrator first"); }
    private static String key(String section, String day, String time) { return section+"/"+day+"/"+time; }
    private static String text(String value) { if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException("Complete all sample fields"); return value.trim(); }
    private static void validateSlot(String day, String time) { if (!DAYS.contains(day) || !TIMES.contains(time)) throw new IllegalArgumentException("Choose a valid day and time"); }
    public List<ViewData> routine(String section, String day, String time) {
        List<ViewData> result=new ArrayList<>();
        for (String slot : TIMES) {
            ViewData value=classes.get(key(section, day, slot));
            if (value != null && ("All Day".equals(time) || slot.equals(time))) result.add(new ViewData(value.getName(), value.getCourse(), value.getRoom(), value.getTime()));
        }
        result.sort(Comparator.comparingInt(value -> TIMES.indexOf(value.getTime())));
        return result;
    }
    public void saveClass(String day, String time, String teacher, String course, String room) {
        requireCr(); validateSlot(day,time);
        classes.put(key(person().section,day,time),new ViewData(text(teacher),text(course),text(room),time));
    }
    public void deleteClass(String day, String time) { requireCr(); validateSlot(day,time); classes.remove(key(person().section,day,time)); }
    public void deleteRoutine() { requireCr(); String prefix=person().section+"/"; classes.keySet().removeIf(key -> key.startsWith(prefix)); }
    public List<Notice> notices() {
        List<Notice> copy=new ArrayList<>();
        for (Notice n:notices) copy.add(new Notice(n.getSender(),n.getTitle(),n.getBody()));
        return copy;
    }
    public void addNotice(String title, String body) { requireCr(); notices.add(0,new Notice(person().name,text(title),text(body))); }
    public Person findStudent(String roll) {
        requireAdmin();
        for (Person p:people.values()) if (!p.admin && p.roll.equals(roll.trim())) return p;
        return null;
    }
    public void setCr(String id, boolean enabled) {
        requireAdmin(); Person p=people.get(id);
        if (p == null || p.admin) throw new IllegalArgumentException("Choose a sample student");
        people.put(id,new Person(p.id,p.name,p.roll,p.email,p.phone,enabled,false));
    }
    public List<Person> representatives(String department, String series, String section) {
        requireAdmin(); List<Person> result=new ArrayList<>();
        for (Person p:people.values()) if (p.cr && p.department.equals(department) && p.series.equals(series) && p.section.equals(section)) result.add(p);
        return result;
    }
    public void updateProfile(String name, String phone) {
        Person p=person(); people.put(p.id,new Person(p.id,text(name),p.roll,p.email,text(phone),p.cr,p.admin));
    }
}
