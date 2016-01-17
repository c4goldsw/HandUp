package com.handup.handup.controller.course.content;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.handup.handup.R;
import com.handup.handup.controller.course.content.ContentFragment.OnContentFragmentInteractionListener;
import com.handup.handup.model.Content;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnContentFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyContentRecyclerViewAdapter extends RecyclerView.Adapter<MyContentRecyclerViewAdapter.ViewHolder> {

    private List<Content> mValues;
    private final OnContentFragmentInteractionListener mListener;

    public MyContentRecyclerViewAdapter(List<Content> items, OnContentFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Bitmap content = mValues.get(position).getContentBitmap();
        holder.mImageView.setImageBitmap(content);

        holder.mView.getLayoutParams().height = holder.mView.getLayoutParams().width;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                //mListener.setUserFragment(holder.mItem);
            }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addItem(Content content){

        mValues.add(content);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.content_image);
        }
    }
}
