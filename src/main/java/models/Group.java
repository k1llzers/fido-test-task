package models;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;

public class Group {
    private String name;
    private LocalTime start;
    private LocalTime end;
    private int[] weeks;
    private String auditory;
    private String dayOfWeek;

    public Group(String name, LocalTime start, LocalTime end, int[] weeks, String auditory, String dayOfWeek) {
        this.name = name;
        this.start = start;
        this.end = end;
        this.weeks = weeks;
        this.auditory = auditory;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return System.lineSeparator() + "Group{" +
                "name='" + name + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", weeks=" + Arrays.toString(weeks) +
                ", auditory='" + auditory + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                '}';
    }
}
