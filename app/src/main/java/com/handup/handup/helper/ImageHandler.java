package com.handup.handup.helper;

import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Christopher on 1/2/2016.  ImageHandler or miscellaneous methods
 */
public class ImageHandler {

    /**
     * Images aren't automatically rotated, so that is done here.
     * @param selectedImage uri of the bitmap to rotate (if nessescary)
     * @param a
     * @return the bitmap in a portrait orientation
     */
    public static Bitmap getPortraitImage(Uri selectedImage, FragmentActivity a, int width
    , int height) throws
            IOException{

        Bitmap firstImage = decodeSampledBitmap(getPath(selectedImage, a), width, height);

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

    /**
     * Taken from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html.
     * Gets a smaller version of an image for displaying on our app.
     * @param reqWidth the minimum width of the image
     * @param reqHeight the minimum height
     * @return Smaller bitmap
     */
    public static Bitmap decodeSampledBitmap(String filePath, int reqWidth, int reqHeight){

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqWidth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Taken from http://developer.android.com/training/displaying-bitmaps/load-bitmap.html.
     * Used to get the "sample size" of an image based off how we want it to appear in our UI
     * @param options Bitmap factory options, containing size of image
     * @param reqWidth The width in pixels that we want our image to be close as possible to
     * @param reqHeight The height in pixels that we want our image to be close as possible to
     * @return The sample size that we'll use
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * Taken from http://stackoverflow.com/questions/6935497/android-uploading-image
     * @param uri Uri we want to convert to stringe
     * @return
     */
    public static String getPath(Uri uri, FragmentActivity a) {
        String[] projection = { MediaStore.Images.Media.DATA };
        CursorLoader loadercursor = new CursorLoader(a, uri, projection, null, null, null);
        Cursor cursor = loadercursor.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static String getImageString(Bitmap finalImage){

        //http://stackoverflow.com/questions/26292969/can-i-store-image-files-in-firebase-using-java-api
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
    }
}
