package com.walker.utils.excel.ali;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

/**
 * <p>
 *
 * </p>
 *
 * @author mu qin
 * @date 2020/6/29
 */
public class ExcelBean extends BaseRowModel {

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "编码")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
