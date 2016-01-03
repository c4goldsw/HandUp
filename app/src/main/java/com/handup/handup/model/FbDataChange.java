package com.handup.handup.model;

import android.os.AsyncTask;

import com.firebase.client.Firebase;
import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 1/3/2016.  Used for making basic changes to firebase data
 */
public class FbDataChange extends AsyncTask<Void, Void, Void> {

    private Object data;
    private String change;

    public FbDataChange(String change, Object data){

        this.data = data;
        this.change = change;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + change);
        ref.setValue(data);

        return null;
    }
}
