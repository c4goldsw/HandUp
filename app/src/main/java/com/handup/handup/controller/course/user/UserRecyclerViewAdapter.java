package com.handup.handup.controller.course.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.model.fbquery.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserDisplayItem} and makes a call to the
 * specified {@link UserFragment.UserListFragmentInteractionListener}.
 */
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

        byte[] profilePictureArray = mValues.get(position).getInAppProfilePicture();

        if (profilePictureArray != null) {

            Bitmap picture = BitmapFactory.decodeByteArray(profilePictureArray, 0,
                    profilePictureArray.length);

            RoundedBitmapDrawable profPic = RoundedBitmapDrawableFactory
                    .create(mListener.getActivityContext().getResources(), picture);
            profPic.setCircular(true);

            holder.mAvatarView.setImageDrawable(profPic);
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mNameView;
        public final TextView mPointsView;
        public final ImageView mAvatarView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name);
            mPointsView = (TextView) view.findViewById(R.id.points);
            mAvatarView = (ImageView) view.findViewById(R.id.avatar);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPointsView.getText() + "'";
        }
    }
}
