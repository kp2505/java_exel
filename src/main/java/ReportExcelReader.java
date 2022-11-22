import facades.DatabaseFacade;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

class ReportExcelReader {
    DatabaseFacade databaseFacade;

    public ReportExcelReader(DatabaseFacade databaseFacade) {
        this.databaseFacade = databaseFacade;
    }

    public void read() {
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
        int i = 0;
        String result = "(";

        while (rows.hasNext()) {
            i++;
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();

            result += this.getQueryByRow(cells);

            if (i % 1000 == 0) {
                result += ");";
                System.out.println(i);
                this.databaseFacade.insert(result);
                result = "(";
            } else {
                Boolean hasNext = rows.hasNext();

                result += hasNext ? "),(" : ");";
                if (!hasNext) {
                    this.databaseFacade.insert(result);
                }
            }
        }
    }

    public String getQueryByRow(Iterator<Cell> cells) {
        String query = "";

        while (cells.hasNext()) {
            Cell cell = cells.next();
            String queryPart = this.getQueryPart(cell, cells.hasNext());

            query += queryPart;
        }

        return query;
    }

    public String getQueryPart(Cell cell, Boolean hasNext) {
        String queryPart = "";
        queryPart += "'" + cell.getStringCellValue() + "'";

        if (hasNext) {
            queryPart += ",";
        }

        return queryPart;
    }
}