package com.handup.handup.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handup.handup.R;
import com.handup.handup.helper.Constants;
import com.handup.handup.helper.JsonConverter;
import com.handup.handup.model.LsQueryTask;
import com.handup.handup.model.StateManager;
import com.handup.handup.model.User;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProfileInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements LsQueryTask.LsQueryUser{

    private TextView userName;

    /**
     * This is the POJO that is used to store all firebase user queries
     */
    private User user = new User();

    private OnProfileInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load data into the UI
        new LsQueryTask(StateManager.getUserName(getContext()), this,
                Constants.PROFILE_REQUEST).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View ui = inflater.inflate(R.layout.fragment_profile, container, false);

        //get UI views
        userName = (TextView) ui.findViewById(R.id.profile_card_name);
        userName.setText(MainActivity.getMe().getMe().getFirstName());

        //if(user.)

        return ui;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfileSelect(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProfileInteractionListener) {
            mListener = (OnProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This method is called when an LsQueryObject finishes from the async query task
     * @param map Contains the data that will be loaded in the activity
     */
    @Override
    public void onLsQueryFinish(HashMap<String, Object> map) {

        Gson g  = new Gson();
        JsonConverter.Me me = null;

        try {
            me = g.fromJson((String) map.get("/me"), JsonConverter.Me.class);
            userName.setText(me.getMe().getFirstName());
        } catch (JsonSyntaxException b) {
            Log.d("Async", "Error: " + b);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProfileInteractionListener {

        void onProfileSelect(Uri uri);
    }
}
