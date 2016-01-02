package com.handup.handup.controller;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handup.handup.R;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.JsonConverter;
import com.handup.handup.model.LsQueryObject;
import com.handup.handup.model.LsQueryTask;
import com.handup.handup.model.StateManager;
import com.handup.handup.model.User;
import com.handup.handup.view.SectionsPagerAdapter;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.core.Response;

import java.io.IOException;
import java.util.HashMap;

/**
 * I followed
 * <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">
 * this tutorial</a> and used code from it in order to create the tab views.
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnProfileInteractionListener,
CourseFragment.OnFragmentInteractionListener, GroupFragment.OnFragmentInteractionListener,
        LsQueryTask.LsQueryUser{

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
     * Toolbar for the app
     */
    private Toolbar  toolbar;

    /**
     * Layout for the right nav drawer
     */
    private DrawerLayout drawerLayout;

    /**
     * List contained by the drawer
     */
    private ListView drawerList;

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
     * This is the POJO that isused to store the LS /me query.
     */
    private static JsonConverter.Me me = new JsonConverter.Me();

    /**
     * This is the POJO that is used to store all firebase user queries
     */
    private static User user;

    public ProfileFragment profileFragment;

    /*===========================================================================================
    Model / Controller related methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        //before loading the main activity, we determine what state the user was last in
        determineState();

        //enable and set up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        setupTabs();
        setupNavDrawer();
    }

    /**
     * As this is the default launch activity, we have to check what the previous state of the
     * app was.  If the user wasn't logged in, they're sent to the login screen. Otherwise, we
     * load the app from here
     */
    private void determineState(){

        if(StateManager.isLoggedIn(this)){

            //start account setup
            new LsQueryTask(StateManager.getUserName(this), this,
                    Constants.PROFILE_REQUEST).execute();

        } else {

            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    /**
     * This method is called when an LsQueryObject finishes from the async query task
     * @param map Contains the data that will be loaded in the activity
     */
    @Override
    public void onLsQueryFinish(HashMap<String, Object> map) {

        final Gson g  = new Gson();

        try {
            me = g.fromJson((String) map.get("/me"), JsonConverter.Me.class);

            final Firebase ref = new Firebase("https://intense-inferno-38.firebaseio.com/users/" +
                    me.getMe().getId());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    user = dataSnapshot.getValue(User.class);

                    if (user == null) {

                        user = new User();
                        user.setUid(me.getMe().getId());
                        ref.setValue(user);
                    }

                    if(profileFragment!= null)
                        profileFragment.updateInterface();
                    else
                        Log.d("ProfileFragment","Profile fragment is null!");
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {Log.d("FireBase","Error:"
                + firebaseError);}
            });

        } catch (JsonSyntaxException b) {
            Log.d("Async", "Error: " + b);
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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        icons = new Drawable[tabCount];

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_people_24dp);
        icons[1].setAlpha(77);

        icons[2] = ContextCompat.getDrawable(this, R.drawable.ic_acount_box_24dp);
        icons[2].setAlpha(77);

        /*icons[3] = ContextCompat.getDrawable(this, R.drawable.ic_search_24dp);
        icons[3].setAlpha(77);

        icons[4] = ContextCompat.getDrawable(this, R.drawable.ic_menu_24dp);
        icons[4].setAlpha(77);*/

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
        //tabLayout.getTabAt(3).setIcon(icons[3]);
        //tabLayout.getTabAt(4).setIcon(icons[4]);


    }

    /**
     * Method used to load content into / set up the right nav bar
     */
    private void setupNavDrawer(){

        //set up the nav bar and the list view inside of  it
        String[] options = {"Logout", "Settings", "Help", "About"};
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerList = (ListView) findViewById(R.id.right_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.nav_drawer_item
                ,R.id.nav_drawer_text_view,options));

        drawerList.setOnItemClickListener(new DrawerListClickListener());
    }

    /**
     * Listener object for items in the drawer listview
     */
    public class DrawerListClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            selectItem(position);
        }
    }

    /**
     *
     * @param pos position of the item in the list we selected
     */
    private void selectItem(int pos){

        if(pos == Constants.DRAWER_LIST_LOGIN){

            //log the user out
            StateManager.setLoggedIn(this, false);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_drawer_button) {

            drawerLayout.openDrawer(GravityCompat.END);
            return true;
        }

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
    public void onGroupSelect(Uri uri) {}

    /*===========================================================================================
    Miscellaneous methods
    ===========================================================================================*/

    public static JsonConverter.Me getMe(){return me;}

    public static User getUser(){return user;}
}
