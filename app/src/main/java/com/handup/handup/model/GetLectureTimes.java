package com.handup.handup.model;

import android.os.AsyncTask;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.ContentTimeDeterminer;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Christopher on 1/5/2016.
 */
public class GetLectureTimes extends AsyncTask<Void, Void, Void>{

    OnLectureTimeGetFinish user;
    String courseID;

    public GetLectureTimes(String courseID, OnLectureTimeGetFinish user){
        this.user = user;
        this.courseID = courseID;
    }

    @Override
    protected Void doInBackground(Void... params) {


        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/lectureinfo/" + courseID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<Date> times = new ArrayList<Date>();

                for(Iterator<DataSnapshot> childIt = dataSnapshot.getChildren().iterator();
                    childIt.hasNext();){

                    times.add(ContentTimeDeterminer.getTimeFromString((String) childIt.next().getValue()));

                    Log.d("LectureTimes",times.get(times.size() - 1).toString());
                }

                user.onLectureTimeGetFinish(times);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return null;
    }

    public interface OnLectureTimeGetFinish{

        public void onLectureTimeGetFinish(ArrayList<Date> times);
    }
}
