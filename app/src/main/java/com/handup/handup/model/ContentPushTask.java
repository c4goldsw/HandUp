package com.handup.handup.model;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.Firebase;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ContentTimeDeterminer;
import com.handup.handup.helper.ImageHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Christopher on 1/5/2016.
 */
public class ContentPushTask extends AsyncTask<Void, Void, Void>{

    Uri uri;
    String imageString;
    String courseID;
    String uid;
    String lectureDay;


    public ContentPushTask(String imageString, String uid, String courseID, AppCompatActivity a, String d){

        this.imageString = imageString;
        this.courseID = courseID;
        this.uid = uid;
        lectureDay = d;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                 "/" + courseID + "/" + lectureDay);

        Firebase refImage = ref.child("image");

        refImage.setValue(imageString);

        Firebase refApproved = ref.child("approved");
            refApproved.setValue(false);

        Log.d("ContentSubmit", "Pushed Photo");

        return null;
    }
}