package models;

import java.util.ArrayList;
import java.util.List;

public class Specialization {
    private String name;
    private List<Subject> subjects;

    public Specialization(String name) {
        this.name = name;
        subjects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSubject(Subject subject){
        subjects.add(subject);
    }

    @Override
    public String toString() {
        return System.lineSeparator() + "Specialization{" +
                "name='" + name + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
