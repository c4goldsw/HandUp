package com.handup.handup.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.pearson.pdn.learningstudio.content.ContentService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.oauth.*;

import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.handup.handup.helper.Constants;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;
import com.pearson.pdn.learningstudio.oauth.request.OAuth2Request;

/**
 * Created by Christopher on 12/23/2015.  Used to manage and persist the state of the app
 * (e.g. check to see if the user is logged in or not, check to see if it's the first time
 * it's being used, etc).
 */
public class StateManager {


    /**
     *
     * @param c Context for the app
     * @return Returns true if the user is using the app is being loaded on the phone
     * for the first time
     */
    public static boolean firstTimeUsing(Context c){

        File file = c.getFileStreamPath(Constants.APP_STATE_FILE);

        //first we check to see if
        if(!file.exists()) {
            createStateFile(file);

            Log.d("StateManager","First Time using");
            return true;

        /*Once the user logs in for the first time, the state will eill exist - we need
        to see if the user has logged in for the first time or not*/
        } else {

            try{
                RandomAccessFile rFile = new RandomAccessFile(file, "r");
                rFile.seek(0);
                if (rFile.readBoolean()) {

                    Log.d("StateManager", "First Time using (login complete)");
                    return true;
                }
                rFile.close();
            }
            catch (IOException e){
                System.err.println(e);
            }
        }

        return false;
    }

    /**
     * Sets the first time variable to false
     * @param c Context of the app
     */
    public static void setFirstTimeToFalse(Context c){
        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

        Log.d("StateManager", "Setting first time to false");

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(0);
            rFile.writeBoolean(false);
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    /**
     * Getter method for active user's username
     * @param c Context of the app
     * @return Activ user's username
     */
    public static String getUserName(Context c){

        String uName = "";

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

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

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

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

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

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

        Log.d("StateManager","Logged in: " + loggedIn);

        return loggedIn;
    }

    /**
     * Setter method for the login status of  the user
     * @param c Context of the app
     * @param value Value indicating login status
     */
    public static void setLoggedIn(Context c, boolean value){

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(2);
            rFile.writeBoolean(value);
            rFile.close();
        }
        catch (IOException e){
            System.err.println(e);
        }

        Log.d("StateManager","Logged in: " + value);
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
