package com.ajstudios.easyattendance.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.model.User;
import com.ajstudios.easyattendance.realm.Attendance_Students_List;
import com.ajstudios.easyattendance.viewholders.ViewHolder_reports_detail;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class Reports_Detail_Adapter extends RecyclerView.Adapter<Reports_Detail_Adapter.ReportViewHolder> {

    private final Activity mActivity;
    private final List<User> mList;

    public Reports_Detail_Adapter(List<User> list, Activity context) {
        mActivity = context;
        mList = list;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_detail_adapter_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

        User user = mList.get(position);

        holder.student_name_adapter.setText(user.getName());
        holder.student_regNo_adapter.setText("Roll No : " + user.getRoll_no());

        if (user.getAttendanceStatus() == null) {
            holder.status_report_detail_adapter.setText("A");
            holder.cardView_report_detail_adapter.setCardBackgroundColor(mActivity.getResources().getColor(R.color.red_new));
        } else {
            if (user.getAttendanceStatus().equals("Present")) {
                holder.status_report_detail_adapter.setText("P");
                holder.cardView_report_detail_adapter.setCardBackgroundColor(mActivity.getResources().getColor(R.color.green_new));
            } else {
                holder.status_report_detail_adapter.setText("A");
                holder.cardView_report_detail_adapter.setCardBackgroundColor(mActivity.getResources().getColor(R.color.red_new));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        public TextView student_name_adapter;
        public TextView student_regNo_adapter;
        public TextView status_report_detail_adapter;
        public CardView cardView_report_detail_adapter;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            student_name_adapter = itemView.findViewById(R.id.student_name_report_detail_adapter);
            student_regNo_adapter = itemView.findViewById(R.id.student_regNo_report_detail_adapter);
            status_report_detail_adapter = itemView.findViewById(R.id.status_report_detail_adapter);
            cardView_report_detail_adapter = itemView.findViewById(R.id.cardView_report_detail_adapter);
        }
    }

}
