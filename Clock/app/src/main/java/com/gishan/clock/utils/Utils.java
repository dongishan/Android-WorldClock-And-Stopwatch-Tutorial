package com.gishan.clock.utils;

import android.app.Activity;

import com.gishan.clock.HelperModels.ClockContent;
import com.gishan.clock.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Gishan on 24/02/15.
 */
public class Utils {


    public static String getTimeStrForCountry (int countryId){
        Calendar ukTimeNow = Calendar.getInstance();
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("MMMM dd");


        switch (countryId){
            case Constants.COUNTRY_ID_UK:
                return sdfTime.format(ukTimeNow.getTime())+" "+sdfDate.format(ukTimeNow.getTime());
            case Constants.COUNTRY_ID_US:
                Calendar usTimeNow = Utils.plusMinusHours(ukTimeNow,-5);
                return sdfTime.format(usTimeNow.getTime())+" "+sdfDate.format(usTimeNow.getTime());
            case Constants.COUNTRY_ID_AU:
                Calendar auTimeNow = Utils.plusMinusHours(ukTimeNow,11);
                return sdfTime.format(auTimeNow.getTime())+" "+sdfDate.format(auTimeNow.getTime());
              case Constants.COUNTRY_ID_NZ:
                  Calendar nzTimeNow = Utils.plusMinusHours(ukTimeNow,13);
                  return sdfTime.format(nzTimeNow.getTime())+" "+sdfDate.format(nzTimeNow.getTime());

            default:
                break;
        }
        return "";
    }

    public static Calendar plusMinusHours(Calendar now, int hours){
        now.add(Calendar.HOUR, hours);
        return now;
    }
}
