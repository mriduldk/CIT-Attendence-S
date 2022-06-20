package com.ajstudios.easyattendance.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseClasses {

    @SerializedName("ResponseCode")
    private String responseCode;

    @SerializedName("ResponseMsg")
    private String responseMsg;

    @SerializedName("Result")
    private String result;

    @SerializedName("Classes")
    private List<Classes> classesList;


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

    public List<Classes> getClassesList() {
        return classesList;
    }

    public void setClassesList(List<Classes> classesList) {
        this.classesList = classesList;
    }
}
