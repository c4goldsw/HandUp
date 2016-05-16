package com.handup.handup.controller.main;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.handup.handup.HandUp;
import com.handup.handup.R;
import com.handup.handup.controller.login.LoginActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Course;
import com.handup.handup.model.lsquery.LsQueryObject;
import com.handup.handup.model.InitialQueryTask;
import com.handup.handup.model.StateManager;
import com.handup.handup.model.User;
import com.handup.handup.model.lsquery.MeRequest;
import com.handup.handup.view.SectionsPagerAdapter;

import java.util.ArrayList;

/**
 * I followed
 * <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">
 * this tutorial</a> and used code from it in order to create the tab views.
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnProfileInteractionListener,
CourseFragment.OnFragmentInteractionListener,
        InitialQueryTask.InitialQueryUser, MenuFragment.OnFragmentInteractionListener {

    /*===========================================================================================
    Variables
    ===========================================================================================*/

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /**
     * The {@link android.support.design.widget.TabLayout} that will correspond
     * a particular fragment in the com.handup.handup.view pager.
     */
    private TabLayout tabLayout;

    /**
     * Used to keep track of the number of tabs
     */
    public static final int tabCount = 3;

    /**
     * An array of Drawables containing each icon used in the tabs
     */
    private Drawable [] icons;

    /**
     * Interface for making Learning Studio queries
     */
    private LsQueryObject q;

    /**
     * This is the POJO that isused to store the LS /meRequest query.
     */
    private static MeRequest meRequest = new MeRequest();

    /**
     * This is the POJO that is used to store all firebase user queries
     */
    private static User user = new User();

    private static Bitmap userContentPreview;

    private static ArrayList<Course> courses = new ArrayList<>();

    private ProfileFragment profileFragment;
    private CourseFragment courseFragment;

    /*===========================================================================================
    Model / Controller related methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //before loading the main activity, we determine what state the user was last in.
        //Once we're logged in, do initial setup
        InitialSetup();

        setupTabs();
    }

    /**
     * As this is the default launch activity, we have to check what the previous state of the
     * app was.  If the user wasn't logged in, they're sent to the login screen. Otherwise, we
     * load the app from here
     */
    private void InitialSetup(){

        if(StateManager.isLoggedIn(this)){

            //Set a weak reference to the main activity
            HandUp.setMainWR(this);

            new InitialQueryTask(StateManager.getUserName(this), this).execute();

        } else {

            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * This method is called when an LsQueryObject finishes from the async query task
     */
    @Override
    public void updateFragmentUIs() {

        /*TODO: I don't know, but this could end up being VERY dangerous ... consider removing*/
        if(profileFragment != null)
            profileFragment.updateUI();

        if(courseFragment != null)
            courseFragment.updateUI();
    }

    /**
     * Logs the user out
     */
    public void logout(){

        //log the user out
        StateManager.setLoggedIn(this, false);
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Context c = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int currentTab = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                icons[currentTab].setAlpha(77);
                icons[currentTab].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                icons[position].setAlpha(255);
                icons[position].setColorFilter(ContextCompat.getColor(c, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        /* Here, we start adding the nessescary components for the actual tabs*/

        tabLayout = (TabLayout) findViewById(R.id.tab_main);
        tabLayout.setupWithViewPager(mViewPager);

        icons = new Drawable[tabCount];

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_acount_box_24dp);
        icons[1].setAlpha(77);

        icons[2] = ContextCompat.getDrawable(this, R.drawable.ic_menu_24dp);
        icons[2].setAlpha(77);

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_drawer_button) {

            drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*===========================================================================================
    Fragment listener methods
    ===========================================================================================*/

    @Override
    public void onProfileSelect(ProfileFragment f) { profileFragment = f;}

    @Override
    public void onCourseSelect(Uri uri) {}

    @Override
    public void menuFragmentInteraction(int option) {

        if(option == Constants.MENUFRAGMENT_LOGOUT)
            logout();
    }

    @Override
    public void setCourseFragment(CourseFragment courseFragment) {
        this.courseFragment = courseFragment;
    }

    /*===========================================================================================
    Miscellaneous methods
    ===========================================================================================*/

    public static MeRequest getMeRequest(){return meRequest;}
    public static void setMeRequest(MeRequest m){meRequest = m;}

    public static User getUser(){return user;}
    public static void setUser(User u){user = u;};

    public static Bitmap getUserContentPreview(){return userContentPreview;}
    public static void setUserContentPrview(Bitmap u){userContentPreview = u; }

    public static ArrayList<Course> getCourses(){

        return courses;
    }
    public static void setCourses(ArrayList<Course> c) {

        courses = c;
    }
}