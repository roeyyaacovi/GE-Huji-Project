package com.Avengers.app.Security;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeSlot {
    private String strStartTime;
    private long longStartTime;
    private long nanoSecondsResponseTime;


    TimeSlot(String startTime, String responseTime){
        this.strStartTime = startTime;
        this.nanoSecondsResponseTime = Integer.parseInt(responseTime);
    }

    public int init(){
        convertStringToDate();

        return 0;
    }

    private long getNanoSecondsResponseTime(){
        return nanoSecondsResponseTime;
    }

    private int convertStringToDate(){
        Calendar calendar = Calendar.getInstance();
        Pattern datePattern = Pattern.compile("([0-9]*?)-([0-9]*?)-([0-9]*)T(.*)");

        Matcher matcher = datePattern.matcher(strStartTime);

        if(!matcher.find()){
            return 0;
        }

        int year  = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int day   = Integer.parseInt(matcher.group(3));

        String time = matcher.group(4);

        Pattern timePattern = Pattern.compile("([0-9]*?):([0-9]*?):([0-9]*).([0-9]*).*");

        matcher = timePattern.matcher(time);

        if(!matcher.find()){
            return 0;
        }

        int hour  = Integer.parseInt(matcher.group(1));
        int minute = Integer.parseInt(matcher.group(2));
        int second   = Integer.parseInt(matcher.group(3));
        int milliSeconds = Integer.parseInt(matcher.group(4))*100;

        calendar.set(year, month, day, hour, minute, second);

        Date startTimeDate = calendar.getTime();

        longStartTime = startTimeDate.getTime() + milliSeconds;

        return 0;
    }

    private long getStartTimeDate() {
        return longStartTime;
    }


    /**
     * Checks if the given time slot (other) is inside the current time slot (this)
     * @param other The time slot to check
     * @return True if inside false if not
     */
    boolean isInsideTimeSlot(TimeSlot other){
        double differenceNanoSeconds = 0;

        if(other.getStartTimeDate() < this.getStartTimeDate()){
            return false;
        }

        differenceNanoSeconds = (other.getStartTimeDate() - this.getStartTimeDate())*java.lang.Math.pow(10,6);

        return getNanoSecondsResponseTime() > (differenceNanoSeconds + other.getNanoSecondsResponseTime());

    }

}
