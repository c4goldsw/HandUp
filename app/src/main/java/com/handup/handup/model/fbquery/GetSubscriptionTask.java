package com.handup.handup.model.fbquery;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.controller.main.MainActivity;
import com.handup.handup.helper.Constants;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Christopher on 2/27/2016.
 */
public class GetSubscriptionTask extends AsyncTask<Void, Void, Void> {

    private String courseID;
    private String uid;
    private GetSubscriptionTaskUser usingClass;

    /**
     * Constructor
     */
    public GetSubscriptionTask(String uid, String courseID, GetSubscriptionTaskUser usingClass){

        this.courseID = courseID;
        this.uid = uid;
        this.usingClass = usingClass;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Firebase getSubscriptionsRef = new Firebase(Constants.FIRE_BASE_URL + "/subscriptions/" +
                courseID + "/" + uid);

        getSubscriptionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> subscriptionIDs = new ArrayList<String>();

                if (dataSnapshot.getValue() != null) {

                    //iterate through the children of dataSnapshop, load into subscriptions
                    Iterator<DataSnapshot> iter =  dataSnapshot.getChildren().iterator();
                    while(iter.hasNext()){

                        //Note: the key of each entry is what we want.
                        subscriptionIDs.add(iter.next().getKey());
                    }
                }

                usingClass.onGetSubscriptionFinish(subscriptionIDs);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.d(Constants.DEBUG_GENERAL, firebaseError.toString());
            }
        });
        return null;
    }

    public interface GetSubscriptionTaskUser{

        void onGetSubscriptionFinish(ArrayList<String> subscriptionIDs);
    }
}
