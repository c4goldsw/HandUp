package com.handup.handup.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handup.handup.R;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.JsonConverter;
import com.handup.handup.model.LSQuery;
import com.handup.handup.model.StateManager;
import com.handup.handup.view.SectionsPagerAdapter;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.core.Response;

import java.io.IOException;

/**
 * I followed
 * <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">
 * this tutorial</a> and used code from it in order to create the tab views.
 */
public class MainActivity extends AppCompatActivity implements ProfileFragment.OnProfileInteractionListener,
CourseFragment.OnFragmentInteractionListener, GroupFragment.OnFragmentInteractionListener{

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
    private LSQuery q;

    static String name = "";

    /*===========================================================================================
    Model / Controller related methods
    ===========================================================================================*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //before loading the main activity, we determine what state the user was last in
        determineState();

        //enable and set up the tool bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        setupTabs();
        setupNavDrawer();

        new Query(StateManager.getUserName(this)).execute();
    }

    /**
     * As this is the default launch activity, we have to check what the previous state of the
     * app was.  If the user wasn't logged in, they're sent to the login screen. Otherwise, we
     * load the app from here
     */
    private void determineState(){

        if(StateManager.firstTimeUsing(this)) {
            if(StateManager.isLoggedIn(this)){

                StateManager.setFirstTimeToFalse(this);
                //start the tutorial

            } else {

                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }

        } else {
            if(!StateManager.isLoggedIn(this)){
                Log.d("StateManager","Regular login");
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    /**
     * Async task used to make a LS Query.  Contains code taken from the Perason API Java
     * documentation
     */
    private class Query extends AsyncTask<Void, Void, String>{

        String username;

        public Query(String username){
            this.username = username;
            Log.d("Async","Username: " + username);
        }

        @Override
        protected String doInBackground(Void... params) {

            Log.d("Async","Staring query with username: " + username);

            String jsonResponse = "";

            BasicService b = LSQuery.getSingleton().getBasicService();
            b.useOAuth2(username);

            Response r = null;

            try{
                r = b.doMethod(AbstractService.HttpMethod.GET, "/me", null);
            } catch (IOException e){
                return e.toString();
            }

            if(r.isError()) {
                int httpStatusCode = r.getStatusCode();// 404
                String httpStatusMessage = r.getStatusMessage();// Not Found

                System.out.println(httpStatusCode + " " + httpStatusMessage);
            }
            else {
                jsonResponse = r.getContent();
            }

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String content){

            Gson g  = new Gson();

            try {
                JsonConverter.Me m = g.fromJson(content, JsonConverter.Me.class);
                name = "Name: "+ m.getMe().getFirstName();

            } catch(JsonSyntaxException b){
                Log.d("Async","Error: " + b);
                name = b.toString();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple com.handup.handup.view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab_view, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(name);
            return rootView;
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

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_stars_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[1].setAlpha(77);

        icons[2] = ContextCompat.getDrawable(this, R.drawable.ic_people_24dp);
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
    public void onProfileSelect(Uri uri) {}

    @Override
    public void onCourseSelect(Uri uri) {}

    @Override
    public void onGroupSelect(Uri uri) {}
}
