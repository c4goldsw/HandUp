package com.handup.handup.controller.course.content;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.handup.handup.R;
import com.handup.handup.controller.course.CourseActivity;
import com.handup.handup.helper.Constants;
import com.handup.handup.model.Content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.handup.handup.helper.MiscFunctions;

public class MyContentRecyclerViewAdapter extends RecyclerView.Adapter<MyContentRecyclerViewAdapter.ViewHolder> {

    private List<Content> mValues;
    private int mScreenWidth;
    private int mColumnCount;
    private boolean enableVoting;
    private boolean isForContentDisplay;

    private final ContentFragment.OnContentInteractionListener mListener;

    private ArrayList<ValueEventListener> resultHandlerArray = new ArrayList<>();
    private ArrayList<Firebase> refArray = new ArrayList<>();

    public MyContentRecyclerViewAdapter(int mScreenWidth, int mColumnCount,
        boolean enableVoting, ContentFragment.OnContentInteractionListener mListener,
        boolean isForContentDisplay) {

        mValues = Collections.synchronizedList(new ArrayList<Content>());
        this.mScreenWidth = mScreenWidth;
        this.mColumnCount = mColumnCount;
        this.enableVoting = enableVoting;
        this.mListener = mListener;
        this.isForContentDisplay = isForContentDisplay;
    }

    public void removeItem(String uidString){
        int uid = Integer.parseInt(uidString);

        Iterator i = mValues.iterator();

        while(i.hasNext()) {
            Content c = (Content) i.next();
            if (c.getOwner() == uid) {
                i.remove();
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void addItem(Content content){
        mValues.add(content);
    }

    /**
     * This method is used to remove listener relationships from FB references to ensure
     * GC occurs
     */
    public void emptyFbLists(){
        for(int i = 0; i < refArray.size(); ++i){
            refArray.get(i).removeEventListener(resultHandlerArray.get(i));
        }
    }

    public void removeApproval(int owner, int uid){

        for (Content c : mValues) {
            if (c.getOwner() == owner) {

                c.removeApproval(uid);
            }
        }
    }

    public void addApproval(int owner, int uid){

        for (Content c : mValues) {
            if (c.getOwner() == owner) {

                c.addApproval(uid);
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
        holder.cotentOwnerName = mValues.get(position).getDescription();
        if(!isForContentDisplay) {
            holder.approvalDescription = mValues.get(position).getApprovalCount() +
                ((mValues.get(position).getApprovalCount() == 1) ? " Approve" : " Approves");

            holder.approveChangeRef = new Firebase(Constants.FIRE_BASE_URL + "/content/" +
            mValues.get(position).getOwner() + "/" + CourseActivity.getCourseID() + "/lastContent/approvals");
            holder.vel = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue() == null)
                        holder.mTextView.setText(holder.cotentOwnerName + ", " +"0 Approves");


                    holder.mTextView.setText(holder.cotentOwnerName + ", " + dataSnapshot.getChildrenCount()
                            + ((dataSnapshot.getChildrenCount() == 1) ? " Approve" : " Approves"));
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            };
            holder.approveChangeRef.addValueEventListener(holder.vel);

            //add the listener and ref into the two direct-access arrays
            refArray.add(holder.approveChangeRef);
            resultHandlerArray.add(holder.vel);

            holder.mTextView.setText(holder.cotentOwnerName + ", " + holder.approvalDescription);
        }
        else{
            holder.mTextView.setText(holder.cotentOwnerName);
        }


        //set the height of each item to be equal to it's width
        holder.mImageView.getLayoutParams().height = (mScreenWidth - (4 + 4*mColumnCount)) /mColumnCount;
        holder.mImageView.getLayoutParams().width = mScreenWidth - 16;
    }

    @Override
    public void onViewRecycled(ViewHolder holder){

        holder.approveChangeRef.removeEventListener(holder.vel);

        refArray.remove(holder.approveChangeRef);
        resultHandlerArray.remove(holder.vel);

        holder.vel = null;
        holder.approveChangeRef = null;
        holder.mImageView.setImageBitmap(null);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final View mView;
        public ImageView mImageView;
        public TextView mTextView;

        public Firebase approveChangeRef;
        public ValueEventListener vel;

        public String cotentOwnerName = "";
        public String approvalDescription = "";

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

            if(MiscFunctions.linearSearchArray(mValues.get(getAdapterPosition()).getApprovals(),
                    Integer.parseInt(CourseActivity.getUid()))){

                dialogInfo.putSerializable(Constants.DIALOG_BUNDLE_BOOL_VAL, true);
            }else{

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
