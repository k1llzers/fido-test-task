package parsers;

import models.Faculty;
import models.Specialization;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NormalScheduleParser extends ScheduleParser{

    @Override
    public Faculty getSchedule(XSSFSheet sheet) {
        XSSFCell cell = sheet.getRow(5).getCell(0);
        Faculty faculty = new Faculty(cell.toString());
        faculty.addSpecialization(getSpecialization(sheet));
        return faculty;
    }

    private Specialization getSpecialization(XSSFSheet sheet){
        StringBuilder builder = new StringBuilder(sheet.getRow(6).getCell(0).toString());
        Specialization specialization
                = new Specialization(builder.substring(builder.indexOf(" ")).replaceAll("\"",""));
        List<Row> rows = new ArrayList<>();
        for (Row row:sheet)
            rows.add(row);
        Map<String, List<Row>> subjects = rows.stream()
                .skip(10)
                .filter(row -> row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty())
                .collect(Collectors.toMap(row -> {
                            if (row.getCell(2).getStringCellValue().contains(",")) {
                                return row.getCell(2).getStringCellValue().substring(0, row.getCell(2).getStringCellValue().indexOf(","));
                            }
                            return row.getCell(2).getStringCellValue();
                        },
                        row -> new LinkedList<>(List.of(row)),
                        (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }));
        for (Map.Entry<String, List<Row>> subject:subjects.entrySet()) {
            specialization.addSubject(getSubject(subject));
        }
        return specialization;
    }
}
