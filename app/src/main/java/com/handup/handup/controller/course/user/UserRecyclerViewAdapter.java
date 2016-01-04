package com.handup.handup.controller.course.user;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.handup.handup.R;
import com.handup.handup.controller.course.user.UserDisplayContent.UserDisplayItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UserDisplayItem} and makes a call to the
 * specified {@link UserFragment.OnListFragmentInteractionListener}.
 */
public class UserRecyclerViewAdapter extends RecyclerView.Adapter<UserRecyclerViewAdapter.ViewHolder> {

    private final List<UserDisplayItem> mValues;
    private final UserFragment.OnListFragmentInteractionListener mListener;

    public UserRecyclerViewAdapter(List<UserDisplayItem> items, UserFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).id);
        holder.mPointsView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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

        public UserDisplayItem mItem;

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
