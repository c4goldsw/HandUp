package com.handup.handup.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.util.ArrayList;

/**
 * Created by Christopher on 1/13/2016.
 */
public class Content {

    private Bitmap contentBitmap;
    private String image;
    private ArrayList<Integer> approvals;
    private String contentDescription;
    private int approvalCount;
    private int owner;

    /* ======================================================================
        Methods
       ======================================================================*/

    public Content(){

    }

    public Content(Bitmap b){

        setContentBitmap(b);
    }

    public int getOwner(){
        return owner;
    }

    public void setOwner(int uid){
        owner = uid;
    }

    public int getApprovalCount(){
        return approvalCount;
    }

    public ArrayList<Integer> getApprovals(){
        return approvals;
    }

    public void setApproved(ArrayList<Integer> approvals) {
        this.approvals = approvals;

        approvalCount = this.approvals.size();
    }

    public Bitmap getContentBitmap() {
        return contentBitmap;
    }

    public void setContentBitmap(Bitmap contentBitmap) {
        this.contentBitmap = contentBitmap;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {

        this.image = image;
        byte[] imageArray = Base64.decode(image, Base64.DEFAULT);

        contentBitmap = BitmapFactory.decodeByteArray(imageArray, 0,
                imageArray.length);
    }

    public void setContentDescription(String description){
        contentDescription = description;
    }

    public String getContentDescription(){

        return  contentDescription;
    }
}