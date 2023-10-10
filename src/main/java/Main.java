import models.Faculty;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import parsers.FenScheduleParser;
import parsers.NormalScheduleParser;
import parsers.ScheduleParser;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileInputStream inputStream = new FileInputStream("3.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        ScheduleParser parser = new NormalScheduleParser();
        Faculty schedule = parser.getSchedule(sheet);
        System.out.println(schedule);
    }
}
