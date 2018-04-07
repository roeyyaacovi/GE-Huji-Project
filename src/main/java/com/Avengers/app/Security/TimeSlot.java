package com.Avengers.app.Security;

public class TimeSlot {
    private int startTime;
    private int responseTime;

    public TimeSlot(int startTime, int responseTime){
        this.startTime = startTime;
        this.responseTime = responseTime;
    }

    public boolean isInsideTimeSlot(TimeSlot other){
        if(other.startTime < this.startTime){
            return false;
        }

        return (other.startTime + other.responseTime) < (this.startTime + this.responseTime);
    }
}
