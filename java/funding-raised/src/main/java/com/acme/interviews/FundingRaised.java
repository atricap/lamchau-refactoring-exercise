package com.acme.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class FundingRaised {
    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        List<String[]> csvData = createCsvRows();

        final List<Option> optionsToCheck = List.of(
                Option.COMPANY_NAME,
                Option.CITY,
                Option.STATE,
                Option.ROUND)
                .stream()
                .filter(o -> options.containsKey(o.getColumnName()))
                .collect(toList());

        for (Option option : optionsToCheck) {
            if(options.containsKey(option.getColumnName())) {
                csvData = filterBy(csvData, options, option.getColumnName(), option.getColumnIndex());
            }
        }

        return createRowMaps(csvData);
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
        List<String[]> csvData = readFromCsvFile();
        if (hasHeaderRow(csvData)) {
            removeHeaderRow(csvData);
        }
        Map<String, String> mapped = new HashMap<String, String> ();

        final List<Option> optionsToCheck = List.of(
                Option.COMPANY_NAME,
                Option.CITY,
                Option.STATE,
                Option.ROUND);

        outer:
        for(int i = 0; i < csvData.size(); i++) {
            for (Option option : optionsToCheck) {
                if (processFindByOption(options, option, csvData, i, mapped)) continue outer;
            }

            return mapped;
        }

        throw new NoSuchEntryException();
    }

    private static boolean processFindByOption(Map<String, String> options, Option companyName, List<String[]> csvData, int i, Map<String, String> mapped) {
        if (options.containsKey(companyName.getColumnName())) {
            if (!csvData.get(i)[companyName.getColumnIndex()].equals(options.get(companyName.getColumnName()))) {
                return true;
            }
            addMappingsForRow(mapped, csvData.get(i));
        }
        return false;
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
