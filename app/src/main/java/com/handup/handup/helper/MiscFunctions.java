package com.handup.handup.helper;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Christopher on 2/27/2016.  Just contains miscellaneous helper functions
 */
public class MiscFunctions {

    //Used for unsorted arrays
    public static <T extends Comparable> boolean linearSearchArray(ArrayList<T> arr, T value){

        for(T obj : arr){
            if(obj.compareTo(value) == 0) {
                return true;
            }
        }

        return false;
    }

    //Used for unsorted arrays
    public static <T extends Comparable> boolean binarySearchArray(ArrayList<T> arr, T value){

        return (binarySearchArray(arr, value, 0, arr.size() / 2, arr.size() - 1)) ? true : false;
    }

    private static <T extends Comparable> boolean binarySearchArray(ArrayList<T> arr, T value, int start, int middle,
         int end){


        if(end < start){

            return false;
        }
        else if(arr.get(middle).compareTo(value) == 0 ){

            return true;
        } else if(arr.get(middle).compareTo(value) == -1 ){

            start = middle + 1;
            middle = (int) Math.floor((start + end) / 2);
            return binarySearchArray(arr, value, start, middle, end);

        } else {

            end = middle - 1;
            middle = (int) Math.floor((start + end) / 2);
            return binarySearchArray(arr, value, start, middle, end);
        }
    }

}
