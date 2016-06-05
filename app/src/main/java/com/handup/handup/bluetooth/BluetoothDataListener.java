package com.handup.handup.bluetooth;

import com.handup.handup.model.User;

import java.util.ArrayList;

/**
 * Created by Christopher on 5/18/2016. Used by objects that are listening for bluetooth data
 */
public interface BluetoothDataListener {
    void onDataReceived(byte[] response);
    void sendManagerThread(ManageBluetoothConnection m);
    String getUID();
    String getCID();
}
