package com.handup.handup.controller.course.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.firebase.client.Firebase;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 2/27/2016.  Code taken from http://tinyurl.com/mnosctq
 */
public class SubscribeDialog extends DialogFragment {

    private String name;
    private String subId;
    private boolean subscribed;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        name = getArguments().getString(Constants.DIALOG_BUNDLE_NAME);
        subId = getArguments().getString(Constants.DIALOG_BUNDLE_UID);
        subscribed = getArguments().getBoolean(Constants.DIALOG_BUNDLE_SUBSCRIBED);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        CharSequence[] s = {"SUBSCRIBE"};
        final boolean[] checkedItems = {subscribed};


        builder.setTitle(name)
            .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            })
            .setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/subscriptions/" +
                            CourseActivity.getCourseID() + "/" + CourseActivity.getUid() + "/" + subId);

                    //unsubscribe
                    if(!checkedItems[0] && subscribed){

                        //TODO: Update the content feed
                        CourseActivity.subscriptionIDs.remove(new Integer(Integer.parseInt(subId)));
                        ref.removeValue();
                    }
                    //subscribe
                    else if(checkedItems[0] && !subscribed){

                        //TODO: Update the content feed
                        CourseActivity.subscriptionIDs.add(Integer.parseInt(subId));
                        ref.setValue("0");
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
}