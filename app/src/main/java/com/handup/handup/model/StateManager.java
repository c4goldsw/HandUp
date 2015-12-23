package com.handup.handup.model;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.pearson.pdn.learningstudio.content.ContentService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.oauth.*;

import android.content.Context;

import com.handup.handup.helper.Constants;
import com.pearson.pdn.learningstudio.oauth.config.OAuthConfig;

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
            return true;

        /*Once the user logs in for the first time, the state will eill exist - we need
        to see if the user has logged in for the first time or not*/
        } else {

            try{
                RandomAccessFile rFile = new RandomAccessFile(file, "r");
                rFile.seek(0);
                if (rFile.readBoolean())
                    return true;

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

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(0);
            rFile.writeBoolean(false);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    /**
     * Getter method for the tokens
     * @param c Context of the app
     * @return Values for each token
     */
    public static String[] getTokens(Context c){

        String[] tokens = new String[2];
        byte[] access = new byte[150];
        byte[] refresh = new byte[150];

        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(2);
            rFile.read(access);
            rFile.seek(152);
            rFile.read(refresh);
        }
        catch (IOException e){
            System.err.println(e);
        }

        tokens[0] = access.toString();
        tokens[1] = refresh.toString();

        return tokens;
    }

    /**
     * Setter method for the OAuth2 tokens
     * @param c Context of the app
     * @param tokens Tokens provided by the LS OAuth2 call
     */
    public static void setTokens(Context c, String[] tokens){

        byte[] access = tokens[0].getBytes();
        byte[] refresh = tokens[1].getBytes();
        File file = new File(c.getFilesDir(), Constants.APP_STATE_FILE);

        try {
            RandomAccessFile rFile = new RandomAccessFile(file, "rw");
            rFile.seek(2);
            rFile.write(access);
            rFile.seek(152);
            rFile.write(refresh);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    /**
     * Used to determine if the user is logged in by checking the access tokens
     * @param c Context of the app
     * @return returns the login status as a boolean valuee
     */
    public static boolean isLoggedIn(Context c){

        String token = getTokens(c)[0];

        OAuthConfig config = new OAuthConfig();
        config.setConsumerKey(Constants.TOKEN_KEY);
        config.setClientString(Constants.CLIENT_STRING);
        config.setApplicationId(Constants.APPLICATION_ID);

        OAuthServiceFactory oauthFactory = new OAuthServiceFactory(config);
        OAuth2PasswordService oauthService = oauthFactory.build(OAuth2PasswordService.class);

        

        return true;
    }

    /**
     * Creates the state file for the app as a random access file.  The lengths are as follows:
     *  - 2 bytes for boolean firstTime
     *  - 150 bytes for String accessToken
     *  - 150 bytes for String refresh token
     * @param f The state file
     */
    public static void createStateFile(File f){

        try {
            RandomAccessFile rFile = new RandomAccessFile(f, "w");
            rFile.setLength(302);

            rFile.seek(0);
            rFile.writeBoolean(true); //User is logged in for the first time

            //strings don't need to be written
        }
        catch (IOException e){
            System.err.println(e);
        }
    }
}
