package com.ajstudios.easyattendance.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.model.Classes;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Students_List;

import java.util.List;

public class StudentsListAdapter extends RecyclerView.Adapter<StudentsListAdapter.StudentsViewHolder> {

    private final Activity mActivity;
    private final List<User> mList;
    String stuID;

    public StudentsListAdapter(List<User> list, Activity context) {
        this.mActivity = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public StudentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_adapter, parent, false);
        return new StudentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsViewHolder holder, int position) {

        User user = mList.get(position);

        //Students_List temp = getItem(position);
        holder.student_name_adapter.setText(user.getName());
        holder.student_regNo_adapter.setText("Roll No : " + user.getRoll_no());


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        stuID = user.getAttendanceStatus();
        //String value = preferences.getString(stuID, null);

        if (stuID == null) {
            holder.student_status.setText("Absent");
        } else {
            if (stuID.equals("Present")) {
                holder.student_status.setText("Present");
            } else {
                holder.student_status.setText("Absent");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class StudentsViewHolder extends RecyclerView.ViewHolder {

        public TextView student_name_adapter;
        public TextView student_regNo_adapter;
        public TextView student_status;

        public StudentsViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name_adapter = itemView.findViewById(R.id.student_name_adapter);
            student_regNo_adapter = itemView.findViewById(R.id.student_regNo_adapter);
            student_status = itemView.findViewById(R.id.student_status);
        }
    }


}
