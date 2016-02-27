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
import com.handup.handup.model.InitialQueryTask;

/**
 * Created by Christopher on 2/27/2016.
 */
public class ContentPreviewQuery extends AsyncTask<Void, Void, Void>{

    InitialQueryTask.InitialQueryUser usingClass;


    public ContentPreviewQuery (InitialQueryTask.InitialQueryUser usingClass){

        this.usingClass = usingClass;
    }

    @Override
    protected Void doInBackground(Void... params) {


        Firebase getContentPreviewRef = new Firebase(Constants.FIRE_BASE_URL + "/content/" +
                MainActivity.getMeRequest().getMe().getId() + "/lastContent");


        getContentPreviewRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("FireBase", "I'm called: " + dataSnapshot.getValue());

                if (dataSnapshot.getValue() == null) {

                    Log.d("FireBase", "Setting content preview to false: " + dataSnapshot.getValue());
                    MainActivity.setUserContentPrview(null);
                } else {
                    byte[] imageBytes = Base64.decode((String) dataSnapshot.getValue(), Base64.DEFAULT);
                    MainActivity.setUserContentPrview(BitmapFactory.decodeByteArray(imageBytes, 0,
                            imageBytes.length));
                }

                usingClass.updateFragmentUIs();

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return null;
    }
}
