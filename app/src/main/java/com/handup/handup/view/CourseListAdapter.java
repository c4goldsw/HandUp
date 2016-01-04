package com.handup.handup.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.controller.main.CourseFragment;
import com.handup.handup.controller.main.MainActivity;
import com.handup.handup.model.lsquery.Course;

import java.util.ArrayList;

/**
 * Created by Christopher on 1/3/2016.  Code taken from
 * http://developer.android.com/training/material/lists-cards.html
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private ArrayList<Course> courseNames = new ArrayList<>();
    static CourseFragment courseFragment;

    public void setCourseNames(ArrayList<Course> courseNames){

        if(courseNames == null){
            return;
        }

        this.courseNames = courseNames;
    }

    public CourseListAdapter(ArrayList<Course> courseNames, CourseFragment f){

        courseFragment = f;
        this.courseNames = courseNames;
    }

    //refer to here for touching handling https://gist.github.com/grantland/cd70814fe4ac369e3e92
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTextView;
        public int courseId;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView = (TextView) itemView;
        }

        @Override
        public void onClick(View v) {
            Log.d("List","being clicked");
            courseFragment.startCourseActivity(courseId, mTextView.getText().toString());
        }
    }

    @Override
    public CourseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list_item, parent, false);



        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CourseListAdapter.ViewHolder holder, int position) {

        holder.mTextView.setText(courseNames.get(position).getName());
        holder.courseId = MainActivity.getCourses().get(position).getId();
    }

    @Override
    public int getItemCount() {
        return courseNames.size();
    }
}
