package com.ajstudios.easyattendance.Adapter;

import static androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.ajstudios.easyattendance.AttendanceActivity;
import com.ajstudios.easyattendance.ClassDetail_Activity;
import com.ajstudios.easyattendance.R;
import com.ajstudios.easyattendance.model.Classes;

import java.util.List;

public class ClassListAdapterStudent extends RecyclerView.Adapter<ClassListAdapterStudent.ClassesViewHolder> {

    private final Activity mActivity;
    private final List<Classes> mList;

    public ClassListAdapterStudent(List<Classes> list, Activity context) {
        this.mActivity = context;
        this.mList = list;

    }

    @NonNull
    @Override
    public ClassesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_adapter, parent, false);
        return new ClassesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassListAdapterStudent.ClassesViewHolder holder, int position) {

        Classes classes = mList.get(position);

        holder.totalStudents_adapter.setText("Students : " + classes.getStudentCount());
        holder.className_adapter.setText(classes.getSemester() + " ( " + classes.getDept() + " )");
        holder.subjectName_adapter.setText(classes.getSubject_name());

        switch (classes.getTheme()) {
            case "0":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_paleblue);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_1);
                break;
            case "1":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_green);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_2);
                break;
            case "2":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_yellow);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_3);
                break;
            case "3":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_palegreen);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_4);
                break;
            case "4":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_paleorange);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_5);
                break;
            case "5":
                holder.imageClass_adapter.setImageResource(R.drawable.asset_bg_white);
                holder.frame_bg.setBackgroundResource(R.drawable.gradient_color_6);
                holder.subjectName_adapter.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.className_adapter.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                holder.totalStudents_adapter.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.text_color_secondary));
                break;
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, AttendanceActivity.class);
                intent.putExtra("theme", classes.getTheme());
                intent.putExtra("className", classes.getSemester());
                intent.putExtra("subjectName", classes.getSubject_name());
                intent.putExtra("classroom_ID", classes.getSubject_id());
                intent.putExtra("totalStudent", classes.getStudentCount());
                intent.putExtra("dept", classes.getDept());
                Pair<View, String> p1 = Pair.create((View) holder.cardView, "ExampleTransition");
                ActivityOptionsCompat optionsCompat = makeSceneTransitionAnimation(mActivity, p1);
                view.getContext().startActivity(intent, optionsCompat.toBundle());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ClassesViewHolder extends RecyclerView.ViewHolder {

        public TextView subjectName_adapter;
        public TextView className_adapter;
        public TextView totalStudents_adapter;
        public ImageView imageClass_adapter;
        public RelativeLayout frame_bg;
        public CardView cardView;

        public ClassesViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName_adapter = itemView.findViewById(R.id.subjectName_adapter);
            className_adapter = itemView.findViewById(R.id.className_adapter);
            totalStudents_adapter = itemView.findViewById(R.id.totalStudents_adapter);
            imageClass_adapter = itemView.findViewById(R.id.imageClass_adapter);
            frame_bg = itemView.findViewById(R.id.frame_bg);
            cardView = itemView.findViewById(R.id.cardView_adapter);
        }
    }


}
