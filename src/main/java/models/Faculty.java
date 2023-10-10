package models;

import java.util.ArrayList;
import java.util.List;

public class Faculty {
    private String name;
    private List<Specialization> specializations;

    public Faculty(String name) {
        this.name = name;
        specializations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSpecialization(Specialization specialization){
        specializations.add(specialization);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", specializations=" + specializations +
                '}';
    }
}
