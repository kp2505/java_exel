import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import data.Data;
import facades.DatabaseFacade;
import services.LogService;
import services.TimeService;


public class Main {
    public static void main(String args[]) {
        LogService logService = new LogService();
        TimeService timeService = new TimeService(logService);
        DatabaseFacade databaseFacade = new DatabaseFacade(logService);
        ReportExcelReader reader = new ReportExcelReader(logService);
        ReportExcelWriter  writer = new ReportExcelWriter(logService);

        databaseFacade.clearTable();
        List<Data> inputData = reader.read();

        databaseFacade.insert(inputData);

        List<Data> outputData = databaseFacade.select();

        writer.write(outputData);
        timeService.logTimeOffset();
    }
}
