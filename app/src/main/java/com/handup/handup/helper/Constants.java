package com.handup.handup.helper;

import android.support.design.internal.NavigationMenu;

/**
 * Created by Christopher on 12/22/2015.  A class that is used to store constants for
 * the application.
 */
public class Constants {

    //Constants related to the application
    public static final String APPLICATION_ID       = "1a9e31fc-425f-4d31-a288-5058867eed5b";
    public static final String TOKEN_KEY            = "04b11650-b3bb-41d6-91a6-c19936aaf4e5";
    public static final String APPLICATION_SECRET   = "8a72063be6d0409da86a5c239e39fd10";
    public static final String CLIENT_STRING        = "gbtestc";
    public static final String APP_NAME             = "HandUp";

    //Constants related to files
    public static final String APP_STATE_FILE_PREFIX = "app state file";

    //Consstats related to the user state
    public static final String NO_ACTIVE_USER       = "";
    public static final int MENUFRAGMENT_LOGOUT     = 0;

    //Activity result codes
    public static final int SELECT_IMAGE            = 0;
    public static final int TAKE_PHOTO              = 2;

    //Fire base related
    public static final String FIRE_BASE_URL        = "https://intense-inferno-38.firebaseio.com";

    //course fragment related
    public static final String PUT_EXTRA_COURSE_NAME    = "courseName";
    public static final String PUT_EXTRA_COURSE_ID      = "courseId";
    public static final String PUT_EXTRA_USERNAME = "username";
    public static final String PUT_EXTRA_UID = "uid";

    //profile fragment related
    public static final String PUT_EXTRA_COURSE_IDS="courseIdss";

    //Course Activity: User fragment related posts
    public static final String DATE = "com.handup.handup.controller.course.date";
    public static final int ContentListBaseSize = 130;
}
