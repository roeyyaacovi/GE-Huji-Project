package com.Avengers.app.UAA;

public class Features_Vector {
    private int max_num_fails;
    private int min_num_success;
    private double min_avg_differences;
    private String label;

    public Features_Vector(int max_num_fails, int min_num_success, double min_avg_differences, String label )
    {
        this.max_num_fails = max_num_fails;
        this.min_num_success = min_num_success;
        this.min_avg_differences = min_avg_differences;
        this.label = label;
    }

    public int getMax_num_fails()
    {
        return max_num_fails;
    }

    public int getMin_num_success()
    {
        return min_num_success;
    }

    public double getMin_avg_differences()
    {
        return min_avg_differences;
    }

    public String getLabel(){return label;}

    public String toString(){
        return Integer.toString(max_num_fails) + "," + Integer.toString(min_num_success) + ","  + Double.toString(min_avg_differences) +" "+ label;
    }
}

