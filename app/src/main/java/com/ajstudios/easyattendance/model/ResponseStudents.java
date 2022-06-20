package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseStudents {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMsg")
    private String responseMsg;

    @SerializedName("Result")
    private String result;

    @SerializedName("Students")
    private List<User> studentList;


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

    public List<User> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<User> studentList) {
        this.studentList = studentList;
    }
}
