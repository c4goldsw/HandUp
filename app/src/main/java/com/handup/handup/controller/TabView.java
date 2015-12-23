package com.handup.handup.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.model.StateManager;

/**
 * I followed
 * <a href="http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/">
 * this tutorial</a> and used code from it in order to create the tab views.
 */
public class TabView extends AppCompatActivity {

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
    private final int tabCount = 4;

    private int currentTab = 0;

    /**
     * An array of Drawables containing each icon used in the tabs
     */
    Drawable [] icons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_view);

        determineState();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final Context c = this;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                icons[currentTab].setAlpha(77);
                icons[currentTab].setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                icons[position].setAlpha(255);
                icons[position].setColorFilter(ContextCompat.getColor(c, R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                currentTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
    }

    /**
     * As this is the default launch activity, we have to check what the previous state of the
     * app was.  If the user wasn't logged in, they're sent to the login screen. Otherwise, we
     * load the app from here
     */
    private void determineState(){

        if(!StateManager.firstTimeUsing(this)) {
            if(StateManager.isLoggedIn(this)){

                StateManager.setFirstTimeToFalse(this);
                //start the tutorial

            } else {
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }

        } else {
            if(!StateManager.isLoggedIn(this)){
                finish();
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    /**
     * Sets the icons for each tab
     */
    private void setupTabIcons(){

        icons = new Drawable[4];

        icons[0] = ContextCompat.getDrawable(this, R.drawable.ic_stars_24dp);
        icons[0].setAlpha(255); // set to 255 as this is the initial tab
        icons[0].setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        icons[1] = ContextCompat.getDrawable(this, R.drawable.ic_school_24dp);
        icons[1].setAlpha(77);

        icons[2] = ContextCompat.getDrawable(this, R.drawable.ic_people_24dp);
        icons[2].setAlpha(77);

        icons[3] = ContextCompat.getDrawable(this, R.drawable.ic_search_24dp);
        icons[3].setAlpha(77);

        tabLayout.getTabAt(0).setIcon(icons[0]);
        tabLayout.getTabAt(1).setIcon(icons[1]);
        tabLayout.getTabAt(2).setIcon(icons[2]);
        tabLayout.getTabAt(3).setIcon(icons[3]);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        /**
         * Returns the fragment associated with a particular tab / position.
         * @param position the requested position
         * @return the fragment associated with the requested tab / position
         */
        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {

            return tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }
}
