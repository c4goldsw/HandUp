package com.handup.handup.controller.course.user;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handup.handup.R;
import com.handup.handup.model.User;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link UserListFragmentInteractionListener}
 * interface.
 */
public class UserFragment extends Fragment {

    private UserListFragmentInteractionListener mListener;

    public RecyclerView mRecyclerView;
    public UserRecyclerViewAdapter mRecyclerViewAdapater;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.setUserFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

            mRecyclerViewAdapater = new UserRecyclerViewAdapter(mListener);

            mRecyclerView.setAdapter(mRecyclerViewAdapater);
        }
        return view;
    }

    public void updateUI(){

        if(mRecyclerView != null){

            mRecyclerViewAdapater.notifyDataSetChanged();
        }
    }

    public void updateUI(User u){

        if(mRecyclerView != null){

            mRecyclerViewAdapater.addItem(u);
            mRecyclerViewAdapater.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserListFragmentInteractionListener) {
            mListener = (UserListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UserListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface UserListFragmentInteractionListener {

        Context getActivityContext();
        void setUserFragment(UserFragment userFragment);
        Activity getActivity();
        ArrayList<Integer> getSubscriptionIDs();
    }
}
