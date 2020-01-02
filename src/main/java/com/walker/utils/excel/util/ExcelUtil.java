package com.walker.utils.excel.util;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * excel 工具类
 * Map: 1. key 字段名 2. 类型模糊
 * @author muqin
 * @create 2018-01-02 13:42
 */
public class ExcelUtil {

    public static final String FONT_NAME = "微软雅黑";

    /**
     * 设置标题单元格样式
     * @param workbook
     * @return
     */
    public static XSSFCellStyle createHeaderCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints((short) 12);
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        cellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 设置正文单元格样式
     * @param workbook
     * @return
     */
    public static XSSFCellStyle createBodyCellStyle(XSSFWorkbook workbook) {
        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints((short) 12);
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    /**
     * 写入标题行
     *
     * @param sheet
     */
    public static void writeHeaderContent(XSSFSheet sheet, XSSFCellStyle cellStyle, List<Map> mapList) {
        if (mapList != null && mapList.size() > 0) {
            Map data = mapList.get(0);
            Set<String> keySet = data.keySet();
            Iterator<String> iterator = keySet.iterator();
            int cell = 0;
            XSSFRow row = sheet.createRow(0);
            while (iterator.hasNext()) {
                String key = iterator.next();
                XSSFCell sxssfCell = row.createCell(cell);
                sxssfCell.setCellValue(key);
                sxssfCell.setCellStyle(cellStyle);
                sheet.setColumnWidth(cell, key.length() * 1024);
                cell++;
            }
        }
    }

    /**
     * 写入body
     *  @param mapList
     * @param sheet*/
    public static void writeBodyContent(List<Map> mapList, XSSFSheet sheet, XSSFCellStyle cellStyle) {
        for (int i = 0; i < mapList.size(); i++) {
            XSSFRow row = sheet.createRow(i + 1);

            Map data = mapList.get(i);
            Set<String> keyset = data.keySet();
            Iterator<String> iterator = keyset.iterator();
            int cell = 0;
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = data.get(key);
                XSSFCell sxssfCell = row.createCell(cell);
                sxssfCell.setCellValue(value == null ? "" : value.toString());
                sxssfCell.setCellStyle(cellStyle);
                sheet.autoSizeColumn(cell);
                cell++;
            }
            // map数据清理
            data.clear();
        }
        // 数据清理
        mapList.clear();
        // 设置空引用
        mapList = null;
    }

}
