package com.handup.handup.controller.course.user;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.helper.Constants;

import java.util.ArrayList;

/**
 * Created by Christopher on 5/13/2016.
 */
public class ConnectDialogRecyclerViewAdapter extends RecyclerView.Adapter<ConnectDialogRecyclerViewAdapter.ViewHolder> {

    ArrayList<ActiveUser> mValues;

    public ConnectDialogRecyclerViewAdapter(){
        mValues = new ArrayList<ActiveUser>();
    }

    public void addItem(String data){
        Log.d(Constants.DEBUG_GENERAL, "ContentDialogRecyclerThing: adding something!");
        mValues.add(new ActiveUser(data));
    }

    @Override
    public ConnectDialogRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_connect_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConnectDialogRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.name.setText(mValues.get(position).name);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.dialog_connect_text_view);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ActiveUser{

        private String name;

        public ActiveUser(String name){
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
