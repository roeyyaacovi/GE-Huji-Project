package com.Avengers.app.MachineLearning.FeatureExtraction;

import java.math.BigInteger;

public class IpAnalyze {
    private String ip;
    private BigInteger lastTimeStamp;
    private int numberOfRequests;
    private double avgBetweenRequests;
    IpAnalyze(String ip, BigInteger lastTimeStamp, int numberOfRequests, double avgBetweenRequests){
        this.ip = ip;
        this.lastTimeStamp = lastTimeStamp;
        this.numberOfRequests = numberOfRequests;
        this.avgBetweenRequests = avgBetweenRequests;
    }
    void printData()
    {
        System.out.println(this.ip + " " + this.lastTimeStamp + " " + this.numberOfRequests + " " + this.avgBetweenRequests);
    }

    String getIp()
    {
        return this.ip;
    }

    BigInteger getLastTimeStamp()
    {
        return this.lastTimeStamp;
    }

    int getNumberOfRequests()
    {
        return this.numberOfRequests;
    }

    double getAvgBetweenRequests()
    {
        return this.avgBetweenRequests;
    }

    void setLastTimeStamp(BigInteger newLastTimeStamp)
    {
        this.lastTimeStamp = newLastTimeStamp;
    }

    void setNumberOfRequests(int newNumberOfRequests)
    {
        this.numberOfRequests = newNumberOfRequests;
    }

    void setAvgBetweenRequests(double newAvgBetweenRequests)
    {
        this.avgBetweenRequests = newAvgBetweenRequests;
    }
}
