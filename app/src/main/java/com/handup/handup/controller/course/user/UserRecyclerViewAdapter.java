package com.handup.handup.controller.course.user;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.User;

import java.util.ArrayList;

import static com.handup.handup.helper.MiscFunctions.binarySearchArray;
import static com.handup.handup.helper.MiscFunctions.linearSearchArray;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private ArrayList<User> mValues;
    private final UserFragment.UserListFragmentInteractionListener mListener;

    public UserRecyclerViewAdapter(ArrayList<User> items, UserFragment.UserListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public void addItem(User u){mValues.add(u);}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        holder.mNameView.setText(mValues.get(position).getDisplayName());
        holder.mPointsView.setText("Points: " + mValues.get(position).getPoints());
        holder.uid = mValues.get(position).getUid();

        byte[] profilePictureArray = mValues.get(position).getInAppProfilePicture();

        if (profilePictureArray != null) {

            Bitmap picture = BitmapFactory.decodeByteArray(profilePictureArray, 0,
                    profilePictureArray.length);

            RoundedBitmapDrawable profPic = RoundedBitmapDrawableFactory
                    .create(mListener.getActivityContext().getResources(), picture);
            profPic.setCircular(true);

            holder.mAvatarView.setImageDrawable(profPic);
        }
        else{
            holder.mAvatarView.setImageDrawable(mListener.getActivityContext().getResources()
                    .getDrawable(R.drawable.ic_mood_24dp));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final View mView;
        public final TextView mNameView;
        public final TextView mPointsView;
        public final ImageView mAvatarView;

        //this uid is used to check to see if the app user is subscribed to this person or not
        public String uid;

        public ViewHolder(View view) {
            super(view);

            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mPointsView = (TextView) view.findViewById(R.id.points);
            mAvatarView = (ImageView) view.findViewById(R.id.avatar);

            mAvatarView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //TODO: for some reason, the listener for the entire view isn't called...
            //this would make a good SO post
            mPointsView.setOnClickListener(this);
            mAvatarView.setOnClickListener(this);
            mNameView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPointsView.getText() + "'";
        }

        @Override
        public void onClick(View v) {

            Bundle dialogInfo = new Bundle();

            if(CourseActivity.subscriptionIDs == null){

                return; //Subscription info hasn't been loaded yet
            }
            else if(binarySearchArray(CourseActivity.subscriptionIDs, Integer.parseInt(uid))){

                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, true);
            }else{

                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, false);
            }

            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_UID, uid);
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_NAME, mNameView.getText().toString());

            //Taken from http://tinyurl.com/3xatjj5
            DialogFragment newFragment = new SubscribeDialog();
            newFragment.setArguments(dialogInfo);
            newFragment.show(mListener.getActivity().getFragmentManager(), "SubscribeDialog");
        }
    }
}
