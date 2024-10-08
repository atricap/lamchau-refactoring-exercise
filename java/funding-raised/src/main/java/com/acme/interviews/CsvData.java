package com.acme.interviews;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CsvData {
    private List<String[]> csvData;

    public CsvData(String fileName) throws IOException{
        this.csvData = createCsvRows(fileName);
    }

    private List<String[]> createCsvRows(String fileName) throws IOException {
        List<String[]> csvData = readFromCsvFile(fileName);
        if (hasHeaderRow(csvData)) {
            removeHeaderRow(csvData);
        }
        return csvData;
    }

    static List<String[]> readFromCsvFile(String fileName) throws IOException {
        List<String[]> csvData;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            csvData = csvReader.readAll();
        }
        return csvData;
    }

    static boolean hasHeaderRow(List<String[]> csvData) {
        // assume header to always exist
        return true;
    }

    static void removeHeaderRow(List<String[]> csvData) {
        csvData.remove(0);
    }

    public List<Map<String, String>> where(Map<String, String> options) {
        final List<Option> optionsToCheck = Stream.of(
                        Option.COMPANY_NAME,
                        Option.CITY,
                        Option.STATE,
                        Option.ROUND)
                .filter(o -> options.containsKey(o.getColumnName()))
                .collect(toList());

        for (Option option : optionsToCheck) {
            if(options.containsKey(option.getColumnName())) {
                filterBy(options, option);
            }
        }

        return createRowMaps();
    }

    public void filterBy(Map<String, String> options, Option option) {
        this.csvData = filteredBy(options, option);
    }

    private List<String[]> filteredBy(Map<String, String> options, Option option) {
        return csvData.stream()
                .filter(row -> row[option.getColumnIndex()].equals(options.get(option.getColumnName())))
                .collect(toList());
    }

    private List<Map<String, String>> createRowMaps() {
        return csvData.stream()
                .map(CsvData::getRowAsMap)
                .collect(toList());
    }

    public Optional<Map<String, String>> findBy(Map<String, String> options) {
        final List<Option> optionsToCheck = List.of(
                Option.COMPANY_NAME,
                Option.CITY,
                Option.STATE,
                Option.ROUND);

        Map<String, String> mapped = new HashMap<>();
        outer:
        for (String[] row : csvData) {
            for (Option option : optionsToCheck) {
                if (processFindByOption(mapped, options, option, row)) continue outer;
            }

            return Optional.of(mapped);
        }
        return Optional.empty();
    }

    private static boolean processFindByOption(Map<String, String> mapped, Map<String, String> options, Option option, String[] row) {
        if (options.containsKey(option.getColumnName())) {
            if (!row[option.getColumnIndex()].equals(options.get(option.getColumnName()))) {
                return true;
            }
            mapped.putAll(getRowAsMap(row));
        }
        return false;
    }

    private static Map<String, String> getRowAsMap(String[] row) {
        final List<Option> allOptions = List.of(
                Option.PERMALINK,
                Option.COMPANY_NAME,
                Option.NUMBER_EMPLOYEES,
                Option.CATEGORY,
                Option.CITY,
                Option.STATE,
                Option.FUNDED_DATE,
                Option.RAISED_AMOUNT,
                Option.RAISED_CURRENCY,
                Option.ROUND);

        return allOptions.stream()
                .collect(toMap(
                        Option::getColumnName,
                        option -> row[option.getColumnIndex()]));
    }
}
