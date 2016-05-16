package com.handup.handup;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.handup.handup.controller.main.MainActivity;

import java.lang.ref.WeakReference;

public class HandUp extends Application {

    //taken from SO: tinyurl.com/zhhlucg
    /**
     * This weak reference is used so that main can be GC
     */
    private static WeakReference<MainActivity> wr;

    private static BluetoothAdapter ba;

    /**
     * Getter method that indicates whether the phone supports Bluetooth or not
     * @return supportsBluetooth
     */
    public static boolean doesSupportBluetooth(){
        ba = BluetoothAdapter.getDefaultAdapter();
        return ba!=null;
    }

    public static BluetoothAdapter getBluetoothAdapter(){
        return ba;
    }

    /**
     * Getter method for wr
     * @return returns a WeakReference to the main activity
     */
    public static WeakReference<MainActivity> getMainWR(){
        return wr;
    }

    /**
     * Setter method for the main activity WeakReference
     * @param activity
     */
    public static void setMainWR(MainActivity activity){
        wr = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
