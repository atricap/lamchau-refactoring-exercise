package com.acme.interviews;

import java.util.*;
import java.io.IOException;

public class FundingRaised {

    private static final String csvFileName = "startup_funding.csv";

    public static List<Map<String, String>> where(Map<String, String> options) throws IOException {
        return where(options, csvFileName);
    }

    public static List<Map<String, String>> where(Map<String, String> options, String fileName) throws IOException {
        CsvData csvObj = new CsvData(fileName);

        return csvObj.where(options);
    }

    public static Map<String, String> findBy(Map<String, String> options) throws IOException, NoSuchEntryException {
        return findBy(options, csvFileName);
    }

    public static Map<String, String> findBy(Map<String, String> options, String fileName) throws IOException, NoSuchEntryException {
        List<String[]> csvData = CsvData.readFromCsvFile(fileName);
        if (CsvData.hasHeaderRow(csvData)) {
            CsvData.removeHeaderRow(csvData);
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
