package com.handup.handup.helper;

import android.support.design.internal.NavigationMenu;

import java.util.UUID;

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
    public static final String PUT_EXTRA_DISPLAY_NAME = "display_name";
    public static final String PUT_EXTRA_UID = "uid";
    public static final int COURSE_ACT_FOR_RESULT = 19;
    public static final int COURSE_ACT_CONTENT_ADDED = 20;

    //profile fragment related
    public static final String PUT_EXTRA_COURSE_IDS="courseIdss";

    //Course Activity: User fragment related posts
    public static final String DATE = "com.handup.handup.controller.course.date";
    public static final int ContentListBaseSize = 130;
    public static final boolean ADDING_NEW_CONTENT = true;
    public static final boolean NOT_ADDING_NEW_CONTENT = false;

    public static final String DIALOG_BUNDLE_TITLE = "title";
    public static final String DIALOG_BUNDLE_BOOL_VAL = "BOOL_VAL";
    public static final String DIALOG_BUNDLE_UID = "uid";
    public static final String DIALOG_BUNDLE_OID = "oid"; //(Other user's ID)
    public static final String DIALOG_BUNDLE_COURSE_ID = "courseId";

    //CONTENT RELATED FIREBASE PATHS
    public static final String CONTENT_APPROVALS = "approvals";
    public static final String CONTENT_DESCRIPTION = "description";
    public static final String CONTENT_DAY = "dayOfYear";

    //LOGCAT DEBUGGING
    public static final String DEBUG_GENERAL = "DEBUG_GEN";
    public static final String DEBUG_FIREBASE = "DEBUG_FIREBASE";

    //BLUETOOTH RELATED
    public static final int BLUETOOTH_ENABLE = 89;
    public static final int BLUETOOTH_MAKE_DISCOVERABLE = 90;
    public static final String DIALOG_BUNDLE_NAME = "diaBunName";
    public static final UUID appUuid = UUID.fromString("993eec49-a05d-4b0e-bb03-f837f8e1af1a");
    public static final String DIALOG_BUNDLE_OTHER_USERS = "OTHER USERS";
}
