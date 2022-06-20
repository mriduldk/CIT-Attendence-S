package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

public class Classes {

    @SerializedName("subject_id")
    private String subject_id;

    @SerializedName("subject_name")
    private String subject_name;

    @SerializedName("subject_code")
    private String subject_code;

    @SerializedName("semester")
    private String semester;

    @SerializedName("dept")
    private String dept;

    @SerializedName("studentCount")
    private String studentCount;

    @SerializedName("theme")
    private String theme;


    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(String studentCount) {
        this.studentCount = studentCount;
    }
}
