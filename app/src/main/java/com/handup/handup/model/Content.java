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

    public Content(){

    }

    public Content(Bitmap b){

        setContentBitmap(b);
    }

    public ArrayList<Integer> getApprovals(){
        return approvals;
    }

    public void setApproved(ArrayList<Integer> approvals) {
        this.approvals = approvals;
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


}