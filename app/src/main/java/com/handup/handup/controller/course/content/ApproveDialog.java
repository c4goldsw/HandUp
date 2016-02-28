package com.handup.handup.controller.course.content;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 2/28/2016.
 */
public class ApproveDialog extends DialogFragment {

    private String name;
    private String uid;
    private String oid;
    private String courseId;
    private boolean approved;

    ApproveDialogListener mListener;

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);
        try{
            mListener = (ApproveDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " doesn't implement "+
                    "ApproveDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        name = getArguments().getString(Constants.DIALOG_BUNDLE_TITLE);
        uid = getArguments().getString(Constants.DIALOG_BUNDLE_UID);
        oid = getArguments().getString(Constants.DIALOG_BUNDLE_OID);
        courseId = getArguments().getString(Constants.DIALOG_BUNDLE_COURSE_ID);
        approved = getArguments().getBoolean(Constants.DIALOG_BUNDLE_BOOL_VAL);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CharSequence[] s = {"APPROVE"};
        final boolean[] checkedItems = {approved};


        builder.setTitle(name)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/content/" +
                        oid + "/" + courseId + "/lastContent/" + Constants.CONTENT_APPROVALS +
                        "/" + uid);

                        Firebase changePoints  = new Firebase(Constants.FIRE_BASE_URL + "/users/"
                                + oid + "/points");

                        //disapprove
                        if(!checkedItems[0] && approved){

                            mListener.removeApproval(oid);

                            ref.removeValue();
                            changePoints.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {

                                    if (mutableData.getValue() == null) {
                                        mutableData.setValue(1);
                                    } else {
                                        mutableData.setValue((long) mutableData.getValue() - 1);
                                    }

                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if (firebaseError != null)
                                        Log.d(Constants.DEBUG_FIREBASE, firebaseError.toString());
                                }
                            });
                        }
                        //approve
                        else if(checkedItems[0] && !approved){

                            mListener.addApproval(oid);

                            ref.setValue(0);
                            changePoints.runTransaction(new Transaction.Handler() {
                                @Override
                                public Transaction.Result doTransaction(MutableData mutableData) {

                                    if (mutableData.getValue() == null) {
                                        mutableData.setValue(1);
                                    } else {
                                        mutableData.setValue((long) mutableData.getValue() + 1);
                                    }

                                    return Transaction.success(mutableData);
                                }

                                @Override
                                public void onComplete(FirebaseError firebaseError, boolean b, DataSnapshot dataSnapshot) {
                                    if (firebaseError != null)
                                        Log.d(Constants.DEBUG_FIREBASE, firebaseError.toString());
                                }
                            });
                        }
                    }

                    //Taken from http://tinyurl.com/44afnd6
                }).setMultiChoiceItems(s, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                checkedItems[which] = isChecked;
            }
        });


        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ApproveDialogListener{
        void removeApproval(String oid);
        void addApproval(String oid);
    }
}
