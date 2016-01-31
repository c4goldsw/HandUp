package com.handup.handup.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Christopher on 1/13/2016.
 */
public class Content {

    private Bitmap contentBitmap;

    private String image;

    private boolean approved;

    public Content(){

    }

    public Content(Bitmap b){

        setContentBitmap(b);
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public void setApproved(String approved)
    {
        if(approved.equals("true"))
            setApproved(true);
        else if(approved.equals("false"))
            setApproved(false);
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