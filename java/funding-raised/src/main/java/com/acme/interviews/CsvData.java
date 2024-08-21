package com.acme.interviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvData {
    private List<String[]> csvData;

    public CsvData(List<String[]> csvData) {
        this.csvData = csvData;
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
