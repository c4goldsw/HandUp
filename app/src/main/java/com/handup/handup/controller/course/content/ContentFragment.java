package com.handup.handup.controller.course.content;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handup.handup.R;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;
import com.handup.handup.view.RecyclerItemSpacing;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnContentFragmentInteractionListener}
 * interface.
 */
public class ContentFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_SCREEN_DESNITY = "screen-desnity";
    private static final String ARG_SCREEN_WIDTH = "screen-width";

    private int mColumnCount = 1;
    private float mDensity;
    private int mScreenWidth;

    private OnContentFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    public MyContentRecyclerViewAdapter mRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContentFragment() {
    }

    public static ContentFragment newInstance(float dpWidth, float density){
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_SCREEN_WIDTH, (int) (dpWidth*density));
        args.putInt(ARG_COLUMN_COUNT, /*(int) (dpWidth / Constants.ContentListBaseSize)*/ 1);
        args.putFloat(ARG_SCREEN_DESNITY, density);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListener.setContentFragment(this);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mDensity     = getArguments().getFloat(ARG_SCREEN_DESNITY);
            mScreenWidth = getArguments().getInt(ARG_SCREEN_WIDTH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;

            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                GridLayoutManager m = new GridLayoutManager(context, mColumnCount);
                mRecyclerView.setLayoutManager(m);
            }

            mRecyclerViewAdapter = new MyContentRecyclerViewAdapter
                    (new ArrayList<Content>(), mScreenWidth, mColumnCount, true);

            mRecyclerView.addItemDecoration(new RecyclerItemSpacing( (int) (8*mDensity), mColumnCount));
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        }

        return view;
    }

    public void updateUI(Content c){

        if(mRecyclerViewAdapter != null){

            mRecyclerViewAdapter.addItem(c);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentFragmentInteractionListener) {
            mListener = (OnContentFragmentInteractionListener) context;
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
    public interface OnContentFragmentInteractionListener {

        void setContentFragment(ContentFragment contentFragment);
    }
}
