package com.cdxy.schoolinforapplication.model;

/**
 * Created by huihui on 2016/12/27.
 */

public class NotSeeMessageStudentEntity {
    //姓名、学号、系、班级、电话号码
    private String name;
    private String id;
    private String department;
    private String clazz;
    private String phoneNumber;

    public NotSeeMessageStudentEntity(String name, String id, String department, String clazz, String phoneNumber) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.clazz = clazz;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NotSeeMessageStudentEntity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", department='" + department + '\'' +
                ", clazz='" + clazz + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
