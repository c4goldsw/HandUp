package com.handup.handup.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Christopher on 1/16/2016. Deived from
 * http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 */
public class RecyclerItemSpacing extends RecyclerView.ItemDecoration {

    private int dpSpacing;
    private int columnCount;

    public RecyclerItemSpacing(int dpSpacing, int columnCount){

        this.columnCount = columnCount;

        //convert dp to pixels
        this.dpSpacing = dpSpacing;
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

        //add spacing to the top
        if(parent.getChildLayoutPosition(view) < columnCount ){
            outRect.top = dpSpacing;
        }else{
            outRect.top = dpSpacing/2;
        }

        //add spacing to the right
        if(parent.getChildLayoutPosition(view) % columnCount == columnCount - 1 ){
            outRect.right = dpSpacing;
        }else{
            outRect.right = dpSpacing/2;
        }

        //add spacing to the left
        if(parent.getChildLayoutPosition(view) % columnCount == 0 ){
            outRect.left = dpSpacing;
        }else{
            outRect.left = dpSpacing/2;
        }

        //add spacing to the bottom
        int numberOfRows = (int) Math.ceil((double) parent.getAdapter().getItemCount() / columnCount);
        if( parent.getChildLayoutPosition(view) + 1> ((numberOfRows - 1) * (columnCount))){
            outRect.bottom = dpSpacing;
        }
        else{
            outRect.bottom = dpSpacing/2;
        }
    }
}
