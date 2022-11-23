package services;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExelReaderService {

    public Sheet read(String path) {
        InputStream inputStream = null;
        XSSFWorkbook workBook = null;
        try {
            inputStream = new FileInputStream(path);
            workBook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Sheet sheet = workBook.getSheetAt(0);

        return sheet;
    }
}
