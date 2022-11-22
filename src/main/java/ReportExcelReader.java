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

        return this.getDataList(rows);
    }

    public List getDataList(Iterator<Row> rows) {
        List dataList = new LinkedList();
        long rowNumber = 0;
        //Дефолтный порядок
        int[] validPositions = {1, 2, 3, 4};

        while (rows.hasNext()) {
            rowNumber++;
            Row row = rows.next();
            if (rowNumber == 1) {
                validPositions = getValidPositionField(row);
            } else {
                dataList.add(this.getDataByRow(row, validPositions));
            }
        }

        return dataList;
    }

    public int[] getValidPositionField(Row row) {
        int[] positions = {4, 2, 3, 1};

        return positions;
    }

    public Data getDataByRow(Row row, int[] validPositions) {
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

            if (i == validPositions[0]) {
                val1 = cell.getStringCellValue();
            }

            if (i == validPositions[1]) {
                val2 = cell.getStringCellValue();
            }

            if (i == validPositions[2]) {
                val3 = cell.getStringCellValue();
            }

            if (i == validPositions[3]) {
                val4 = cell.getStringCellValue();
            }
        }

        return new Data(val1, val2, val3, val4);
    }
}