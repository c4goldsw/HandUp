package com.handup.handup.controller.course.user;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.handup.handup.R;
import com.handup.handup.helper.Constants;

/**
 * Created by Christopher on 2/27/2016.  Code taken from http://tinyurl.com/mnosctq
 */
public class SubscribeDialog extends DialogFragment {

    private String firstName;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        firstName = getArguments().getString(Constants.DIALOG_BUNDLE_NAME);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(firstName)
                .setPositiveButton("Subscribe", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("Unsubscribe", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
