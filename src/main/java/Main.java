import java.util.List;

import data.Provider;
import data.ProviderConfig;
import facades.DatabaseFacade;
import services.LogService;
import services.TimeService;


public class Main {
    public static void main(String args[]) {
        LogService logService = new LogService();
        TimeService timeService = new TimeService(logService);
        ProvidersConfigReader providersConfigReader = new ProvidersConfigReader();

        ProviderConfig providerConfig = providersConfigReader.read("config");


        DatabaseFacade databaseFacade = new DatabaseFacade(logService);

        ReportExcelReader reader = new ReportExcelReader(logService,providerConfig);
        ReportExcelWriter  writer = new ReportExcelWriter(logService,providerConfig);

        databaseFacade.clearTable();
        List<Provider> inputData = reader.read("./test");

        databaseFacade.insert(inputData);

        List<Provider> outputData = databaseFacade.select();

        writer.write(outputData);

        timeService.logTimeOffset();
    }
}
