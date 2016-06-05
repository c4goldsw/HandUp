package com.handup.handup.controller.course.content;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handup.handup.R;
import com.handup.handup.controller.main.MainActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;
import com.handup.handup.view.RecyclerItemSpacing;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnContentInteractionListener}
 * interface.
 */
public class ContentFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_SCREEN_DESNITY = "screen-desnity";
    private static final String ARG_SCREEN_WIDTH = "screen-width";

    private int mColumnCount = 1;
    private float mDensity;
    private int mScreenWidth;

    private OnContentInteractionListener mListener;

    public RecyclerView mRecyclerView;
    public MyContentRecyclerViewAdapter mRecyclerViewAdapter;
    public FloatingActionButton mFab;

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

        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);

        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            GridLayoutManager m = new GridLayoutManager(context, mColumnCount);
            mRecyclerView.setLayoutManager(m);
        }

        mRecyclerViewAdapter = new MyContentRecyclerViewAdapter(mScreenWidth, mColumnCount, true, mListener, false);

        mRecyclerView.addItemDecoration(new RecyclerItemSpacing( (int) (8*mDensity), mColumnCount));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.submitContent(v);
            }
        });

        return view;
    }

    public void updateUI(Content c){

        if(mRecyclerViewAdapter != null){

            /*whenever content is added, it is locally included into the recycler view AND uploaded to FB.
            But, due to low memory situations, the activity might be destroyed and reloaded when onActivityResult is called.
            Thus, we'll have to ensure we don't load the same content twice after pulling the dup from FB*/
            if(c.getOwner() == Integer.parseInt(MainActivity.getUser().getUid()))
               mRecyclerViewAdapter.removeItem(MainActivity.getUser().getUid());

            mRecyclerViewAdapter.addItem(c);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentInteractionListener) {
            mListener = (OnContentInteractionListener) context;
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
    public interface OnContentInteractionListener {

        void setContentFragment(ContentFragment contentFragment);
        Activity getActivity();
        void submitContent(final View view);
    }
}
