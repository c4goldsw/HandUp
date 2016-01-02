package com.handup.handup.model;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.JsonConverter;
import com.pearson.pdn.learningstudio.core.AbstractService;
import com.pearson.pdn.learningstudio.core.BasicService;
import com.pearson.pdn.learningstudio.core.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Christopher on 12/31/2015. Contains code taken from the Perason API Java
 * documentation
 */
public class LsQueryTask extends AsyncTask<Void, Void, HashMap<String, Object>> {

    /**
     * The username is used to make requests to LS
     */
    String username;
    LsQueryUser usingClass;
    int queryType;

    private String[] profileQueryURLS = {"/me"};

    private String[][] queryCollection = {profileQueryURLS};

    /**
     * Constructor for the async task
     * @param username username that is used to make the REST call to learning studio
     * @param usingClass the object that is using this task
     */
    public LsQueryTask(String username, LsQueryUser usingClass, int queryType){

        this.username = username;
        this.usingClass = usingClass;
        this.queryType =  queryType;
    }

    @Override
    protected HashMap<String, Object> doInBackground(Void... params) {

        Log.d("Async", "Staring query with username: " + username);

        HashMap<String, Object> jsonResponses = new HashMap<>();

        //TODO: make generic?
        BasicService b = LsQueryObject.getSingleton().getBasicService();
        b.useOAuth2(username);

        Response r = null;

        for(int i = 0; i < queryCollection[queryType].length; i++){

            try{
                r = b.doMethod(AbstractService.HttpMethod.GET, queryCollection[queryType][i], null);

            } catch (IOException e){
                return null;
            }

            if(r.isError()) {
                int httpStatusCode = r.getStatusCode();// 404
                String httpStatusMessage = r.getStatusMessage();// Not Found

                System.out.println(httpStatusCode + " " + httpStatusMessage);
            }
            else {
                jsonResponses.put(profileQueryURLS[i], r.getContent());
            }
        }

        return jsonResponses;
    }

    @Override
    protected void onPostExecute(HashMap<String, Object> resultMap){

        //send data off to the calling activity
        usingClass.onLsQueryFinish(resultMap);
    }

    public interface LsQueryUser {

        public void onLsQueryFinish(HashMap<String, Object> map);
    }

}