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

public class FenScheduleParser extends ScheduleParser{
    private Specialization economic;
    private Specialization finance;
    private Specialization marketing;
    private Specialization management;

    @Override
    public Faculty getSchedule(XSSFSheet sheet) {
        XSSFCell cell = sheet.getRow(5).getCell(0);
        Faculty faculty = new Faculty(cell.toString());
        initializeTheSpeciality(sheet.getRow(6).getCell(0));
        fillSpecialization(sheet);
        faculty.addSpecialization(economic);
        faculty.addSpecialization(finance);
        faculty.addSpecialization(management);
        faculty.addSpecialization(marketing);
        return faculty;
    }

    private void initializeTheSpeciality(XSSFCell cell){
        String year = cell.toString().substring(cell.toString().length() - 6);
        economic = new Specialization("Економіка, " + year);
        finance = new Specialization("Фінанси, банківська справа та страхування, " + year);
        marketing = new Specialization("Маркетинг, " + year);
        management = new Specialization("Менеджмент, " + year);
    }

    private void fillSpecialization(XSSFSheet sheet){
        List<Row> rows = new ArrayList<>();
        for (Row row:sheet)
            rows.add(row);
        Map<String, List<Row>> subjects = rows.stream()
                .skip(10)
                .filter(row -> row.getCell(4) != null && !row.getCell(4).getStringCellValue().isEmpty())
                .collect(Collectors.toMap(row ->{
                            String name = row.getCell(2).toString();
                            if (name.contains("(Custumer experience)"))
                                name = name.substring(0,name.indexOf("(Custumer experience)"));
                            return name.substring(0, !name.contains(")") ?name.indexOf("\n"):(name.lastIndexOf(")") + 1));
                        },
                        row -> new LinkedList<>(List.of(row))
                        , (list1, list2) -> {
                            list1.addAll(list2);
                            return list1;
                        }));
        for (Map.Entry<String, List<Row>> subject:subjects.entrySet())
            for (Specialization specialization:findSpecialization(subject.getKey()))
                specialization.addSubject(getSubject(subject));
    }

    private List<Specialization> findSpecialization(String nameOfSubject){
        ArrayList<Specialization> specializations = new ArrayList<>();
        if (nameOfSubject.contains("(фін"))
            specializations.add(finance);
        if (nameOfSubject.contains("(ек"))
            specializations.add(economic);
        if (nameOfSubject.contains("(мар") || nameOfSubject.contains("+мар") || nameOfSubject.contains(" мар"))
            specializations.add(marketing);
        if (nameOfSubject.contains("(мен") || nameOfSubject.contains("+мен") || nameOfSubject.contains(" мен") || nameOfSubject.contains(",мен"))
            specializations.add(management);
        if (specializations.size() == 0) return List.of(economic,finance,management,marketing);
        return specializations;
    }
}
