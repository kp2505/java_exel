import com.opencsv.CSVReader;
import data.Provider;
import data.ProviderConfig;
import data.ProviderInfo;
import data.Sku;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import services.CsvReaderService;
import services.ExelReaderService;
import services.LogService;

import java.io.*;
import java.util.*;

class ReportExcelReader {
    LogService logService;
    ProviderConfig providerConfig;

    public ReportExcelReader(LogService logService, ProviderConfig providerConfig) {
        this.logService = logService;
        this.providerConfig = providerConfig;
    }

    public List read(String fileFolder) {
        ExelReaderService exelReaderService = new ExelReaderService();
        List dataList = new LinkedList();
        File folder = new File(fileFolder);
        File[] listOfFiles = folder.listFiles();
        this.logService.log("Read of data was started");

        for (File file : listOfFiles) {
            String fileName = file.getName();
            String path = fileFolder + "\\" + fileName;
            this.logService.log(fileName);

            if (file.isFile()) {
                ProviderInfo providerConfig = this.findConfigForProvider(fileName);

                if (fileName.contains("csv")) {
                    try {
                        dataList.addAll(this.getCsvProviderList(path, providerConfig));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Sheet sheet = exelReaderService.read(path);
                    Iterator<Row> rows = sheet.iterator();
                    dataList.addAll(this.getProviderList(rows, fileName, providerConfig));
                }
            }
        }

        this.logService.log("end of Read of data");

        return dataList;
    }

    public ProviderInfo findConfigForProvider(String fileName) {
        Iterator<ProviderInfo> providerInfoIterator = this.providerConfig.providerInfoList.iterator();
        ProviderInfo result = null;

        while (providerInfoIterator.hasNext()) {
            ProviderInfo providerInfo = providerInfoIterator.next();

            if (fileName.contains(providerInfo.fileName)) {
                result = providerInfo;
            }
        }

        return result;
    }

    private Boolean isProviderValid(Provider provider, ProviderInfo providerConfig) {
        Boolean isSkuValid = !provider.sku.equals("-");
        Boolean isPriceValid =
                provider.price >= providerConfig.minPrice &&
                        provider.price <= providerConfig.maxPrice;
        Boolean isBrandNameValid = !this.providerConfig.forbiddenBrands.contains(provider.brand);
        Boolean isMinCountValid = provider.minCount == 1 || provider.minCount == 0;
        Boolean isCountValid = provider.count != 0;

        this.logService.writeUnhandledRow(provider.getProvider());

        return isSkuValid && isPriceValid && isBrandNameValid && isMinCountValid && isCountValid;
    }

    public List getCsvProviderList(String path, ProviderInfo providerConfig) throws IOException {
        CsvReaderService csvReaderService = new CsvReaderService();
        BufferedReader br = csvReaderService.read(path);
        List providerList = new LinkedList();
        int rowNumber = 0;
        int[] validPositions = this.getValidPositionField(path);
        int startRowNumber = this.getStartRowNumber(path);
        int numberOfColumn = this.getNumberOfColumn(path);
        int errors = 0;
        String line = "";

        while ((line = br.readLine()) != null) {
            rowNumber++;
            String[] items = line.split(";");

            if (rowNumber < startRowNumber) {
            } else {

                Provider provider = this.getDataByCsv(items, validPositions, numberOfColumn, providerConfig);

                provider.sku = this.getProviderSku(provider);

                Boolean isProviderValid = this.isProviderValid(provider, providerConfig);

                if (isProviderValid) {
                    providerList.add(provider);
                }

                if(provider.brand.equals("")) {
                    this.logService.writeUnhandledRow(providerConfig.fileName+";"+line);
                    errors++;
                }

                if (rowNumber % 10000 == 0) {
                    //System.out.println("Строка заполнения " + rowNumber);
                }
            }
        }

        System.out.println("Количество строк: " + rowNumber);
        System.out.println("Количество валидных строк: " + providerList.size());
        System.out.println("Количество валидных ошибок: " + errors);

        return providerList;
    }

    public List getProviderList(Iterator<Row> rows, String fileName, ProviderInfo providerConfig) {
        List providerList = new LinkedList();
        long rowNumber = 0;
        int[] validPositions = this.getValidPositionField(fileName);
        int startRowNumber = this.getStartRowNumber(fileName);
        int numberOfColumn = this.getNumberOfColumn(fileName);

        while (rows.hasNext()) {
            rowNumber++;
            Row row = rows.next();

            if (rowNumber < startRowNumber) {
            } else {
                Provider provider = this.getDataByRow(row, validPositions, providerConfig, numberOfColumn);

                provider.sku = this.getProviderSku(provider);

                Boolean isProviderValid = this.isProviderValid(provider, providerConfig);

                if(provider.minCount == -1000) {
                    this.logService.writeUnhandledRow(provider.getProvider());
                }

                if (isProviderValid) {
                    providerList.add(provider);
                }
            }
        }

        System.out.println("Количество строк: " + rowNumber);
        System.out.println("Количество валидных строк: " + providerList.size());

        return providerList;
    }

    public String getProviderSku(Provider provider) {
        String resultSku = "-";

        Comparator<Sku> c = new Comparator<Sku>() {
            public int compare(Sku u1, Sku u2) {
                return u1.getCompareValue().compareTo(u2.getCompareValue());
            }
        };
        int index = Collections.binarySearch(this.providerConfig.existedSkuList,
                new Sku(provider.vendorCode + " " + provider.brand, ""), c);

        if (index >= 0) {
            Sku sku2 = this.providerConfig.existedSkuList.get(index);
            resultSku = sku2.value;
        }

        return resultSku;
    }

    public int[] getValidPositionField(String fileName) {
        int[] defaultPositions = {1, 3, 5, 6, 8};

        if (fileName.contains("1.Berg")) {
            int[] positions = {1, 3, 5, 6, 8};
            return positions;
        }

        if (fileName.contains("2.Forum")) {
            int[] positions = {2, 1, 6, 5, 7 };
            return positions;
        }

        if (fileName.contains("3.Rossko")) {
            int[] positions = {2, 1, 6, 5, 4 };
            return positions;
        }

        // Возможно,битый файл
        if (fileName.contains("4.Armtek")) {
            int[] positions = {2, 1, 6, 8, 7 };
            return positions;
        }

        if (fileName.contains("5.Auto-Euro")) {
            int[] positions = {4, 1, 9, 7, 10 };
            return positions;
        }

        if (fileName.contains("6.Autorus")) {
            int[] positions = {2 ,3 ,6 ,8 ,7 };
            return positions;
        }

        if (fileName.contains("7.Partkom")) {
            int[] positions = {1 ,2 ,5 ,4 ,6 };
            return positions;
        }

        if (fileName.contains("8.Shate-M")) {
            int[] positions = {2 ,1 ,4 ,7 ,5 };
            return positions;
        }

        if (fileName.contains("9.Mikado")) {
            int[] positions = {2 ,3 ,6 ,5 ,8 };
            return positions;
        }

        if (fileName.contains("test")) {
            int[] positions = { 1,3,10,6,10000 };
            return positions;
        }

        return defaultPositions;
    }

    public int getStartRowNumber(String fileName) {
        int defaultRowNumber = 1;

        if (fileName.contains("1.Berg")) {
            return 2;
        }

        if (fileName.contains("2.Forum")) {
            return 5;
        }

        if (fileName.contains("3.Rossko")) {
            return 2;
        }

        if (fileName.contains("4.Armtek")) {
            return 2;
        }

        if (fileName.contains("5.Auto-Euro")) {
            return 2;
        }

        if (fileName.contains("6.Autorus")) {
            return 2;
        }

        if (fileName.contains("7.Partkom")) {
            return 1;
        }

        if (fileName.contains("8.Shate-M")) {
            return 2;
        }

        if (fileName.contains("9.Mikado")) {
            return 1;
        }

        if (fileName.contains("test")) {
            return 2;
        }

        return defaultRowNumber;
    }

    private int getNumberOfColumn(String path) {
        int defaultRowNumber = 1;

        if (path.contains("1.Berg")) {
            return 12;
        }

        if (path.contains("2.Forum")) {
            return 9;
        }

        if (path.contains("3.Rossko")) {
            return 6;
        }

        if (path.contains("4.Armtek")) {
            return 8;
        }

        if (path.contains("5.Auto-Euro")) {
            return 10;
        }

        if (path.contains("6.Autorus")) {
            return 11;
        }

        if (path.contains("7.Partkom")) {
            return 7;
        }

        if (path.contains("8.Shate-M")) {
            return 8;
        }

        if (path.contains("9.Mikado")) {
            return 8;
        }

        if (path.contains("test")) {
            return 10;
        }

        return defaultRowNumber;
    }

    public Provider getDataByRow(Row row, int[] validPositions, ProviderInfo providerConfig, int numberOfColumn) {
        Iterator<Cell> cells = row.iterator();
        String
                provider = providerConfig.provider,
                vendorCode = "",
                brand = "",
                sku = "";
        double
                count = 0,
                minCount = 0,
                rating = providerConfig.priority,
                price = 0,
                maxPrice = 0,
                minPrice = 0;

        int i = 0;

        while (cells.hasNext()) {
            Cell cell = cells.next();

            i++;

            if (i == validPositions[0]) {
                vendorCode = this.getCellValue(cell);

            }

            if (i == validPositions[1]) {
                brand = this.getCellValue(cell);
            }

            if (i == validPositions[2]) {
                String countValue = this.getNumericValue(cell);

                count = (int) Double.parseDouble(countValue);
            }

            if (i == validPositions[3]) {
                Float priceValue = Float.valueOf(this.getNumericValue(cell));

                price = this.getCalculatedPrice(priceValue, 1);
                minPrice = this.getCalculatedPrice(priceValue, providerConfig.priceFromParam);
                maxPrice = this.getCalculatedPrice(priceValue, providerConfig.priceToParam);
            }

            if (i == validPositions[4]) {
                String countValue = this.getNumericValue(cell);

                minCount = (int) Double.parseDouble(countValue);
            }
        }

        if(i != numberOfColumn) {
            minCount = -1000;
        }

        sku = vendorCode + " " + brand;

        //this.logService.log(sku);

        return new Provider(
                provider,
                vendorCode,
                brand,
                sku,
                count,
                minCount,
                price,
                maxPrice,
                minPrice,
                rating
        );
    }

    public String getNumericValue(Cell cell) {
        String numericValue = this.getCellValue(cell);

        if(numericValue.contains("-")) {
            numericValue = numericValue.split("-")[0];
        }

        if(numericValue.contains(">")) {
            return numericValue.replace(">", "");
        }

        if(numericValue.contains("<")) {
            return numericValue.replace("<", "");
        }

        if(numericValue.contains(" ")) {
            return numericValue.replace(" ", "");
        }


        return numericValue;
    }

    public String getCellValue(Cell cell) {
        String numericValue = "";


        if(CellType.STRING == cell.getCellType()) {
            numericValue = cell.getStringCellValue();
        } else {
            numericValue = cell.getNumericCellValue() + "";
        }

        return numericValue;
    }

    public Provider getDataByCsv(String[] items, int[] validPositions, int size, ProviderInfo providerConfig) {
        Iterator<String> cells = Arrays.stream(items).iterator();
        String
                provider = providerConfig.provider,
                vendorCode = "",
                brand = "",
                sku = "";
        double
                count = 0,
                minCount = 0,
                rating = providerConfig.priority,
                price = 0,
                maxPrice = 0,
                minPrice = 0;

        int i = 0;

        if (items.length == size) {
            while (cells.hasNext()) {
                String cell = cells.next().replaceAll("\"", "");

                i++;
                //this.logService.log(cell);
                //this.logService.log(i);
                if (i == validPositions[0]) {
                    vendorCode = cell;
                }

                if (i == validPositions[1]) {
                    brand = cell;
                }

                if (i == validPositions[2]) {
                    count = Integer.parseInt(this.getCount(cell));
                }

                if (i == validPositions[3]) {
                    Float priceValue = Float.valueOf(this.getPrice(cell));

                    price = this.getCalculatedPrice(priceValue, 1);
                    minPrice = this.getCalculatedPrice(priceValue, providerConfig.priceFromParam);
                    maxPrice = this.getCalculatedPrice(priceValue, providerConfig.priceToParam);
                }

                if (i == validPositions[4]) {
                    minCount = Integer.parseInt(this.getCount(cell));
                }
            }
        } else {
            this.logService.log("Ошибка парсинга");
        }


        sku = vendorCode + " " + brand;

        return new Provider(
                provider,
                vendorCode,
                brand,
                sku,
                count,
                minCount,
                price,
                maxPrice,
                minPrice,
                rating
        );
    }

    public String getPrice(String price) {

        Boolean isNeedFormat = price.contains(",");
        if (isNeedFormat) {
            return price.replace(",", ".");
        }

        return price;
    }

    private String getCount(String value) {
        Boolean more = value.contains(">");
        Boolean less = value.contains("<");
        if (more || less) {
            String separator = more ? ">" : "<";

            return value.replace(separator, "");
        }

        if(value.contains("+")) {
            return value.replace("+", "");
        }

        return value;
    }

    public double getCalculatedPrice(double price,double multiplyParam) {
        double resultPrice = price < 500 ? price + 155 : price;

        return resultPrice * multiplyParam;
    }
}