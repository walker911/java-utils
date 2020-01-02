package com.walker.utils.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author walker
 * @date 2019/10/22
 */
public class UserModel {
    public static void main(String[] args) {
        try {
            Thread.sleep(5000);
            System.out.println("start read");
            File file = new File("C:\\Users\\ThinkPad\\Desktop\\2019热点下单城市.xlsx");
            InputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    System.out.println("row:" + row.getRowNum() + ", cell:" + cell.toString());
                }
            }
            Thread.sleep(1000);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
