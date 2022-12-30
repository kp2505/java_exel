import data.ProviderConfig;
import data.ProviderInfo;
import data.Sku;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.NumberToTextConverter;
import services.ExelReaderService;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

public class ProvidersConfigReader {

    public ProviderConfig read(String fileFolder) {
        ExelReaderService exelReaderService = new ExelReaderService();
        File folder = new File(fileFolder);
        File[] listOfFiles = folder.listFiles();
        List<String> forbiddenBrands = new LinkedList();
        List<ProviderInfo> providersInfo = new LinkedList();
        List<Sku> existedSkuList = new LinkedList();

        for (File file : listOfFiles) {
            String fileName = file.getName();

            if (file.isFile()) {

                System.out.println(fileName);

                if (fileName.equals("Forbidden_providers_config.xlsx")) {
                    Sheet sheet = exelReaderService.read(fileFolder + "\\" + fileName);
                    Iterator<Row> rows = sheet.iterator();
                    System.out.println("Forbidden_providers_config.xlsx started");
                    forbiddenBrands = this.getForbiddenBrands(rows);
                    System.out.println("Forbidden_providers_config.xlsx finished");
                }

                if (fileName.equals("Provider_config.xlsx")) {
                    Sheet sheet = exelReaderService.read(fileFolder + "\\" + fileName);
                    Iterator<Row> rows = sheet.iterator();
                    System.out.println("Provider_config.xlsx started");
                    providersInfo = this.getProviderInfoList(rows);
                    System.out.println("Provider_config.xlsx finished");
                }

                if (fileName.equals("SKU_config.xlsx")) {
                    Sheet sheet = exelReaderService.read(fileFolder + "\\" + fileName);
                    Iterator<Row> rows = sheet.iterator();
                    System.out.println("SKU_config_.xlsx started");
                    existedSkuList = this.getSkuConfig(rows);
                    System.out.println("SKU_config.xlsx finished");

                    Collections.sort(existedSkuList,new Comparator<Sku>() {
                        public int compare(Sku u1, Sku u2) {
                            return u1.getCompareValue().compareTo(u2.getCompareValue());
                        }
                    });
                }
            }
        }

        return new ProviderConfig(forbiddenBrands, providersInfo, existedSkuList);
    }

    public List<String> getForbiddenBrands(Iterator<Row> rows) {
        List<String> forbiddenBrands = new LinkedList();

        while (rows.hasNext()) {
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();

            while (cells.hasNext()) {
                Cell cell = cells.next();
                forbiddenBrands.add(cell.getStringCellValue());
            }
        }

        return forbiddenBrands;
    }

    public List<ProviderInfo> getProviderInfoList(Iterator<Row> rows) {
        List<ProviderInfo> providerInfoList = new LinkedList();
        int rowNumber = 0;

        while (rows.hasNext()) {
            rowNumber++;
            int priority = 0;
            double priceFrom = 0, priceTo = 0, maxPrice = 0, minPrice = 0;
            String provider = "", filename = "";
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();

            int i = 0;

            while (cells.hasNext()) {
                Cell cell = cells.next();
                if (rowNumber != 1) {
                    i++;

                    if (i == 1) {
                        priority = (int) cell.getNumericCellValue();
                    }

                    if (i == 2) {
                        provider = new String(cell.getStringCellValue().getBytes(Charset.forName("UTF-8")));
                    }

                    if (i == 3) {
                        filename = cell.getRichStringCellValue().getString();
                    }

                    if (i == 4) {
                        priceFrom = cell.getNumericCellValue();
                    }

                    if (i == 5) {
                        priceTo = cell.getNumericCellValue();
                    }

                    if (i == 6) {
                        minPrice = cell.getNumericCellValue();
                    }

                    if (i == 7) {
                        maxPrice = cell.getNumericCellValue();
                    }
                }
            }
            providerInfoList.add(new ProviderInfo(
                    priority,
                    provider,
                    filename,
                    priceFrom,
                    priceTo,
                    maxPrice,
                    minPrice
            ));
        }

        return providerInfoList;
    }

    public List<Sku> getSkuConfig(Iterator<Row> rows) {
        List<Sku> skuConfig =new ArrayList<Sku>();

        while (rows.hasNext()) {
            String id = "", value = "";
            Row row = rows.next();
            Iterator<Cell> cells = row.iterator();

            int i = 0;

            while (cells.hasNext()) {
                i++;
                Cell cell = cells.next();
                if (i == 1) {
                    id = this.getCellValue(cell);
                }

                if (i == 2) {
                    value = this.getCellValue(cell);
                }
                //skuConfig.add(cell.getStringCellValue());
                if (i % 10000 == 0) {
                    System.out.println(i);
                }
            }

            skuConfig.add(new Sku(id, value));
        }

        return skuConfig;
    }

    public String getCellValue(Cell cell) {
        String numericValue = "";

        if(CellType.STRING == cell.getCellType()) {
            numericValue = cell.getStringCellValue();
        } else {
            numericValue = NumberToTextConverter.toText(cell.getNumericCellValue());
        }

        return numericValue;
    }
}
