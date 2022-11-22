package services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class LogService {
    public LogService() {
        try {

            // Если нужно в новый файл System.setErr(new PrintStream(new File("log.txt")));
            System.setErr(new PrintStream(new FileOutputStream("log.txt", true)));
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
}
