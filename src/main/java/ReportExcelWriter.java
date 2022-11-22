import data.Data;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import services.LogService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;

class ReportExcelWriter {

    private final XSSFWorkbook wb;
    private final XSSFSheet sheet;
    LogService logService;

    public ReportExcelWriter(LogService logService) {
        this.wb = new XSSFWorkbook();
        this.sheet = wb.createSheet();
        createTitle();
        this.logService = logService;
    }

    public void createRow(int index, Data data) {
        XSSFRow row = sheet.createRow(index);
        setCellValue(row.createCell(0), data.val1);
        setCellValue(row.createCell(1), data.val2);
        setCellValue(row.createCell(2), data.val3);
        setCellValue(row.createCell(3), data.val4);
    }

    private void writeWorkbook() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\User\\Downloads\\result.csv");
        wb.write(fileOut);
        fileOut.close();
    }

    private void createTitle() {
        XSSFRow rowTitle = sheet.createRow(0);
        setCellValue(rowTitle.createCell(0), "column_1");
        setCellValue(rowTitle.createCell(1), "Name");
        setCellValue(rowTitle.createCell(2), "Rating");
        setCellValue(rowTitle.createCell(3), "Text");
    }

    public void write(List<Data> data) {
        int i = 0;

        Iterator<Data> it  = data.iterator();

        while (it.hasNext()) {
            Data item = it.next();
            i++;

            if (i % 1000 == 0) {
                this.logService.log(i);
            }
            this.createRow(i,item);
        }
        try {
            this.writeWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    private void setCellValue(XSSFCell cell, String value) {
        cell.setCellValue(value);
    }

    private void setCellValue(XSSFCell cell, long value) {
        cell.setCellValue(value);
    }

    private void setCellValue(XSSFCell cell, Instant value) {
        cell.setCellValue(value.toString());
    }
}