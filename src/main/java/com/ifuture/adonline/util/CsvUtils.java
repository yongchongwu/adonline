package com.ifuture.adonline.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CsvUtils {

    private final static String NEW_LINE_SEPARATOR = "\n";

    public static List<Map<String, Object>> csvToMapList(String csvFilePath) {
        List<Map<String, Object>> list = new ArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(csvFilePath));
            String[] nameArr = br.readLine().split(",");
            String paramLine = null;
            while ((paramLine = br.readLine()) != null) {
                Map<String, Object> map = new HashMap<>();
                String[] paramLineArr = paramLine.split(",");
                for (int i = 0; i < paramLineArr.length; i++) {
                    map.put(nameArr[i], paramLineArr[i]);
                }
                list.add(map);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public static void mapListToCsv(String[] headers, List<Map<String, Object>> list,
        String resultFilePath) {

        CSVFormat formator = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR)
            .withHeader(headers);
        FileWriter fileWriter = null;
        CSVPrinter printer = null;
        try {
            fileWriter = new FileWriter(resultFilePath);
            printer = new CSVPrinter(fileWriter, formator);
            //String[] keyArray = keys.toArray(new String[keys.size()]);
            for (Map<String, Object> map : list) {
                for (String key : headers) {
                    printer.print(map.get(key));
                }
                printer.println();
            }
            printer.flush();
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != printer) {
                try {
                    printer.close();
                } catch (IOException e) {
                    printer = null;
                    e.printStackTrace();
                }
            }
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    fileWriter = null;
                    e.printStackTrace();
                }
            }
        }
    }

    public static void listToCsv(String[] headers, List dataList, String resultFilePath) {
        CSVFormat formator = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR)
            .withHeader(headers);
        FileWriter fileWriter = null;
        CSVPrinter printer = null;
        try {
            fileWriter = new FileWriter(resultFilePath);
            printer = new CSVPrinter(fileWriter, formator);
            for (Object val : dataList) {
                printer.printRecord(val);
            }
            printer.flush();
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != printer) {
                try {
                    printer.close();
                } catch (IOException e) {
                    printer = null;
                    e.printStackTrace();
                }
            }
            if (null != fileWriter) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    fileWriter = null;
                    e.printStackTrace();
                }
            }
        }
    }
}
