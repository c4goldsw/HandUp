package com.handup.handup.controller.course.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.handup.handup.HandUp;
import com.handup.handup.R;
import com.handup.handup.bluetooth.BluetoothDataListener;
import com.handup.handup.bluetooth.ManageBluetoothConnection;
import com.handup.handup.bluetooth.OpenBluetoothServerConnection;
import com.handup.handup.helper.Constants;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

/**
 * Created by Christopher on 5/13/2016.  Took code from
 * developer.android.com/guide/topics/ui/dialogs.html#AddingAList
 */
public class ConnectDialog extends DialogFragment implements BluetoothDataListener{

    ConnectDialogListener mListener;
    private String courseID;
    private String uid;
    private ArrayList<String> otherUsersUIDs;

    OpenBluetoothServerConnection bluetoothServerConnection;
    ManageBluetoothConnection bluetoothConnection;

    //Used to listen for new Bluetooth device detections whilst discovering devices
    private BroadcastReceiver mReceiver;

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
    public void onDetach(){
        super.onDetach();

        //stop any ongoing listeners/ processes
        if(mRecyclerViewAdapter.bluetoothClientConnection != null)
            mRecyclerViewAdapter.bluetoothClientConnection.cancel();
        bluetoothServerConnection.cancel();
        getActivity().getApplicationContext().unregisterReceiver(mReceiver);
        HandUp.getBluetoothAdapter().cancelDiscovery();
        if(bluetoothConnection != null)
            bluetoothConnection.cancel();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //set the values for the strings to be transmitted
        courseID = getArguments().getString(Constants.DIALOG_BUNDLE_COURSE_ID);
        uid = getArguments().getString(Constants.DIALOG_BUNDLE_UID);
        otherUsersUIDs = getArguments().getStringArrayList(Constants.DIALOG_BUNDLE_OID);

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater, do UI stuff
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View list = inflater.inflate(R.layout.dialog_connect, null);
        mRecyclerViewAdapter = new ConnectDialogRecyclerViewAdapter(this);
        ((RecyclerView) list.findViewById(R.id.list)).setAdapter(mRecyclerViewAdapter);
        ((RecyclerView) list.findViewById(R.id.list))
                .setLayoutManager(new LinearLayoutManager(list.getContext()));

        builder.setView(list).setTitle("Find Users");

        //Query the current list of known Bluetooth devices.  Taken from tinyurl.com/cv7rnag
        Set<BluetoothDevice> knownBluetoothDevices = HandUp.getBluetoothAdapter().getBondedDevices();
        if(knownBluetoothDevices.size() > 0){
            for(BluetoothDevice bd: knownBluetoothDevices){
                Log.d(Constants.DEBUG_GENERAL, "ConnectDialog: A previously known is: " + bd.getName());
                mRecyclerViewAdapter.addItem(bd);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }

        //Start looking for Bluetooth devices to connect to.  Taken from tinyurl.com/cv7rnag
        HandUp.getBluetoothAdapter().startDiscovery();

        Log.d(Constants.DEBUG_GENERAL, "Bluetooth: started trying to discover");

        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    Log.d(Constants.DEBUG_GENERAL, "ConnectDialog: A device was found: " + device.getName());
                    mRecyclerViewAdapter.addItem(device);
                    mRecyclerViewAdapter.notifyDataSetChanged();
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().getApplicationContext().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        //Create and start the Bluetooth server socket
        bluetoothServerConnection = new OpenBluetoothServerConnection(this);
        bluetoothServerConnection.start();

        return builder.create();
    }

    /**
     * Here, we check to see if: the other user is a valid user AND that this user can claim points
     * @param response the response sent by the other user's Bluetooth device
     */
    @Override
    public void onDataReceived(byte[] message) {

        //convert the byte array to a null-terminated UTF-8 string
        int numBytes;
        for(numBytes = 0; numBytes < message.length && message[numBytes] != 0; ++numBytes);
        String response = "";
        try {
            response = new String(message, 0, numBytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        //get the UID and OID from the string
        String otherUID = response.split("_")[0];
        String otherCID = response.split("_")[1];

        //check to see if the user is in the course
        boolean foundOtherUser = false;
        for(String s : otherUsersUIDs){
            if(s.equals(otherUID))
                foundOtherUser = true;
        }

        if(!foundOtherUser || !otherCID.equals(courseID))
            return;

        Log.d(Constants.DEBUG_GENERAL, "ConnectDialog: the other user has been validated");
        //check to see if we've already been awarded a point for the day
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(System.currentTimeMillis()));
        final Firebase ref = new Firebase(Constants.FIRE_BASE_URL + "/meetings/" + uid + "/"
        + courseID + "/" + c.get(Calendar.DAY_OF_YEAR));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    //TODO: Alert the user that they already have a point
                    return;
                }

                ref.setValue(0); //give it an arbitrary value

                //increment the number of points the user has
                Firebase incrementPoints  = new Firebase(Constants.FIRE_BASE_URL + "/users/"
                        + uid + "/points");
                incrementPoints.runTransaction(new Transaction.Handler() {
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
                        else{
                            //TODO: Alert the user via a toast / the snackbar that they got a point

                        }
                    }
                });
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public void sendManagerThread(ManageBluetoothConnection m) {
        bluetoothConnection = m;
    }

    @Override
    public String getUID() {
        return uid;
    }

    @Override
    public String getCID() {
        return courseID;
    }

    public interface ConnectDialogListener {
        void onConnectionCompleted();
    }
}
