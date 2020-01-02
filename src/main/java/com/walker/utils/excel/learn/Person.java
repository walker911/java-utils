package com.walker.utils.excel.learn;

import java.util.Date;

public class Person {

    @ExcelField(value = "名字")
    private String name;

    @ExcelField(value = "年龄", type = ExcelCellType.NUMBER)
    private Integer age;

    @ExcelField(value = "性别")
    private String gender;

    @ExcelField(value = "生日", type = ExcelCellType.DATE)
    private Date birthday;

    @ExcelField(value = "昵称")
    private String nickname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
