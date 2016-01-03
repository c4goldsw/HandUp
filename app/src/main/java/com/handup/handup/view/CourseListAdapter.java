package com.handup.handup.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handup.handup.R;

/**
 * Created by Christopher on 1/3/2016.  Code taken from
 * http://developer.android.com/training/material/lists-cards.html
 */
public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.ViewHolder> {

    private String[] courseNames;

    public CourseListAdapter(String[] courseNames){

        this.courseNames = courseNames;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
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

        holder.mTextView.setText(courseNames[position]);
    }

    @Override
    public int getItemCount() {
        return courseNames.length;
    }
}
