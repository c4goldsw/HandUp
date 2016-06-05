package com.handup.handup.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.handup.handup.HandUp;
import com.handup.handup.helper.Constants;

import java.io.IOException;

/**
 * Created by Christopher on 5/17/2016. Taken from https://developer.android.com/guide/topics/connectivity/bluetooth.html#ConnectingDevices
 */
public class OpenBluetoothClientConnection extends Thread{

    private BluetoothSocket clientSocket;
    private BluetoothDataListener mListener;

    public OpenBluetoothClientConnection(BluetoothDevice bd, BluetoothDataListener listener){

        mListener = listener;

        try{
            clientSocket = bd.createRfcommSocketToServiceRecord(Constants.appUuid);
        }catch(IOException e){}
    }

    @Override
    public void run(){

        //the discovery process is cancelled since it slows the conneciton process -
        //also, for the purposes of the app, it's no longer needed
        HandUp.getBluetoothAdapter().cancelDiscovery();

        Log.d(Constants.DEBUG_GENERAL, "OpenBluetoothClientConnection:Attempting to open a " +
                "connection with a server!");

        try{
            //attempt to open a connection
            clientSocket.connect();
        }catch(IOException connectException){

            //Can't connect, close the socket
            Log.d(Constants.DEBUG_GENERAL, "OpenBluetoothClientConnection: unable to connect, " +
            "closing the socket.");
            try{
                clientSocket.close();
            }catch (IOException closeException){}

            return;
        }

        //Start managing the connection
        Log.d(Constants.DEBUG_GENERAL, "Bluetooth connection established!");
        ManageBluetoothConnection connection = new ManageBluetoothConnection(clientSocket,
                mListener);
        connection.start();
    }

    public void cancel(){
        try{
            clientSocket.close();
        } catch(IOException e){}
    }
}
