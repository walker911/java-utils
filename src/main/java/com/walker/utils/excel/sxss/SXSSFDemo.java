package com.walker.utils.excel.sxss;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.Assert;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * SXSSFWorkbook Demo
 */
public class SXSSFDemo {
    public static void main(String[] args) throws IOException {
        sxxsf();
        sxxsf2();
    }

    public static void sxxsf2() throws IOException {
        // turn off auto-flushing and accumulate all rows in memory
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        Sheet sheet = workbook.createSheet();
        for (int rownum = 0; rownum < 1000; rownum++) {
            Row row = sheet.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }
            // manually control how rows are flushed to disk
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sheet).flushRows(100);
            }
        }
        FileOutputStream outputStream = new FileOutputStream("./sxxsf.xlsx");
        workbook.write(outputStream);
        outputStream.close();
        workbook.dispose();
    }

    /**
     * 100
     * @throws IOException
     */
    public static void sxxsf() throws IOException {
        // keep 100 rows in memory, exceeding rows will be flushed to disk
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet();
        for (int rownum = 0; rownum < 1000; rownum++) {
            Row row = sheet.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }
        }

        // Rows with rownum < 900 are flushed and not accessible
        for (int rownum = 0; rownum < 900; rownum++) {
            Assert.assertNull(sheet.getRow(rownum));
        }

        // the last 100 rows are still in memory
        for (int rownum = 900; rownum < 1000; rownum++) {
            Assert.assertNotNull(sheet.getRow(rownum));
        }

        FileOutputStream outputStream = new FileOutputStream("./sxxsf.xlsx");
        workbook.write(outputStream);
        outputStream.close();

        // dispose of temporary files backing this workbook on disk
        workbook.dispose();
    }
}
