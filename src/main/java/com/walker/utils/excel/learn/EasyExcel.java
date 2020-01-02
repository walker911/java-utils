package com.walker.utils.excel.learn;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class EasyExcel implements Closeable {

    private int startRow;

    private String sheetName;

    private String excelFilePath;

    private Workbook workbook;

    // 默认的格式化
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    private static final String DEFAULT_NUM_FORMAT = "#.##";

    private static final String DEFAULT_BOOL_FORMAT = "true:false";

    // 表达式处理
    private static final ExpressionParser elParser = new SpelExpressionParser();
    private static final TemplateParserContext elContext = new TemplateParserContext();

    private static final Logger logger = LoggerFactory.getLogger(EasyExcel.class);

    public EasyExcel(String excelFilePath) throws IOException, InvalidFormatException {
        this.startRow = 0;
        this.sheetName = "Sheet1";
        this.excelFilePath = excelFilePath;
        this.workbook = createWorkbook();
    }

    private Workbook createWorkbook() throws IOException, InvalidFormatException {
        File file = new File(this.excelFilePath);
        Workbook workbook;
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("文件创建失败");
            }
            workbook = new XSSFWorkbook();
        } else {
            workbook = WorkbookFactory.create(file);
        }
        return workbook;
    }

    public <T> boolean createExcel(List<T> list) {
        if (excelFilePath == null || "".equals(excelFilePath)) {
            throw new NullPointerException("excelFilePath is none");
        }
        if (list == null || list.isEmpty()) {
            return false;
        }

        // 获取bean的配置信息
        T bean = list.get(0);
        List<ExcelFieldInfo> infoList = getFieldInfo(bean.getClass());

        // sort
        Collections.sort(infoList);

        FileOutputStream fileOutputStream = null;
        try {
            Sheet sheet = workbook.createSheet(sheetName);
            // 标题行
            Row titleRow = sheet.createRow(0);
            int fieldSize = infoList.size();
            for (int i = 0; i < fieldSize; i++) {
                Cell cell = titleRow.createCell(i);
                ExcelFieldInfo fieldInfo = infoList.get(i);
                String cellName = fieldInfo.getCellName();
                if (StringUtils.isBlank(cellName)) {
                    cellName = fieldInfo.getField().getName();
                }
                cell.setCellValue(cellName);
            }
            // 数据行
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                T t = list.get(i);
                for (int j = 0; j < fieldSize; j++) {
                    Cell cell = row.createCell(j);
                    ExcelFieldInfo info = infoList.get(j);
                    String format = info.getFormat();
                    Field field = info.getField();
                    String el = info.getEl();
                    Object object;
                    // 若存在el表达式，计算
                    if (StringUtils.isNotBlank(el)) {
                        Expression expression = elParser.parseExpression(el,elContext);
                        object = expression.getValue(t, field.getType());
                    } else {
                        object = field.get(t);
                    }
                    CellStyle cellStyle;
                    switch (info.getType()) {
                        case TEXT:
                            cell.setCellType(CellType.STRING);
                            cell.setCellValue(object.toString());
                            break;
                        case DATE:
                            cell.setCellType(CellType.STRING);
                            cellStyle = workbook.createCellStyle();
                            format = StringUtils.isBlank(format) ? DEFAULT_DATE_FORMAT : format;
                            DataFormat dataFormat = workbook.createDataFormat();
                            cellStyle.setDataFormat(dataFormat.getFormat(format));
                            cell.setCellStyle(cellStyle);
                            cell.setCellValue((Date) object);
                            break;
                        case NUMBER:
                            cell.setCellType(CellType.NUMERIC);
                            cellStyle = workbook.createCellStyle();
                            format = StringUtils.isBlank(format) ? DEFAULT_NUM_FORMAT : format;
                            cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
                            cell.setCellStyle(cellStyle);
                            if (object instanceof Integer) {
                                cell.setCellValue((Integer) object);
                            } else if (object instanceof Short) {
                                cell.setCellValue((Short) object);
                            } else if (object instanceof Float) {
                                cell.setCellValue((Float) object);
                            } else if (object instanceof Byte) {
                                cell.setCellValue((Byte) object);
                            } else if (object instanceof Double) {
                                cell.setCellValue((Double) object);
                            } else {
                                cell.setCellValue((String) object);
                            }
                            break;
                        case BOOL:
                            cell.setCellType(CellType.STRING);
                            format = StringUtils.isBlank(format) ? DEFAULT_BOOL_FORMAT : format;
                            String[] boolValues = format.split(":|:");
                            cell.setCellValue(object == null ? boolValues[0] : (Boolean) object ? boolValues[0] : boolValues[1]);
                            break;
                    }
                }
            }
            File file = new File(excelFilePath);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("文件创建失败");
                }
            }
            fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error("反射异常", e);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("流异常", e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("其他异常", e);
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
        return true;
    }

    private List<ExcelFieldInfo> getFieldInfo(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ExcelFieldInfo> infoList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(ExcelField.class)) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                field.setAccessible(true);
                infoList.add(new ExcelFieldInfo(field, excelField, i));
            }
        }
        return infoList;
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }
}
