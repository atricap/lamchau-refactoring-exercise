package com.acme.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

public class FundingRaised {
    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = createCsvRows();

        if(options.containsKey(Option.COMPANY_NAME.getColumnName())) {
            csvData = filterBy(csvData, options, Option.COMPANY_NAME.getColumnName(), Option.COMPANY_NAME.getColumnIndex());
        }

        if(options.containsKey(Option.CITY.getColumnName())) {
            csvData = filterBy(csvData, options, Option.CITY.getColumnName(), Option.CITY.getColumnIndex());
        }

        if(options.containsKey(Option.STATE.getColumnName())) {
            csvData = filterBy(csvData, options, Option.STATE.getColumnName(), Option.STATE.getColumnIndex());
        }

        if(options.containsKey(Option.ROUND.getColumnName())) {
            csvData = filterBy(csvData, options, Option.ROUND.getColumnName(), Option.ROUND.getColumnIndex());
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
            if(options.containsKey(Option.COMPANY_NAME.getColumnName())) {
                if (!csvData.get(i)[Option.COMPANY_NAME.getColumnIndex()].equals(options.get(Option.COMPANY_NAME.getColumnName()))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey(Option.CITY.getColumnName())) {
                if (!csvData.get(i)[Option.CITY.getColumnIndex()].equals(options.get(Option.CITY.getColumnName()))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey(Option.STATE.getColumnName())) {
                if (!csvData.get(i)[Option.STATE.getColumnIndex()].equals(options.get(Option.STATE.getColumnName()))) {
                    continue;
                }
                addMappingsForRow(mapped, csvData.get(i));
            }

            if(options.containsKey(Option.ROUND.getColumnName())) {
                if (!csvData.get(i)[Option.ROUND.getColumnIndex()].equals(options.get(Option.ROUND.getColumnName()))) {
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
        mapped.put(Option.PERMALINK.getColumnName(), row[Option.PERMALINK.getColumnIndex()]);
        mapped.put(Option.COMPANY_NAME.getColumnName(), row[Option.COMPANY_NAME.getColumnIndex()]);
        mapped.put(Option.NUMBER_EMPLOYEES.getColumnName(), row[Option.NUMBER_EMPLOYEES.getColumnIndex()]);
        mapped.put(Option.CATEGORY.getColumnName(), row[Option.CATEGORY.getColumnIndex()]);
        mapped.put(Option.CITY.getColumnName(), row[Option.CITY.getColumnIndex()]);
        mapped.put(Option.STATE.getColumnName(), row[Option.STATE.getColumnIndex()]);
        mapped.put(Option.FUNDED_DATE.getColumnName(), row[Option.FUNDED_DATE.getColumnIndex()]);
        mapped.put(Option.RAISED_AMOUNT.getColumnName(), row[Option.RAISED_AMOUNT.getColumnIndex()]);
        mapped.put(Option.RAISED_CURRENCY.getColumnName(), row[Option.RAISED_CURRENCY.getColumnIndex()]);
        mapped.put(Option.ROUND.getColumnName(), row[Option.ROUND.getColumnIndex()]);
    }

    public static void main(String[] args) {
        try {
            Map<String, String> options = new HashMap<String, String> ();
            options.put(Option.COMPANY_NAME.getColumnName(), "Facebook");
            options.put(Option.ROUND.getColumnName(), "a");
            System.out.print(FundingRaised.where(options).size());
        } catch(IOException e) {
            System.out.print(e.getMessage());
            System.out.print("error");
        }
    }
}
