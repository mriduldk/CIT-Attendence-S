package com.ajstudios.easyattendance.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.Reports_Detail_Activity;
import com.ajstudios.easyattendance.model.MaxStudent;
import com.ajstudios.easyattendance.realm.Attendance_Reports;
import com.ajstudios.easyattendance.viewholders.ViewHolder_reports;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

    private final Activity mActivity;
    List<MaxStudent> mList;
    private String subName, sem, dept;

    public ReportsAdapter(List<MaxStudent>list, Activity context, String subName, String sem, String dept) {
        mActivity = context;
        this.mList = list;
        this.subName = subName;
        this.sem = sem;
        this.dept = dept;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_adapter_item, parent, false);
        return new ReportViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {

        MaxStudent maxStudent = mList.get(position);

        try{

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfM = new SimpleDateFormat("MMM");
            SimpleDateFormat sdfD = new SimpleDateFormat("dd");

            Date dateM = sdf.parse(maxStudent.getDateTime());
            Date dateD = sdf.parse(maxStudent.getDateTime());

            holder.month.setText("" + sdfM.format(dateM));
            holder.date.setText("" + sdfD.format(dateD));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), Reports_Detail_Activity.class);
                    intent.putExtra("ID", maxStudent.getCourse_id());
                    intent.putExtra("date", maxStudent.getDateTime());
                    intent.putExtra("subject", subName);
                    intent.putExtra("class", sem);
                    intent.putExtra("dept", dept);
                    view.getContext().startActivity(intent);
                }
            });


        } catch (Exception ex) {

        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        public TextView month;
        public TextView date;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            month = itemView.findViewById(R.id.month_report_adapter);
            date = itemView.findViewById(R.id.date_report_adapter);
        }
    }
}
