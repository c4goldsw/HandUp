package com.handup.handup.controller.course;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.handup.handup.R;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ImageHandler;
import com.handup.handup.model.CourseUsersQueryTask;
import com.handup.handup.model.FbContentPushTask;
import com.handup.handup.model.GetLectureTimes;
import com.handup.handup.model.fbquery.User;
import com.handup.handup.view.CourseSectionPagerAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CourseActivity extends AppCompatActivity implements UserFragment.OnListFragmentInteractionListener,
        ContentFragment.OnListFragmentInteractionListener, CourseUsersQueryTask.CourseUserQueryImplementer,
        GetLectureTimes.OnLectureTimeGetFinish{

    /*===========================================================================================
    Variables
    ===========================================================================================*/

    private CourseSectionPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private int courseID;
    private String username;
    private String uid;

    private TabLayout tabLayout;

    /**
     * Used to keep track of the number of tabs
     */
    public static final int tabCount = 2;

    /**
     * An array of Drawables containing each icon used in the tabs
     */
    private Drawable[] icons;

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<Date> submitTimes;

    private int numberOfPeers;
    private int peerCounter = 0;

    private UserFragment userFragment;

    /*===========================================================================================
    Model/ Controller methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseID = getIntent().getIntExtra(Constants.PUT_EXTRA_COURSE_ID, 0);
        username = getIntent().getStringExtra(Constants.PUT_EXTRA_USERNAME);
        uid      = getIntent().getStringExtra(Constants.PUT_EXTRA_UID);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.PUT_EXTRA_COURSE_NAME));

        setupTabs();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitContent(view);
            }
        });

        //new GetLectureTimes(Integer.toString(courseID), this).execute();
        new CourseUsersQueryTask(courseID, this, username, uid).execute();
    }

    private void submitContent(View view){

        if(submitTimes == null){
            Snackbar.make(view, "Still loading", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        //if we're in range of one of the submission dates, submit
        for(Date time: submitTimes){
            //Day to millisecond conversion: http://stackoverflow.com/questions/6980376/convert-from-days-to-milliseconds
            if(time.getTime() < System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
                    && System.currentTimeMillis() < time.getTime()){
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        Constants.SELECT_IMAGE);
            }
        }
    }


    //taken from http://bit.ly/1UmSnOi , on SO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SELECT_IMAGE)
            if (resultCode == AppCompatActivity.RESULT_OK) {

                new FbContentPushTask(data.getData(), uid, Integer.toString(courseID), this);
            }
    }

    /*===========================================================================================
    View (UI) methods
    ===========================================================================================*/

    /**
     * Sets up the tabs and their needed adapters for switching between activities
     */
    private void setupTabs(){

        /* Create the adapter that will return a fragment for each of the three
         primary sections of the activity. */
        mSectionsPagerAdapter = new CourseSectionPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Context c = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentTab = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                icons[currentTab].setAlpha(77);
                icons[currentTab].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                icons[position].setAlpha(255);
                icons[position].setColorFilter(ContextCompat.getColor(c, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /* Here, we start adding the nessescary components for the actual tabs*/

        tabLayout = (TabLayout) findViewById(R.id.tab_course);
        tabLayout.setupWithViewPager(mViewPager);

        icons = new Drawable[tabCount];

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        //Used to be in position two
        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_assignment_24dp);
        icons[1].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        icons[1].setAlpha(77);

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);

    }

    /*===========================================================================================
    Fragment related methods
    ===========================================================================================*/

    @Override
    public void onListFragmentInteraction(UserFragment userFragment) {
        this.userFragment = userFragment;
    }

    @Override
    public void onListFragmentInteraction(com.handup.handup.controller.course.content.DummyContent.DummyItem item) {

    }

    @Override
    public void onCourseUserQueryFinish(User u) {

        users.add(u);
        userFragment.updateUI(u);
    }

    @Override
    public void onLectureTimeGetFinish(ArrayList<Date> times) {
        submitTimes = times;
    }

}
