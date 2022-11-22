import java.io.*;
import java.util.Iterator;
import java.util.List;

import data.Data;
import facades.DatabaseFacade;
import services.TimeService;


public class Main {
    public static void main(String args[]) {
        TimeService timeService = new TimeService();
        DatabaseFacade databaseFacade = new DatabaseFacade();
        ReportExcelReader reader = new ReportExcelReader();
        ReportExcelWriter  writer = new ReportExcelWriter();

        databaseFacade.clearTable();
        List<Data> inputData = reader.read();

        databaseFacade.insert(inputData);

        List<Data> outputData = databaseFacade.select();

        writer.write(outputData);
        timeService.logTimeOffset();
    }
}
