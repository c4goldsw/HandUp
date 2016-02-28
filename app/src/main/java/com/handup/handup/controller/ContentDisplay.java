package com.handup.handup.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import com.handup.handup.R;
import com.handup.handup.controller.course.content.MyContentRecyclerViewAdapter;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;
import com.handup.handup.model.fbquery.ContentPullTask;
import com.handup.handup.view.RecyclerItemSpacing;

import java.util.ArrayList;

public class ContentDisplay extends AppCompatActivity implements ContentPullTask.ContentQueryImplementer {

    private RecyclerView mRecyclerView;
    private MyContentRecyclerViewAdapter mRecyclerViewAdapter;

    private String[] courses;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        uid = getIntent().getStringExtra(Constants.PUT_EXTRA_UID);
        courses = getIntent().getStringArrayExtra(Constants.PUT_EXTRA_COURSE_IDS);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        /*We need to determine the overall size of the screen.  We can then set the
            size and number of photo tiles accordingly*/
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int mColumnCount = /*(int) (dm.widthPixels / Constants.ContentListBaseSize)*/ 1;

        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            GridLayoutManager m = new GridLayoutManager(this, mColumnCount);
            mRecyclerView.setLayoutManager(m);
        }

        mRecyclerViewAdapter = new MyContentRecyclerViewAdapter(new ArrayList<Content>(),
                dm.widthPixels, mColumnCount, false);

        mRecyclerView.addItemDecoration(new RecyclerItemSpacing((int) (8 * dm.density), mColumnCount));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        for(String course : courses)
            new ContentPullTask(course, uid, this).execute();
    }

    public void updateUI(Content c){

        if(mRecyclerViewAdapter != null){

            mRecyclerViewAdapter.addItem(c);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onContentQueryFinish(Content c) {
        updateUI(c);
    }
}
