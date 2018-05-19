package com.Avengers.app.UAA;

public class My_Time {
    private int hour;
    private int minute;
    private double second;
    private static final double TO_SECONDS = 60;

    public My_Time(int h, int mi, double s) {
        hour = h;
        minute = mi;
        second = s;
    }

    public int getHour() {
        return hour;
    }
    public int getMinute(){
        return minute;
    }

    public double getSecond() {
        return second;
    }

    public Double time_difference(My_Time other)
    {
        return (other.getSecond() - second) + ((other.getMinute() - minute) * TO_SECONDS) + ((other.getHour() - hour) *TO_SECONDS*TO_SECONDS);
    }

    public String toString()
    {
        return hour + ":" + minute + ":" + second;
    }
}
