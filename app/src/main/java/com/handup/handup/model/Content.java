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

    private String description;
    private String additionalText;

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

    public void removeApproval(int uid){
        approvals.remove(new Integer(uid));
        contentDescription = description + ", " +
                getApprovalCount() + ((getApprovalCount() == 1) ? " Approve":" Approves");
    }

    public void addApproval(int uid){
        approvals.add(new Integer(uid));
        contentDescription = description + ", " +
                getApprovalCount() + ((getApprovalCount() == 1) ? " Approve":" Approves");
    }

    public int getApprovalCount(){
        return approvals.size();
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

    public void setContentDescription(String description, String additionalText){

        this.description = description;
        this.additionalText = additionalText;
        contentDescription = description + ", " + additionalText;
    }

    public String getContentDescription(){

        return  contentDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionalText() {
        return additionalText;
    }

    public void setAdditionalText(String additionalText) {
        this.additionalText = additionalText;
    }
}