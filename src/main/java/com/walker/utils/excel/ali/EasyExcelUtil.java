package com.walker.utils.excel.ali;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mu qin
 * @date 2020/6/29
 */
public class EasyExcelUtil {

    public static void main(String[] args) {
        try {
            create();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void create() throws FileNotFoundException {
        String filePath = "/home/cproject/service/cproject-service-activity/excel/20200629-测试预警领券异常.xlsx";
        ExcelWriter writer = null;
        List<ExcelBean> excelBeans = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdir();
            }
            FileOutputStream out = new FileOutputStream(file);
            writer = new ExcelWriter(out, ExcelTypeEnum.XLSX);
            Sheet sheet = new Sheet(1, 0, ExcelBean.class, "Sheet1", null);
            writer.write(excelBeans, sheet);
        } finally {
            if (writer != null) {
                writer.finish();
            }
        }
    }
}
