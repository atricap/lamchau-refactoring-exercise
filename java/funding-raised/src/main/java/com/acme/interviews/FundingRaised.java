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
        CsvData csvObj = new CsvData(fileName);

        Optional<Map<String, String>> mapped = csvObj.findBy(options);

        return mapped.orElseThrow(NoSuchEntryException::new);
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
