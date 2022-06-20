package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMaxStudent {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMsg")
    private String responseMsg;

    @SerializedName("Result")
    private String result;

    @SerializedName("MaxStudent")
    private MaxStudent maxStudent;

    @SerializedName("DailyReport")
    private List<MaxStudent> maxStudents;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public MaxStudent getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(MaxStudent maxStudent) {
        this.maxStudent = maxStudent;
    }

    public List<MaxStudent> getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(List<MaxStudent> maxStudents) {
        this.maxStudents = maxStudents;
    }
}
