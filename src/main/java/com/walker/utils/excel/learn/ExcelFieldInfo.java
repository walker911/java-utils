package com.walker.utils.excel.learn;

import java.lang.reflect.Field;

public class ExcelFieldInfo implements Comparable<ExcelFieldInfo> {

    private Field field;

    private String cellName;

    private int order;

    private String format;

    private String el;

    private ExcelCellType type;

    public ExcelFieldInfo(Field field, ExcelField excelField, int order) {
        this.field = field;
        this.cellName = excelField.value();
        this.type = excelField.type();
        this.format = excelField.format();
        this.el = excelField.el();
        this.order = excelField.order() == 0 ? order : excelField.order();
    }

    public Field getField() {
        return field;
    }

    public String getCellName() {
        return cellName;
    }

    public int getOrder() {
        return order;
    }

    public String getFormat() {
        return format;
    }

    public String getEl() {
        return el;
    }

    public ExcelCellType getType() {
        return type;
    }

    @Override
    public int compareTo(ExcelFieldInfo o) {
        int x = this.getOrder();
        int y = o.getOrder();
        return (x<y) ? -1 : (x==y ? 0 : 1);
    }
}
