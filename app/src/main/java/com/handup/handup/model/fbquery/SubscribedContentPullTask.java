package com.handup.handup.model.fbquery;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Christopher on 2/27/2016.
 */
public class SubscribedContentPullTask extends AsyncTask<Void, Void, Void> {

    ArrayList<Integer> subscriptions;
    String courseID;
    ContentPullTask.ContentQueryImplementer usingClass;

    public SubscribedContentPullTask(ArrayList<Integer> subscriptions, String courseID,
         ContentPullTask.ContentQueryImplementer usingClass){

        this.subscriptions = subscriptions;
        this.courseID = courseID;
        this.usingClass = usingClass;
    }

    @Override
    protected Void doInBackground(Void... params) {

        for(Integer uid : subscriptions) {

            Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" + uid +
                    "/" + courseID + "/lastContent");

            final int uidRef = uid;

            ref.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue() == null)
                        return;

                    String imageString = (String) dataSnapshot.child("image").getValue();

                    //get each approval
                    ArrayList<Integer> approvals = new ArrayList<Integer>();
                    for(Iterator<DataSnapshot> approvalIt = dataSnapshot.child(Constants.CONTENT_APPROVALS).getChildren().iterator();
                            approvalIt.hasNext();) {

                            approvals.add(Integer.parseInt(approvalIt.next().getKey()));
                    }

                    Content c = new Content();
                    c.setApproved(approvals);
                    c.setOwner(uidRef);
                    c.setImage(imageString);
                    c.setContentDescription((String) dataSnapshot.child(Constants.CONTENT_DESCRIPTION).getValue()
                    + ", " + c.getApprovalCount() + ((c.getApprovalCount() == 1) ? " approve":" approves"));

                    usingClass.onContentQueryFinish(c);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d("ContentRequest", "Error: " + firebaseError.toString());
                }
            });
        }

        return null;
    }
}
