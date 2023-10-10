package parsers;

import models.Faculty;
import models.Group;
import models.Subject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.IntStream;

public abstract class ScheduleParser {
    public abstract Faculty getSchedule(XSSFSheet sheet);

    Subject getSubject(Map.Entry<String, List<Row>> subjectEntry){
        String nameOfSubject = subjectEntry.getKey();
        if (nameOfSubject.contains("("))
            nameOfSubject = nameOfSubject.substring(0,nameOfSubject.indexOf("("));
        Subject subject = new Subject(nameOfSubject.trim());
        List<Row> rowsOfGroup = subjectEntry.getValue();
        for(Row group:rowsOfGroup){
            subject.addGroup(getGroup(group));
        }
        return subject;
    }

    public static ScheduleParser getParser(XSSFSheet sheet){
        if (sheet.getRow(1).getCell(2).toString().contains("Глущенко"))
            return new FenScheduleParser();
        return new NormalScheduleParser();
    }

    private Group getGroup(Row group){
        String groupName = group.getCell(3).toString();
        if (groupName.toLowerCase(Locale.ROOT).contains("лекція"))
            groupName = "Лекція";
        else groupName = groupName.charAt(0) + "";
        if (group.getCell(3).getCellType() == CellType.NUMERIC)
            groupName = "" + (int) Double.parseDouble(groupName);
        LocalTime[] time = getTime(group.getCell(1));
        int[] weeks = getWeeks(group.getCell(4));
        String auditory = group.getCell(5).toString();
        return new Group(groupName,time[0],time[1],weeks,auditory,getDayOfWeek(group.getCell(0)));
    }

    private LocalTime[] getTime(Cell cell){
        String[] time;
        if (cell.toString().isEmpty()){
            CellRangeAddress rangeAddress = getRangeAddress(cell);
            time = cell.getSheet().getRow(rangeAddress.getFirstRow())
                    .getCell(rangeAddress.getFirstColumn()).getStringCellValue().replaceAll("\\.",":").split("-");
        } else
            time = cell.getStringCellValue().replaceAll("\\.",":").split("-");
        if (time[0].length() == 4)
            time[0] = "0" + time[0];
        if (time[1].length() == 4)
            time[1] = "0" + time[1];
        LocalTime start = LocalTime.parse(time[0]);
        LocalTime end = LocalTime.parse(time[1]);
        return new LocalTime[]{start,end};
    }

    private String getDayOfWeek(Cell cell){
        if (cell.toString().isEmpty()){
            CellRangeAddress rangeAddress = getRangeAddress(cell);
            return cell.getSheet().getRow(rangeAddress.getFirstRow())
                    .getCell(rangeAddress.getFirstColumn()).toString();
        } else
            return cell.toString();
    }

    private CellRangeAddress getRangeAddress(Cell cell){
        List<CellRangeAddress> mergedRegions = cell.getRow().getSheet().getMergedRegions();
        for (CellRangeAddress address:mergedRegions)
            if (address.isInRange(cell))
                return address;
        return null;
    }

    private int[] getWeeks(Cell cell){ // can be abstract
        String weeksAsString = cell.toString();
        return Arrays.stream(weeksAsString.split(",")).flatMapToInt(e -> {
            if (!e.contains("-"))
                return IntStream.of(Integer.parseInt(e));
            else {
                String[] splitWeek = e.split("-");
                return IntStream.range(Integer.parseInt(splitWeek[0].trim()), Integer.parseInt(splitWeek[1].trim()) + 1);
            }
        }).toArray();
    }
}

