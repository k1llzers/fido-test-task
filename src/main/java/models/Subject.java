package models;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private String name;
    private List<Group> groups;

    public Subject(String name) {
        this.name = name;
        groups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGroup(Group group){
        groups.add(group);
    }

    @Override
    public String toString() {
        return System.lineSeparator() + "Subject{" +
                "name='" + name + '\'' +
                ", groups=" + groups +
                '}' + System.lineSeparator();
    }
}
