package com.handup.handup.model;

import android.util.Base64;

import java.util.ArrayList;

/**
 * Created by Christopher on 1/1/2016. POJO for combining results of LS and Firebase user queries.  All of the data that this
 * object contains is needed to make the application "functional" from a single user's
 * point of view.
 */
public class User {

    private String uid;
    private long points;

    //profilePicture is only to be used for FireBase storage, whilst inAppProfilePicture
    //is for use in the app
    private String profilePicture;
    private byte[] inAppProfilePicture;

    public void setInAppProfilePicture(byte[] inAppProfilePicture) {
        this.inAppProfilePicture = inAppProfilePicture;
    }

    private String displayName;

    private int[] courses;

    private ArrayList<ArrayList<String>> contentAddresses;
    private ArrayList<ArrayList<String>> meetingAddresses;

    public User(){}

    public byte[] getInAppProfilePicture() {
        return inAppProfilePicture;
    }

    public int[] getCourses() {
        return courses;
    }

    public void setCourses(int[] courses) {
        this.courses = courses;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(long points) {
        this.points = points;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;

        //taken from http://stackoverflow.com/questions/26292969/can-i-store-image-files-in-firebase-using-java-api
        inAppProfilePicture = Base64.decode(profilePicture, Base64.DEFAULT);
    }

    public void setProfilePicture(String profilePicture, byte[] byteArray){
        this.profilePicture = profilePicture;
        inAppProfilePicture = byteArray;
    }

    public ArrayList<ArrayList<String>> getContentAddresses() {
        return contentAddresses;
    }

    public void setContentAddresses(ArrayList<ArrayList<String>> contentAddresses) {
        this.contentAddresses = contentAddresses;
    }

    public ArrayList<ArrayList<String>> getMeetingAddresses() {
        return meetingAddresses;
    }

    public void setMeetingAddresses(ArrayList<ArrayList<String>> meetingAddresses) {
        this.meetingAddresses = meetingAddresses;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}