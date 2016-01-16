package com.handup.handup.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;

import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.controller.main.ProfileFragment;
import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 1/4/2016.
 */
public class CourseSectionPagerAdapter extends FragmentPagerAdapter {

    private float dpWidth;

    public CourseSectionPagerAdapter(FragmentManager fm, Context c) {
        super(fm);

        /*We need to determine the overall size of the screen.  We can then set the
            size and number of photo tiles accordingly*/
        DisplayMetrics dm = c.getResources().getDisplayMetrics();
        dpWidth = dm.widthPixels / dm.density;
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){
            return UserFragment.newInstance();
        } else {

            /*Here, we're dividing by the widht in dp by Constants.ContentListBaseSizee, that is,
            * the smallest size for each image tile.  Each tile will resized to fit the screen
            * seamlessly*/
            return ContentFragment.newInstance(dpWidth);
        }
    }

    @Override
    public int getCount() {
        return CourseActivity.tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position){return null;}
}
