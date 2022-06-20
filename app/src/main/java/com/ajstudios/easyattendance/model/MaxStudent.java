package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

public class MaxStudent {

    @SerializedName("max_id")
    private String max_id;

    @SerializedName("teacher_id")
    private String teacher_id;

    @SerializedName("course_id")
    private String course_id;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("value")
    private String value;

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
