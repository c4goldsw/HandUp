package com.handup.handup.view;

/**
 * Created by Christopher on 12/27/2015.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handup.handup.controller.CourseFragment;
import com.handup.handup.controller.GroupFragment;
import com.handup.handup.controller.MainActivity;
import com.handup.handup.controller.ProfileFragment;

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

        /*if(position == 0) {
            return ProfileFragment.newInstance();
        }
        else if(position == 1){
            return CourseFragment.newInstance();
        }
        else if(position == 2){
            return GroupFragment.newInstance();
        }*/

        return MainActivity.PlaceholderFragment.newInstance(position + 1);
}

    @Override
    public int getCount() {

        return MainActivity.tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        /*if(position == 0)
            return "PROFILE";
        else if(position == 1)
            return "COURSES";
        else if(position == 2)
            return "STUDY";*/

        return null;
    }
}