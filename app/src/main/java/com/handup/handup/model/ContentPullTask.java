package com.handup.handup.model;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.helper.Constants;

import java.util.Iterator;

/**
 * Created by Christopher on 1/14/2016.
 */
public class ContentPullTask extends AsyncTask<Void, Void, Void> {

    String courseID;
    String uid;
    ContentQueryImplementer cu;

    public ContentPullTask(String courseID, String uid, ContentQueryImplementer cu){

        this.courseID = courseID;
        this.uid = uid;
        this.cu = cu;
    }

    @Override
    protected Void doInBackground(Void... params) {


        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                "/" + courseID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //iteratres through the content for each user
                for(Iterator<DataSnapshot> childIt = dataSnapshot.getChildren().iterator();
                    childIt.hasNext(); ){

                    DataSnapshot contentIt = childIt.next();

                    String imageString = (String) contentIt.child("image").getValue();
                    boolean approved = (boolean) contentIt.child("approved").getValue();

                    Content c = new Content();
                    c.setApproved(approved);
                    c.setImage(imageString);

                    cu.onContentQueryFinish(c);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("ContentRequest","Error: " + firebaseError.toString());
            }
        });

        return null;
    }

    public interface ContentQueryImplementer{

        void onContentQueryFinish(Content c);
    }
}
