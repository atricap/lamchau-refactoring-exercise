package com.acme.interviews;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toList;

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
        List<String[]> csvData = new ArrayList<String[]>();
        CSVReader reader = new CSVReader(new FileReader(fileName));
        String[] row = null;

        while((row = reader.readNext()) != null) {
            csvData.add(row);
        }

        reader.close();
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
                filterBy(options, option);
            }
        }

        return createRowMaps();
    }

    public void filterBy(Map<String, String> options, Option option) {
        this.csvData = filteredBy(options, option);
    }

    private List<String[]> filteredBy(Map<String, String> options, Option option) {
        List<String[]> results = new ArrayList<String[]>();

        for (int i = 0; i < csvData.size(); i++) {
            if (csvData.get(i)[option.getColumnIndex()].equals(options.get(option.getColumnName()))) {
                results.add(csvData.get(i));
            }
        }
        return results;
    }

    public List<Map<String, String>> createRowMaps() {
        List<Map<String, String>> output = new ArrayList<Map<String, String>>();

        for(int i = 0; i < csvData.size(); i++) {
            output.add(createSingleRowMap(csvData.get(i)));
        }
        return output;
    }

    private static Map<String, String> createSingleRowMap(String[] row) {
        Map<String, String> mapped = new HashMap<String, String>();
        addMappingsForRow(mapped, row);
        return mapped;
    }

    public Optional<Map<String, String>> findBy(Map<String, String> options) {
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

            return Optional.of(mapped);
        }
        return Optional.empty();
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

    static void addMappingsForRow(Map<String, String> mapped, String[] row) {
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
}
