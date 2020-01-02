package com.walker.utils.excel.learn;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelField {

    /**
     * excel中某列的名字
     * @return
     */
    String value() default "";

    /**
     * 数据类型
     * @return
     */
    ExcelCellType type() default ExcelCellType.TEXT;

    /**
     * 格式
     * @return
     */
    String format() default "";

    /**
     * 表达式
     * @return
     */
    String el() default "";

    /**
     * 顺序
     * @return
     */
    int order() default 0;

}
