package services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LogService {
    PrintStream printStream;

    public LogService() {
        try {

            System.setErr(new PrintStream(new File("log.txt")));
            this.printStream = new PrintStream(new File("log2.txt"));
            //System.setErr(new PrintStream(new FileOutputStream("log.txt", true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void log(String  message) {
        System.err.println(message);
        System.out.println(message);
    }

    public void log(int message) {
        System.err.println(message);
        System.out.println(message);
    }

    public void writeUnhandledRow(String row) {
        this.printStream.println(row);
    }
}
