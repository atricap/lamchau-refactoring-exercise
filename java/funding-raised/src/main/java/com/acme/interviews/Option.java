package com.acme.interviews;

public enum Option {
    PERMALINK("permalink", 0),
    COMPANY_NAME("company_name", 1),
    NUMBER_EMPLOYEES("number_employees", 2),
    CATEGORY("category", 3),
    CITY("city", 4),
    STATE("state", 5),
    FUNDED_DATE("funded_date", 6),
    RAISED_AMOUNT("raised_amount", 7),
    RAISED_CURRENCY("raised_currency", 8),
    ROUND("round", 9);

    private final String columnName;
    private final int columnIndex;

    Option(String columnName, int columnIndex) {
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
