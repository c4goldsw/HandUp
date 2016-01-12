package com.handup.handup.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Christopher on 1/5/2016.  Used to determine lecture times and lecture numbers
 * for the purpose of submitting and storing user content.
 */
public class ContentTimeDeterminer {


    /**
     * Gets the lecture times as dates for a course
     * @param time The time represented in the format [DAY][24HOUR], e.g. "W13"
     * @return The date object that coressponds to the time string
     */
    public static Date getTimeFromString(String time){

        char[] buffer = new char[2];
        time.getChars(1, 3, buffer, 0);
        int hour = Integer.parseInt(String.valueOf(buffer));

        //refering to this:
        //http://stackoverflow.com/questions/3300328/add-1-week-to-a-date-which-way-is-preferred
        Calendar lTime = Calendar.getInstance();
        Calendar cTime = Calendar.getInstance();

        /*NOTE: I place everything 24 hours ahead in order to check whether we've passed the submission
        * window, which takes place 24 hours after a lecture */
        if(time.contains("M")){
            lTime.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        }else if(time.contains("T")){
            lTime.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        }else if(time.contains("W")){
            lTime.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        }else if(time.contains("R")){
            lTime.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        }else if(time.contains("F")){
            lTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        }

        lTime.set(Calendar.HOUR_OF_DAY, hour);
        lTime.set(Calendar.MINUTE, 0);
        lTime.set(Calendar.SECOND, 0);

        /* NOTE: we return a time 24 hours before the time we check for, i.e. the lecture time
        * thus the + (6 * 24) and the - (24)*/
        return (cTime.after(lTime)) ? new Date(lTime.getTimeInMillis() + (6 * 24 * 3600 * 1000)) :
                new Date(lTime.getTimeInMillis() - (24 * 3600 * 1000));
    }
}
