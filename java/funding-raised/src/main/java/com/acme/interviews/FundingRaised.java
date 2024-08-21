package com.acme.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class FundingRaised {
    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = createCsvRows();

        if(options.containsKey("company_name")) {
            csvData = filterBy(csvData, options, "company_name", 1);
        }

        if(options.containsKey("city")) {
            csvData = filterBy(csvData, options, "city", 4);
        }

        if(options.containsKey("state")) {
            csvData = filterBy(csvData, options, "state", 5);
        }

        if(options.containsKey("round")) {
            csvData = filterBy(csvData, options, "round", 9);
        }

        return createRowMaps(csvData);
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
        List<String[]> csvData = readFromCsvFile();
        if (hasHeaderRow(csvData)) {
            removeHeaderRow(csvData);
        }
        Map<String, String> mapped = new HashMap<String, String> ();

        for(int i = 0; i < csvData.size(); i++) {
            if(options.containsKey("company_name")) {
                if (!csvData.get(i)[1].equals(options.get("company_name"))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey("city")) {
                if (!csvData.get(i)[4].equals(options.get("city"))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey("state")) {
                if (!csvData.get(i)[5].equals(options.get("state"))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey("round")) {
                if (!csvData.get(i)[9].equals(options.get("round"))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            return mapped;
        }

        throw new NoSuchEntryException();
    }

    private static List<String[]> createCsvRows() throws IOException {
        List<String[]> csvData = readFromCsvFile();
        if (hasHeaderRow(csvData)) {
            removeHeaderRow(csvData);
        }
        return csvData;
    }

    private static List<String[]> readFromCsvFile() throws IOException {
        List<String[]> csvData = new ArrayList<String[]>();
        CSVReader reader = new CSVReader(new FileReader("startup_funding.csv"));
        String[] row = null;

        while((row = reader.readNext()) != null) {
            csvData.add(row);
        }

        reader.close();
        return csvData;
    }

    private static boolean hasHeaderRow(List<String[]> csvData) {
        // assume header to always exist
        return true;
    }

    private static void removeHeaderRow(List<String[]> csvData) {
        csvData.remove(0);
    }

    private static List<String[]> filterBy(List<String[]> csvData, Map<String, String> options, String option, int column) {
        List<String[]> results = new ArrayList<String[]>();

        for (int i = 0; i < csvData.size(); i++) {
            if (csvData.get(i)[column].equals(options.get(option))) {
                results.add(csvData.get(i));
            }
        }
        return results;
    }

    private static List<Map<String, String>> createRowMaps(List<String[]> csvData) {
        List<Map<String, String>> output = new ArrayList<Map<String, String>>();

        for(int i = 0; i < csvData.size(); i++) {
            output.add(createSingleRowMap(csvData.get(i)));
        }
        return output;
    }

    private static Map<String, String> createSingleRowMap(String[] row) {
        Map<String, String> mapped = new HashMap<String, String> ();
        addMappingsForRow(mapped, row);
        return mapped;
    }

    private static void addMappingsForRow(Map<String, String> mapped, String[] row) {
        mapped.put("permalink", row[0]);
        mapped.put("company_name", row[1]);
        mapped.put("number_employees", row[2]);
        mapped.put("category", row[3]);
        mapped.put("city", row[4]);
        mapped.put("state", row[5]);
        mapped.put("funded_date", row[6]);
        mapped.put("raised_amount", row[7]);
        mapped.put("raised_currency", row[8]);
        mapped.put("round", row[9]);
    }

    public static void main(String[] args) {
        try {
            Map<String, String> options = new HashMap<String, String> ();
            options.put("company_name", "Facebook");
            options.put("round", "a");
            System.out.print(FundingRaised.where(options).size());
        } catch(IOException e) {
            System.out.print(e.getMessage());
            System.out.print("error");
        }
    }
}

class NoSuchEntryException extends Exception {}
