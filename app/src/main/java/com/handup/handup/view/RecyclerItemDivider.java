package com.handup.handup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Christopher on 1/7/2016.  Taken from
 * http://stackoverflow.com/questions/24618829/how-to-add-dividers-and-spaces-between-items-in-recyclerview
 */
public class RecyclerItemDivider extends RecyclerView.ItemDecoration{

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivder;

    /**
     * Constructor that uses the "default divider"
     * @param c Context
     */
    public RecyclerItemDivider(Context c){
        final TypedArray styledAttributes = c.obtainStyledAttributes(ATTRS);
        mDivder = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state){

        int left = parent.getPaddingLeft();
        int right = parent.getRight() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for(int i = 0; i < childCount; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivder.getIntrinsicHeight();

            mDivder.setBounds(left, top, right, bottom);
            mDivder.draw(c);
        }
    }
}
