package com.handup.handup.model;

import java.util.ArrayList;

/**
 * Created by Christopher on 1/1/2016. POJO for combining results of LS and Firebase user queries.  All of the data that this
 * object contains is needed to make the application "functional" from a single user's
 * point of view.
 */
public class User {

    private String uid;
    private int points;
    private byte[] profilePicture;

    private int[] courses;

    private ArrayList<ArrayList<String>> contentAddresses;
    private ArrayList<ArrayList<String>> meetingAddresses;

    public User(){

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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
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
}