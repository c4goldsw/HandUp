package com.handup.handup.model;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import com.firebase.client.Firebase;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ContentTimeDeterminer;
import com.handup.handup.helper.ImageHandler;

import java.io.IOException;

/**
 * Created by Christopher on 1/5/2016.
 */
public class FbContentPushTask  extends AsyncTask<Void, Void, Void>{

    Uri uri;
    String courseID;
    String uid;
    AppCompatActivity a;

    public FbContentPushTask(Uri uri, String courseID, String uid, AppCompatActivity a){

        this.uri = uri;
        this.courseID = courseID;
        this.uid = uid;
        this.a = a;
    }

    @Override
    protected Void doInBackground(Void... params) {

        try {
            String imageString = ImageHandler.getImageString(
                    ImageHandler.getPortraitImage(uri, a, 1000, 1000));
            Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + courseID +
                    ContentTimeDeterminer.determineLectureNumber(courseID) + "/" + uid);

            ref.setValue(imageString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
