package com.handup.handup.model.fbquery;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ContentTimeDeterminer;
import com.handup.handup.helper.ImageHandler;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Christopher on 1/5/2016.
 */
public class ContentPushTask extends AsyncTask<Void, Void, Void>{

    Uri uri;
    String imageString;
    String courseID;
    String uid;
    String userName;
    String lectureDay;
    String courseName;

    public ContentPushTask(String userName, String courseName, String imageString, String uid, String courseID,
                           AppCompatActivity a, String d){

        this.imageString = imageString;
        this.courseID = courseID;
        this.uid = uid;
        this.courseName = courseName;
        lectureDay = d;
        this.userName = userName;
    }

    @Override
    protected Void doInBackground(Void... params) {

        /*
         * Storing image for user's own content library in profile fragment
         */
        SimpleDateFormat time = new SimpleDateFormat("dd.MM.yy");

        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                 "/" + courseID + "/" + lectureDay);
        ref.child("image").setValue(imageString);
        ref.child(Constants.CONTENT_DESCRIPTION).setValue(courseName + ", " + time.format(new Date()));


        /*Storing last image submitted for the course - this is the image that others will see
        in the content feed*/
        final Firebase contentFeedRef = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                "/" + courseID + "/lastContent");
        contentFeedRef.child("image").setValue(imageString);
        contentFeedRef.child(Constants.CONTENT_DESCRIPTION).setValue(userName);


        //TODO: We need to see if it's a new day - then we still need to award a point
        /*assign user their own point for uploading an image, if they don't already have a one!*/
        Firebase checkUserApproval = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                "/" + courseID + "/lastContent/" + Constants.CONTENT_APPROVALS + "/" + uid);
        checkUserApproval.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() == null){

                    contentFeedRef.child(Constants.CONTENT_APPROVALS + "/" + uid).setValue(0);

                    Firebase incrementPoints  = new Firebase(Constants.FIRE_BASE_URL + "/users/"
                    + uid + "/points");
                    incrementPoints.runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {

                            if(mutableData.getValue() == null){
                                mutableData.setValue(1);
                            }else{
                                mutableData.setValue((long) mutableData.getValue() + 1);
                            }

                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                            if(firebaseError != null)
                                Log.d(Constants.DEBUG_FIREBASE, firebaseError.toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /*Storing an the image at a higher level so we can grab a preview for the profile page
        easily*/
        ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid + "/lastContent");
        ref.setValue(imageString);

        return null;
    }
}