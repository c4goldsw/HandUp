package com.handup.handup.helper;

import com.squareup.picasso.Picasso;

/**
 * Created by Christopher on 1/5/2016.
 */
public class LevelPicker {

    public static int levelPicker(int points){

        if(0 <= points || points < 100)
            return 1;
        else
            return 2;
    }
}
