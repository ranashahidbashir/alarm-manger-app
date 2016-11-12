package com.alarm.manger.util;

import java.io.Serializable;

/**
 * Created by Rana Shahid Bashir on 10/29/2016.
 */
public class AlarmInfo implements Serializable {
    private int id;

    private String status;
    
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int sec;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minet) {
        this.minute = minet;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }


    public String getDate() {
        return (month+1)+"/"+day+"/"+year;
    }

    /*public void setDate(String date) {
        this.date = date;
    }*/

    public String getTime() {

        String timeSet = "";
        int h = hour;
        if (hour > 12) {
            h -= 12;
            timeSet = "PM";
        } else if (hour == 0) {
            h += 12;
            timeSet = "AM";
        } else if (hour == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (minute < 10)
            minutes = "0" + minute;
        else
            minutes = String.valueOf(minute);
        return h+":"+minutes+" "+timeSet;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
