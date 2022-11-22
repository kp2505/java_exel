import data.Data;
import facades.DatabaseFacade;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class ReportExcelReader {
    public List read() {
        InputStream inputStream = null;
        XSSFWorkbook workBook = null;

        try {
            inputStream = new FileInputStream("C:\\Users\\User\\Downloads\\input.csv");
            workBook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        List dataList = new LinkedList();

        while (rows.hasNext()) {
            Row row = rows.next();
            dataList.add(this.getDataByRow(row));
        }

        return dataList;
    }

    public Data getDataByRow(Row row) {
        Iterator<Cell> cells = row.iterator();
        String
                val1 = "",
                val2 = "",
                val3 = "",
                val4 = "";
        int i = 0;

        while (cells.hasNext()) {
            Cell cell = cells.next();

            i++;
            switch (i) {
                case 1:
                    val1 = cell.getStringCellValue();
                    break;
                case 2:
                    val2 = cell.getStringCellValue();
                    break;
                case 3:
                    val3 = cell.getStringCellValue();
                    break;
                case 4:
                    val4 = cell.getStringCellValue();
                    break;
            }
        }

        return new Data(val1, val2, val3, val4);
    }
}