package com.handup.handup.controller.course;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.handup.handup.R;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.user.UserDisplayContent;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.CourseUsersQueryTask;
import com.handup.handup.model.fbquery.User;
import com.handup.handup.view.CourseSectionPagerAdapter;

import java.util.ArrayList;

public class CourseActivity extends AppCompatActivity implements UserFragment.OnListFragmentInteractionListener,
        ContentFragment.OnListFragmentInteractionListener, CourseUsersQueryTask.CourseUserQueryImplementer {

    /*===========================================================================================
    Variables
    ===========================================================================================*/

    private CourseSectionPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private int courseID;
    private String username;

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

    private int numberOfPeers;
    private int peerCounter = 0;

    /*===========================================================================================
    Controller methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseID = getIntent().getIntExtra(Constants.PUT_EXTRA_COURSE_ID, 0);
        username = getIntent().getStringExtra(Constants.PUT_EXTRA_USERNAME);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Constants.PUT_EXTRA_COURSE_NAME));

        /*back button info here: http://stackoverflow.com/questions/14545139/android-back-button-in-the-title-bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        setupTabs();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
    public void onListFragmentInteraction(UserDisplayContent.UserDisplayItem item) {

    }

    @Override
    public void onListFragmentInteraction(com.handup.handup.controller.course.content.DummyContent.DummyItem item) {

    }

    @Override
    public void onCourseUserQueryFinish(User u) {

        users.add(u);
    }
}
