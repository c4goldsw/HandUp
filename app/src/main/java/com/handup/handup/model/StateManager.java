package com.handup.handup.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.content.Context;
import android.util.Log;

import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 12/23/2015.  Used to manage and persist the state of the app
 * (e.g. check to see if the user is logged in or not, check to see if it's the first time
 * it's being used, etc).
 */
public class StateManager {

    /**
     * Getter method for active user's username
     * @param c Context of the app
     * @return Activ user's username
     */
    public static String getUserName(Context c){

        String uName = "";

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE_PREFIX);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "r");
            rFile.seek(4);
            uName = rFile.readUTF();
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }

        return uName;
    }

    /**
     * Setter method for ther username of the active user
     * @param c Context of the app
     * @param value User's username
     */
    public static void setUserName(Context c, String value){

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE_PREFIX);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(4);
            rFile.writeUTF(value);
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    /**
     * Used to determine if the user is logged in
     * @param c Context of the app
     * @return returns the login status as a boolean valuee
     */
    public static boolean isLoggedIn(Context c){

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE_PREFIX);

        boolean loggedIn = false;

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "r");
            rFile.seek(2);
            loggedIn = rFile.readBoolean();
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }

        return loggedIn;
    }

    /**
     * Setter method for the login status of  the user
     * @param c Context of the app
     * @param value Value indicating login status
     */
    public static void setLoggedIn(Context c, boolean value){

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE_PREFIX);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(2);
            rFile.writeBoolean(value);
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }

    }

    /**
     * Creates the state file for the app as a random access file.  The lengths are as follows:
     *  - 2 bytes for boolean firstTime
     *  - 2 bytes to determine if the user has logged in or not
     *  - 100 bytes for the user name
     * @param f The state file
     */
    public static void createStateFile(File f){

        try {
            RandomAccessFile rFile = new RandomAccessFile(f, "rw");
            rFile.setLength(204);

            rFile.seek(0);
            rFile.writeBoolean(true); //User is using the app for the first time
            rFile.seek(2);
            rFile.writeBoolean(false); //User is not logged in by default

            //strings don't need to be written
        }
        catch (IOException e){
            System.err.println(e);
        }
    }
}
