package com.ajstudios.easyattendance.retrofit;


import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    @POST("register.php")
    Call<JsonObject> register(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "login.php")
    Call<JsonObject> loginUser(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "teacherClasses.php")
    Call<JsonObject> getClasses(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "classStudents.php")
    Call<JsonObject> getClassStudents(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "addMaxStudent.php")
    Call<JsonObject> addMaxStudent(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "getMaxStudent.php")
    Call<JsonObject> getMaxStudent(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "studentIndividualClass.php")
    Call<JsonObject> studentIndividualClass(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "getStudentAttendance.php")
    Call<JsonObject> getStudentAttendance(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "addAttendance.php")
    Call<JsonObject> addAttendance(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "getDailyReport.php")
    Call<JsonObject> getDailyReport(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "dailySubReport.php")
    Call<JsonObject> dailySubReport(@Body RequestBody requestBody);

    @POST(APIClient.APPEND_URL + "createClass.php")
    Call<JsonObject> createClass(@Body RequestBody requestBody);



}
