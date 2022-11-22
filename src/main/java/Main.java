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
        ReportExcelReader reader = new ReportExcelReader(databaseFacade);
        ReportExcelWriter  writer = new ReportExcelWriter();

        databaseFacade.clearTable();
        reader.read();

        List<Data> data = databaseFacade.select();

        writer.write(data);
        timeService.logTimeOffset();
    }
}
