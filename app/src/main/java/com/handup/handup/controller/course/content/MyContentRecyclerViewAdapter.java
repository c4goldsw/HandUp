package com.handup.handup.controller.course.content;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.controller.course.content.ContentFragment.OnContentFragmentInteractionListener;
import com.handup.handup.controller.course.user.SubscribeDialog;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;

import java.util.List;

import static com.handup.handup.helper.MiscFunctions.binarySearchArray;
import static com.handup.handup.helper.MiscFunctions.linearSearchArray;

public class MyContentRecyclerViewAdapter extends RecyclerView.Adapter<MyContentRecyclerViewAdapter.ViewHolder> {

    private List<Content> mValues;
    private int mScreenWidth;
    private int mColumnCount;
    private boolean enableVoting;

    public MyContentRecyclerViewAdapter(List<Content> items, int mScreenWidth, int mColumnCount,
        boolean enableVoting) {

        mValues = items;
        this.mScreenWidth = mScreenWidth;
        this.mColumnCount = mColumnCount;
        this.enableVoting = enableVoting;
    }

    public void removeItem(String uidString){
        int uid = Integer.parseInt(uidString);

        for(Content c : mValues){

            Log.d(Constants.DEBUG_GENERAL, "uid: " + uid + ". Owner: " + c.getOwner());
            if(c.getOwner() ==  uid){
                mValues.remove(c);
                notifyDataSetChanged();
            }
        }
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
        holder.mTextView.setText(mValues.get(position).getContentDescription());

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public CardView mCardView;
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.content_image);
            mTextView = (TextView) view.findViewById(R.id.content_card_text);
        }

        @Override
        public void onClick(View v) {

            /*if(!enableVoting){
                return;
            }

            //TODO: Get nessescary local variables associated with this view, pass to dialog

            Bundle dialogInfo = new Bundle();

            if(binarySearchArray(CourseActivity.subscriptionIDs, /*TODO: Our UID)){

                Log.d(Constants.DEBUG_GENERAL, "Approved");
                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, true);
            }else{

                Log.d(Constants.DEBUG_GENERAL, "Not approved " /* TODO: oid ...+ uid);
                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, false);
            }

            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_UID, uid);
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_NAME, mNameView.getText().toString());

            //Taken from http://tinyurl.com/3xatjj5
            DialogFragment newFragment = new SubscribeDialog();
            newFragment.setArguments(dialogInfo);
            newFragment.show(mListener.getActivity().getFragmentManager(), "SubscribeDialog");*/
        }
    }
}
