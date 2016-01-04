package com.handup.handup.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.controller.course.content.ContentFragment;
import com.handup.handup.controller.course.user.UserFragment;
import com.handup.handup.controller.main.ProfileFragment;

/**
 * Created by Christopher on 1/4/2016.
 */
public class CourseSectionPagerAdapter extends FragmentPagerAdapter {

    public CourseSectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if(position == 0){
            return UserFragment.newInstance();
        } else {
            return ContentFragment.newInstance(2);
        }
    }

    @Override
    public int getCount() {
        return CourseActivity.tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position){return null;}
}
