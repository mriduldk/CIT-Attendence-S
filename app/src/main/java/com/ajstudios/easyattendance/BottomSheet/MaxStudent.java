package com.ajstudios.easyattendance.BottomSheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.ajstudios.easyattendance.Adapter.StudentsListAdapter;
import com.ajstudios.easyattendance.ClassDetail_Activity;
import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.model.ResponseMessge;
import com.ajstudios.easyattendance.model.ResponseStudents;
import com.ajstudios.easyattendance.retrofit.APIClient;
import com.ajstudios.easyattendance.retrofit.GetResult;
import com.ajstudios.easyattendance.utils.CustPrograssbar;
import com.ajstudios.easyattendance.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MaxStudent extends BottomSheetDialogFragment implements GetResult.MyListener {

    public String subId, teacherId, maxValue;
    public EditText editTextMaxStudent;
    public TextView textViewBtnSubmit;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    public MaxStudent(String subId, String teacherId, String maxValue) {
        this.subId = subId;
        this.teacherId = teacherId;
        this.maxValue = maxValue;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.bottomsheet_max_student, container, false);

        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(getActivity());
        editTextMaxStudent = v.findViewById(R.id.editTextMaxStudent);
        textViewBtnSubmit = v.findViewById(R.id.textViewBtnSubmit);

        textViewBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (editTextMaxStudent.getText().toString().trim().isEmpty()) {

                    Toast.makeText(getActivity(), "Student number is empty." , Toast.LENGTH_LONG).show();
                } else {
                    addMaxStudent(editTextMaxStudent.getText().toString().trim());
                }
            }
        });

        if (!maxValue.trim().isEmpty()) {
            editTextMaxStudent.setText(maxValue);
        }

        return v;
    }

    private void addMaxStudent(String value) {

        custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tid", teacherId);
            jsonObject.put("subId", subId);
            jsonObject.put("value", value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().addMaxStudent(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }



    @Override
    public void callback(JsonObject result, String callNo) {

        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {

                Gson gson = new Gson();
                ResponseMessge responseMessge = gson.fromJson(result.toString(), ResponseMessge.class);
                if (responseMessge.getResult().equals("true")) {

                    Toast.makeText(getActivity(), "" + responseMessge.getResponseMsg(), Toast.LENGTH_LONG).show();
                    dismiss();

                } else {
                    Toast.makeText(getActivity(), "" + responseMessge.getResponseMsg(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("Errror", "==>" + e.toString());
        }
    }

}
