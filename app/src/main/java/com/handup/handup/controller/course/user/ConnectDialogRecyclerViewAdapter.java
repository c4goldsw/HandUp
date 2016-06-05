package com.handup.handup.controller.course.user;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.bluetooth.BluetoothDataListener;
import com.handup.handup.bluetooth.OpenBluetoothClientConnection;
import com.handup.handup.helper.Constants;

import java.util.ArrayList;

/**
 * Created by Christopher on 5/13/2016.
 */
public class ConnectDialogRecyclerViewAdapter extends RecyclerView.Adapter<ConnectDialogRecyclerViewAdapter.ViewHolder> {

    ArrayList<BluetoothDevice> mValues;
    OpenBluetoothClientConnection bluetoothClientConnection;
    private BluetoothDataListener mListener;

    public ConnectDialogRecyclerViewAdapter(BluetoothDataListener listener){
        mValues = new ArrayList<BluetoothDevice>();
        mListener = listener;
    }

    public void addItem(BluetoothDevice bd){
        Log.d(Constants.DEBUG_GENERAL, "ContentDialogRecyclerThing: adding something!");
        mValues.add(bd);
    }

    @Override
    public ConnectDialogRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_connect_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnectDialogRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(mValues.get(position).getName());
        holder.detectedDevice = mValues.get(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        BluetoothDevice detectedDevice;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.dialog_connect_text_view);
            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //start a connection with this device
            Log.d(Constants.DEBUG_GENERAL, "Touching the list in the connect dialog!");

            if(bluetoothClientConnection != null){
                bluetoothClientConnection.cancel();
            }

            OpenBluetoothClientConnection bluetoothClientConnection =
                    new OpenBluetoothClientConnection(detectedDevice, mListener);
            bluetoothClientConnection.start();
        }
    }
}
