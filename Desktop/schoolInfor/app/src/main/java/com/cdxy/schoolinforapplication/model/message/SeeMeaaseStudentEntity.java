package com.cdxy.schoolinforapplication.model.message;

/**
 * Created by huihui on 2016/12/27.
 */

public class SeeMeaaseStudentEntity {
    private String name;
    private String id;
    private String department;
    private String clazz;
    private String accept_time;

    public SeeMeaaseStudentEntity(String name, String id, String department, String clazz, String accept_time) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.clazz = clazz;
        this.accept_time = accept_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getAccept_time() {
        return accept_time;
    }

    public void setAccept_time(String accept_time) {
        this.accept_time = accept_time;
    }

    @Override
    public String toString() {
        return "SeeMeaaseStudentEntity{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", department='" + department + '\'' +
                ", clazz='" + clazz + '\'' +
                ", accept_time='" + accept_time + '\'' +
                '}';
    }


}
