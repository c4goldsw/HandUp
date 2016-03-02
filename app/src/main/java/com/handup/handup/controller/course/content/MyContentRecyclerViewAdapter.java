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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.R;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.controller.course.user.SubscribeDialog;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;

import java.util.List;

import static com.handup.handup.helper.MiscFunctions.linearSearchArray;

public class MyContentRecyclerViewAdapter extends RecyclerView.Adapter<MyContentRecyclerViewAdapter.ViewHolder> {

    private List<Content> mValues;
    private int mScreenWidth;
    private int mColumnCount;
    private boolean enableVoting;

    private final ContentFragment.OnContentInteractionListener mListener;

    public MyContentRecyclerViewAdapter(List<Content> items, int mScreenWidth, int mColumnCount,
        boolean enableVoting, ContentFragment.OnContentInteractionListener mListener) {

        mValues = items;
        this.mScreenWidth = mScreenWidth;
        this.mColumnCount = mColumnCount;
        this.enableVoting = enableVoting;
        this.mListener = mListener;
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

    public void addItem(Content content){

        mValues.add(content);
    }

    public void removeApproval(int owner, int uid){
        for(Content c : mValues){
            if(c.getOwner() == owner){

                c.removeApproval(uid);
            }
        }
    }

    public void addApproval(int owner, int uid){

        Log.d(Constants.DEBUG_GENERAL, "Attemping to add an approve" );
        for(Content c : mValues){
            if(c.getOwner() == owner){

                c.addApproval(uid);

                Log.d(Constants.DEBUG_GENERAL, "Approve added");

                for(Integer i : c.getApprovals()){
                    Log.d(Constants.DEBUG_GENERAL, "Approver: " + i);
                }
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
        holder.contentDescription1 = mValues.get(position).getDescription();
        holder.contentDescription2 = Integer.toString(mValues.get(position).getApprovalCount());
        holder.mTextView.setText(holder.contentDescription1 + ", " + holder.contentDescription2);

        Firebase approveChangeRef = new Firebase(Constants.FIRE_BASE_URL + "/content/" +
        mValues.get(position).getOwner() + "/" + CourseActivity.getCourseID() + "/lastContent/approvals");

        approveChangeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null)
                    return;

                holder.mTextView.setText(holder.contentDescription1 + ", " + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public CardView mCardView;
        public ImageView mImageView;
        public TextView mTextView;

        public String contentDescription1;
        public String contentDescription2;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.content_image);
            mTextView = (TextView) view.findViewById(R.id.content_card_text);

            mView.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if(!enableVoting){
                return;
            }

            Bundle dialogInfo = new Bundle();

            //TODO: check to see if b-Search is safe is safe (i.e. list is sorted)
            if(linearSearchArray(mValues.get(getAdapterPosition()).getApprovals(),
                    Integer.parseInt(CourseActivity.getUid()))){

                Log.d(Constants.DEBUG_GENERAL, "Approved");
                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, true);
            }else{

                Log.d(Constants.DEBUG_GENERAL, "Not approved");
                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, false);
            }

            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_TITLE, "APPROVE");
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_UID, CourseActivity.getUid());
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_OID, Integer.toString(
                    mValues.get(getAdapterPosition()).getOwner()));
            dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_COURSE_ID,
                    Integer.toString(CourseActivity.getCourseID()));

            //Taken from http://tinyurl.com/3xatjj5
            DialogFragment newFragment = new ApproveDialog();
            newFragment.setArguments(dialogInfo);
            newFragment.show(mListener.getActivity().getFragmentManager(), "ApproveDialog");
        }
    }
}
