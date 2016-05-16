package com.handup.handup.controller.course.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.handup.handup.HandUp;
import com.handup.handup.R;
import com.handup.handup.helper.Constants;

import java.util.Set;

/**
 * Created by Christopher on 5/13/2016.  Took code from
 * developer.android.com/guide/topics/ui/dialogs.html#AddingAList
 */
public class ConnectDialog extends DialogFragment{

    ConnectDialogListener mListener;
    private String name;
    private String courseID;

    ConnectDialogRecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);
        try{
            mListener = (ConnectDialogListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " doesn't implement "+
                    "SubscribeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //set the values for the strings to be transmitted
        name = getArguments().getString(Constants.DIALOG_BUNDLE_NAME);
        courseID = getArguments().getString(Constants.DIALOG_BUNDLE_COURSE_ID);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater, do UI stuff
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View list = inflater.inflate(R.layout.dialog_connect, null);
        mRecyclerViewAdapter = new ConnectDialogRecyclerViewAdapter();
        ((RecyclerView) list.findViewById(R.id.list)).setAdapter(mRecyclerViewAdapter);

        builder.setView(list).setTitle("Find Users");

        //Query the current list of known Bluetooth devices.  Taken from tinyurl.com/cv7rnag
        Set<BluetoothDevice> knownBluetoothDevices = HandUp.getBluetoothAdapter().getBondedDevices();
        if(knownBluetoothDevices.size() > 0){
            for(BluetoothDevice bd: knownBluetoothDevices){
                Log.d(Constants.DEBUG_GENERAL, "ConnectDialog: A previously known is: " + bd.getName());
                mRecyclerViewAdapter.addItem(bd.getName() + " " + bd.getAddress());
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }

        //Start looking for Bluetooth devices to connect to.  Taken from tinyurl.com/cv7rnag
        HandUp.getBluetoothAdapter().startDiscovery();

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    Log.d(Constants.DEBUG_GENERAL, "ConnectDialog: A device was found: " + device.getName());
                    mRecyclerViewAdapter.addItem(device.getName() + " " + device.getAddress());
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        return builder.create();
    }

    public interface ConnectDialogListener {
        void onConnectionCompleted();
    }
}
