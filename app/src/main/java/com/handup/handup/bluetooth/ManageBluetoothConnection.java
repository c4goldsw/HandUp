package com.handup.handup.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.handup.handup.helper.Constants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Christopher on 5/18/2016.  Taken from
 * https://developer.android.com/guide/topics/connectivity/bluetooth.html#ManagingAConnection
 */
public class ManageBluetoothConnection extends Thread {

    private BluetoothSocket connection;
    private InputStream iStream;
    private OutputStream oStream;
    private BluetoothDataListener mListener;

    public ManageBluetoothConnection(BluetoothSocket bSock, BluetoothDataListener listener){
        connection = bSock;
        mListener = listener;
        mListener.sendManagerThread(this);

        try{
            iStream = connection.getInputStream();
            oStream = connection.getOutputStream();
        } catch (IOException e){}
    }

    @Override
    public void run(){

        byte[] buffer = new byte[2^10];
        ByteArrayOutputStream message = new ByteArrayOutputStream();

        //write the UID and OID, then close the writing stream;
        try {
            Log.d(Constants.DEBUG_GENERAL,"Sending data to other BD device: " +
            mListener.getUID() + "_" + mListener.getCID());
            oStream.write((mListener.getUID() + "_" + mListener.getCID() + '\0').getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(Constants.DEBUG_GENERAL,"Data sent!");

        //listen for data from other Bluetooth device
        while(true){
            try{
                int result = iStream.read(buffer);
                if(result == -1)
                    mListener.onDataReceived(message.toByteArray());
                message.write(buffer);
            } catch (IOException e){
                mListener.onDataReceived(message.toByteArray());
                break;
            }
        }

        //close the connection
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancel(){
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
