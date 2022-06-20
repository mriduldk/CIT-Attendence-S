package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

public class Attendance {

    @SerializedName("attendance_id")
    private String attendance_id;

    @SerializedName("student_id")
    private String student_id;

    @SerializedName("subject_id")
    private String subject_id;

    @SerializedName("status")
    private String status;

    @SerializedName("dateTime")
    private String dateTime;

    @SerializedName("max_id")
    private String max_id;


    public String getAttendance_id() {
        return attendance_id;
    }

    public void setAttendance_id(String attendance_id) {
        this.attendance_id = attendance_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }
}
