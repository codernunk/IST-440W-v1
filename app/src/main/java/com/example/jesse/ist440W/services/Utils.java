package com.example.jesse.ist440W.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains a list of useful functions.
 * Created by Jesse on 10/31/2015.
 */
public class Utils {

    public static String formatTime(int seconds){
        long minute = (seconds / 60) % 60;
        long hour = (seconds / 3600);
        long displaySeconds = seconds % 60;

        StringBuilder result = new StringBuilder();

        if (hour == 1)
            result.append(hour + " hour ");
        else if (hour > 0)
            result.append(hour + " hours ");

        if (minute == 1)
            result.append(minute + " minute ");
        else if (minute > 0)
            result.append(minute + " minutes ");

        if (displaySeconds == 1)
            result.append(displaySeconds + " second");
        else if (displaySeconds > 0)
            result.append(displaySeconds + " seconds");

        return result.toString();
    }

    public static String formatQuantity(float quantity){
        if (quantity - (int)quantity > 0){
            return Float.toString(quantity);
        }

        return Integer.toString((int)quantity);
    }
}
