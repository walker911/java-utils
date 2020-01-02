package com.walker.utils.excel.learn;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PersonTest {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Person person = new Person();
            person.setAge(20);
            person.setBirthday(new Date());
            person.setGender("M");
            person.setName("张三");
            person.setNickname("校长");
            personList.add(person);
        }
        try {
            EasyExcel easyExcel = new EasyExcel("./data.xlsx");
            easyExcel.createExcel(personList);
            easyExcel.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取类注解信息
     * @param clazz
     */
    public static List<ExcelFieldInfo> getFieldInfo(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<ExcelFieldInfo> infoList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.isAnnotationPresent(ExcelField.class)) {
                ExcelField excelField = field.getAnnotation(ExcelField.class);
                field.setAccessible(true);
                ExcelFieldInfo fieldInfo = new ExcelFieldInfo(field, excelField, i);
                infoList.add(fieldInfo);
            }
        }
        return infoList;
    }
}
