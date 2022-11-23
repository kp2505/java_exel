import data.Provider;
import data.ProviderConfig;
import data.ProviderInfo;
import org.apache.poi.hssf.usermodel.HSSFCell;
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
    ProviderConfig providerConfig;

    public ReportExcelWriter(LogService logService, ProviderConfig providerConfig) {
        this.wb = new XSSFWorkbook();
        this.sheet = wb.createSheet();
        createTitle();
        this.logService = logService;
        this.providerConfig = providerConfig;
    }

    public void createRow(int index, Provider provider) {
        XSSFRow row = sheet.createRow(index);
        setCellValue(row.createCell(0), this.getProviderName(provider.rating));
        setCellValue(row.createCell(1), provider.vendorCode);
        setCellValue(row.createCell(2), provider.brand);
        setCellValue(row.createCell(3), provider.sku);
        setCellValue(row.createCell(4), provider.count);
        setCellValue(row.createCell(5), provider.price);
        setCellValue(row.createCell(6), provider.minPrice);
        setCellValue(row.createCell(7), provider.maxPrice);
        setCellValue(row.createCell(8), provider.minCount);
        setCellValue(row.createCell(9), provider.rating);
    }

    public String getProviderName(double rating) {
        Iterator<ProviderInfo> providerInfoIterator = this.providerConfig.providerInfoList.iterator();
        String resultName = "";

        while (providerInfoIterator.hasNext()) {
            ProviderInfo providerInfo = providerInfoIterator.next();
            if(providerInfo.priority == rating) {
                resultName = providerInfo.fileName;
            }
        }

        return resultName;
    }

    private void writeWorkbook() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("C:\\Users\\User\\Downloads\\result.csv");
        wb.write(fileOut);
        fileOut.close();
    }

    private void createTitle() {
        XSSFRow rowTitle = sheet.createRow(0);
        setCellValue(rowTitle.createCell(0), "Provider");
        setCellValue(rowTitle.createCell(1), "Vendor code");
        setCellValue(rowTitle.createCell(2), "Brand");
        setCellValue(rowTitle.createCell(3), "Sku");
        setCellValue(rowTitle.createCell(4), "Count");
        setCellValue(rowTitle.createCell(5), "price");
        setCellValue(rowTitle.createCell(6), "min price");
        setCellValue(rowTitle.createCell(7), "max price");
        setCellValue(rowTitle.createCell(8), "min count");
        setCellValue(rowTitle.createCell(9), "rating");
    }

    public void write(List<Provider> data) {
        int i = 0;

        Iterator<Provider> it  = data.iterator();

        while (it.hasNext()) {
            Provider item = it.next();
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

    private void setCellValue(XSSFCell cell, double value) {
        cell.setCellValue(value);
    }

    private void setCellValue(XSSFCell cell, Instant value) {
        cell.setCellValue(value.toString());
    }
}