package services;

public class TimeService {
    long startTime;
    LogService logService;

    public TimeService( LogService logService) {
        this.startTime = System.currentTimeMillis();
        this.logService = logService;
        this.logService.log("###STARTED###");
    }

    public void logTimeOffset() {
        long endTime = System.currentTimeMillis();

        this.logService.log("###FINISHED###\n That took " + (endTime - this.startTime)/1000 + " seconds");
    }
}
