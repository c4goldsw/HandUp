package com.handup.handup.helper;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import java.io.IOException;

/**
 * Created by Christopher on 1/2/2016.  General or miscellaneous methods
 */
public class General {

    /**
     * Images aren't automatically rotated, so that is done here.
     * @param selectedImage uri of the bitmap to rotate (if nessescary)
     * @param a
     * @return the bitmap in a portrait orientation
     */
    public static Bitmap getPortraitImage(Uri selectedImage, FragmentActivity a) throws
            IOException{

         /*Taken from http://bit.ly/1JQ79b1 on SO*/
        Bitmap firstImage = android.provider.MediaStore.Images.Media.getBitmap(
                a.getContentResolver(), selectedImage);

        //from http://bit.ly/1ZGNnYq
        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
        Cursor cur = a.getContentResolver().
                query(selectedImage, orientationColumn, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        return Bitmap.createBitmap(firstImage, 0, 0, firstImage.getWidth(), firstImage.getHeight()
        , matrix, true);
    }

}
