package com.acme.interviews;

import java.util.*;
import com.opencsv.CSVReader;
import java.io.FileReader;
import java.io.IOException;

import static java.util.stream.Collectors.toList;

public class FundingRaised {
    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        CsvData csvObj = new CsvData(createCsvRows());

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
                csvObj.filterBy(options, option);
            }
        }

        return csvObj.createRowMaps();
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
            CsvData.addMappingsForRow(mapped, csvData.get(i));
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
