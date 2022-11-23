package services;

import com.opencsv.CSVReader;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CsvReaderService {
    public BufferedReader read(String path) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return br;
    }
}
