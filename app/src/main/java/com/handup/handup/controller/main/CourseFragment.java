package com.handup.handup.controller.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handup.handup.R;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.StateManager;
import com.handup.handup.model.User;
import com.handup.handup.model.fbquery.MainActivityReloadQuery;
import com.handup.handup.view.CourseListAdapter;

/**
 * Used to list courses that users have enrolled in on the app.  Clicking on a course leads to
 * a new activity for that course.
 *
 * Code for recycler view is taken from: http://developer.android.com/training/material/lists-cards.html
 */
public class CourseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //components for recycler view
    private RecyclerView mRecyclerView;
    private CourseListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /*===========================================================================================
    Controller methods
    ===========================================================================================*/

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CourseFragment.
     */
    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void startCourseActivity(int courseID, String courseName){

        Intent i = new Intent(getContext(), CourseActivity.class);
        i.putExtra(Constants.PUT_EXTRA_COURSE_NAME, courseName);
        i.putExtra(Constants.PUT_EXTRA_COURSE_ID, courseID);
        i.putExtra(Constants.PUT_EXTRA_USERNAME, StateManager.getUserName(getContext()));
        i.putExtra(Constants.PUT_EXTRA_DISPLAY_NAME, MainActivity.getMeRequest().getMe().getFirstName() + " "
        + MainActivity.getMeRequest().getMe().getLastName());
        i.putExtra(Constants.PUT_EXTRA_UID, MainActivity.getUser().getUid());

        startActivityForResult(i, Constants.COURSE_ACT_FOR_RESULT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d(Constants.DEBUG_GENERAL, "MainActivity: OnActivityCalled: Request:" + requestCode + ", Result: " + resultCode);

        if(requestCode == Constants.COURSE_ACT_FOR_RESULT){
            new MainActivityReloadQuery(MainActivity.getmActivity()).execute();
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
    public interface OnFragmentInteractionListener {
        void onCourseSelect(Uri uri);
        void setCourseFragment(CourseFragment f);
    }

    /*===========================================================================================
    View (UI) methods
    ===========================================================================================*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener.setCourseFragment(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ui = inflater.inflate(R.layout.fragment_course, container, false);

        //set up the recycler view
        mRecyclerView = (RecyclerView) ui.findViewById(R.id.fragment_course_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //mRecyclerView.addItemDecoration(new RecyclerItemDivider(getActivity()));

        mAdapter = new CourseListAdapter(MainActivity.getCourses(), this);
        mRecyclerView.setAdapter(mAdapter);

        updateUI();

        return ui;
    }

    public void updateUI(){

        User user = MainActivity.getUser();

        //check to see if anythign if the user is null OR if we haven't switched to the activity yet
        if(user == null || mRecyclerView == null )
            return;

        mAdapter.setCourseNames(MainActivity.getCourses());
        mAdapter.notifyDataSetChanged();
    }
}
