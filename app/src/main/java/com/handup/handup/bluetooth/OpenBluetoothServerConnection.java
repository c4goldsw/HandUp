package com.handup.handup.bluetooth;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.util.Log;

import com.handup.handup.HandUp;
import com.handup.handup.helper.Constants;

import java.io.IOException;

/**
 * Created by Christopher on 5/17/2016.  Taken from https://developer.android.com/guide/topics/connectivity/bluetooth.html#ConnectingDevices
 */
public class OpenBluetoothServerConnection extends Thread {

    private BluetoothServerSocket sSocket;
    private BluetoothDataListener mListener;

    public OpenBluetoothServerConnection(BluetoothDataListener listener){

        mListener = listener;

        try {
            sSocket = HandUp.getBluetoothAdapter().listenUsingRfcommWithServiceRecord("HandUp",
                    Constants.appUuid);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        BluetoothSocket clientConnection = null;

        //listen for a connection / wait for an exception to occur
        while(true){
            try{

                Log.d(Constants.DEBUG_GENERAL,"OpenBluetoothServerConnection: server is set up!");
                clientConnection = sSocket.accept();

                //A connection is accepted
                if(sSocket != null){
                    //start managing the connection
                    Log.d(Constants.DEBUG_GENERAL, "Server made a connection to a client!");
                    ManageBluetoothConnection connection = new ManageBluetoothConnection(
                            clientConnection, mListener);
                    connection.start();
                    sSocket.close();
                    break;
                }

            }catch(IOException e){
                break;
            }
        }
    }

    public void cancel(){
        try{
            sSocket.close();
        } catch (IOException e){}
    }
}
