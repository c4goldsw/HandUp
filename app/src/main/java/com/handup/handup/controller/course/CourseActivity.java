package com.handup.handup.controller.course;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.firebase.client.Firebase;
import com.handup.handup.R;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ImageHandler;
import com.handup.handup.model.Content;
import com.handup.handup.model.ContentPullTask;
import com.handup.handup.model.CourseUsersQueryTask;
import com.handup.handup.model.ContentPushTask;
import com.handup.handup.model.GetLectureTimes;
import com.handup.handup.model.User;
import com.handup.handup.view.CourseSectionPagerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CourseActivity extends AppCompatActivity implements UserFragment.UserListFragmentInteractionListener,
        ContentFragment.OnContentFragmentInteractionListener, CourseUsersQueryTask.CourseUserQueryImplementer,
        GetLectureTimes.OnLectureTimeGetFinish, ContentPullTask.ContentQueryImplementer{

    /*===========================================================================================
    Variables
    ===========================================================================================*/

    private CourseSectionPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private FloatingActionButton mFab;

    private int courseID;
    private String username;
    private String uid;

    private TabLayout tabLayout;

    /**
     * Used to keep track of the number of tabs
     */
    public static final int tabCount = 2;

    private int currentTab = 0;

    /**
     * An array of Drawables containing each icon used in the tabs
     */
    private Drawable[] icons;

    private ArrayList<User> users = new ArrayList<>();

    private ArrayList<Date> lectureTimes;

    private int numberOfPeers;
    private int peerCounter = 0;

    private UserFragment userFragment;
    private ContentFragment contentFragment;

    private Date selectedLecture;

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

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentTab == 0)
                    ;
                else if(currentTab == 1)
                    submitContent(view);
            }
        });

        Firebase.setAndroidContext(this);

        new GetLectureTimes(Integer.toString(courseID), this).execute();
        new CourseUsersQueryTask(courseID, this, username, uid).execute();
        new ContentPullTask(Integer.toString(courseID), uid, this).execute();
    }

    //TODO: change currentTimeMillis to something system-time independent
    private void submitContent(View view){

        if(lectureTimes == null) {
            Snackbar.make(view, "Still loading", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();

            return;
        }

        //if we're in range of one of the submission dates, submit
        for(Date time: lectureTimes){
            //Day to millisecond conversion: http://stackoverflow.com/questions/6980376/convert-from-days-to-milliseconds
            if(time.getTime() < System.currentTimeMillis() && System.currentTimeMillis() < time.getTime()
                     + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)){

                selectedLecture = time;
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        , Constants.TAKE_PHOTO);
            }
        }

        Snackbar.make(view, "Content submission closed, try again on ...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    //taken from http://bit.ly/1UmSnOi , on SO
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.TAKE_PHOTO && resultCode == AppCompatActivity.RESULT_OK) {

            Calendar c = Calendar.getInstance();
            c.setTime(selectedLecture);

            try {

                Bitmap image = ImageHandler.getPortraitImage(
                        data.getData(), this, 250, 259);

                contentFragment.updateUI(new Content(image));

                String imageString = ImageHandler.getImageString(image);

                new ContentPushTask(imageString, uid, Integer.toString(courseID), this,
                        Integer.toString(c.get(Calendar.DAY_OF_YEAR))).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedStateInstance){

        savedStateInstance.putSerializable(Constants.DATE, selectedLecture);
        super.onSaveInstanceState(savedStateInstance);
    }

    @Override
    public void onRestoreInstanceState(Bundle b){

        selectedLecture = (Date) b.getSerializable(Constants.DATE);
    }

    /*===========================================================================================
    View (UI) methods
    ===========================================================================================*/

    /**
     * Sets up the tabs and their needed adapters for switching between activities
     */
    private void setupTabs(){

        /* Create the adapter that will return a fragment for each of the three
         primary sections of the activity. We're sending it the screen dimensions for the
         content section, which uses a grid list which needs to be size appropriately*/
        mSectionsPagerAdapter = new CourseSectionPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Context c = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private Drawable[] buttonIcons;
            //Initializer
            {
                buttonIcons = new Drawable[2];
                buttonIcons[0] = getResources().getDrawable(R.drawable.ic_alarm_24dp);
                buttonIcons[1] = getResources().getDrawable(R.drawable.ic_camera_alt_24dp);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                //change color of tab icons
                icons[currentTab].setAlpha(77);
                icons[currentTab].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                icons[position].setAlpha(255);
                icons[position].setColorFilter(ContextCompat.getColor(c, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                currentTab = position;

                //animate button: http://stackoverflow.com/questions/4689918/move-image-from-left-to-right-in-android
                TranslateAnimation anim = new TranslateAnimation(0f, 0, 0, 300);
                AccelerateDecelerateInterpolator adi = new AccelerateDecelerateInterpolator();
                anim.setInterpolator(adi);
                anim.setDuration(250);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(1);
                anim.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                        if(currentTab == 0)
                            mFab.setImageDrawable(buttonIcons[0]);
                        else if(currentTab == 1)
                            mFab.setImageDrawable(buttonIcons[1]);
                    }
                });

                mFab.startAnimation(anim);
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
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void setUserFragment(UserFragment userFragment) {
        this.userFragment = userFragment;
    }

    @Override
    public void setContentFragment(ContentFragment contentFragment){ this.contentFragment = contentFragment; }

    /*===========================================================================================
    Query interaction methods
    ===========================================================================================*/

    @Override
    public void onCourseUserQueryFinish(User u) {

        users.add(u);
        userFragment.updateUI(u);
    }

    @Override
    public void onLectureTimeGetFinish(ArrayList<Date> times) {
        lectureTimes = times;
    }

    @Override
    public void onContentQueryFinish(Content c) {

        contentFragment.updateUI(c);
    }
}
