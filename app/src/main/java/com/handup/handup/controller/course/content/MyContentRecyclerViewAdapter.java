package com.handup.handup.controller.course.content;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.handup.handup.R;
import com.handup.handup.controller.course.content.ContentFragment.OnContentFragmentInteractionListener;
import com.handup.handup.model.Content;

import java.util.List;

public class MyContentRecyclerViewAdapter extends RecyclerView.Adapter<MyContentRecyclerViewAdapter.ViewHolder> {

    private List<Content> mValues;
    private int mScreenWidth;
    private int mColumnCount;

    public MyContentRecyclerViewAdapter(List<Content> items, int mScreenWidth, int mColumnCount) {
        mValues = items;
        this.mScreenWidth = mScreenWidth;
        this.mColumnCount = mColumnCount;
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

        //set the height of each item to be equal to it's width
        holder.mImageView.getLayoutParams().height = (mScreenWidth - (4 + 4*mColumnCount)) /mColumnCount;
        holder.mImageView.getLayoutParams().width = mScreenWidth - 16;

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        public CardView mCardView;
        public ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.content_image);
        }
    }
}
