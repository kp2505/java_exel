package services;

public class TimeService {
    long startTime;

    public TimeService() {
        this.startTime = System.currentTimeMillis();
    }

    public void logTimeOffset() {
        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - this.startTime)/1000 + " seconds");
    }
}
