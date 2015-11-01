package com.gishan.clock.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;


public class StopwatchService extends Service {
    //Binder object to return to the activity
    private final IBinder binder = new StopwatchBinder();
    private static long startTime = 0L;
    private static long timeInMilliseconds = 0L;
    private static long timeSwapBuff = 0L;
    private static long updatedTime = 0L;
    private static Handler customHandler = new Handler();
    public static Context context;

    public StopwatchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class StopwatchBinder extends Binder{
        public void init(Context context){
            StopwatchService.this.init(context);
        }

        //Interface methods to for start, stop and reset the clocks
        public void start(){
            StopwatchService.this.start();
        }

        public void stop(){
            StopwatchService.this.stop();
        }


        public void reset(){
            StopwatchService.this.reset();
        }
    }

    //Storing the start time and calling the updateTimerThread using the handler
    public void start(){
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    //Stopping/Pausing the stopwatch and removing the callback to the updateTimerThread
    public void stop(){
        timeSwapBuff +=timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }


    //Resetting the stopwatch and removing the callback to the updateTimerThread
    public void reset(){
        updatedTime=0;
        timeSwapBuff = 0L;
        customHandler.removeCallbacks(updateTimerThread);

    }

    public void init(Context context){
        this.context = context;
    }

    //Thread for the stopwatch timing
    private static Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            //Stopwatch time in miliseconds by finding the difference between the current system time and stopwatch start time
            timeInMilliseconds  = SystemClock.uptimeMillis() - startTime;
            //Stopwatch updated time by adding the time already countered to the above time diff
            updatedTime = timeSwapBuff + timeInMilliseconds;
            //Seconds  = miliseconds/1000
            int seconds = (int)(updatedTime/1000);
            //Mins = miliseconds/(1000*60)
            int minutes = seconds /60;
            //Seconds as a whole number
            seconds = seconds % 60;
            //Remaing are the miliseconds
            int milliseconds = (int) (updatedTime % 1000);

            //Generating the string to display at stopwatch and the string is formatted to 2 digits for each min, sec and milisec
            String timeStr;
            if (milliseconds < 100) {
                timeStr = String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":0" + milliseconds / 10;
            } else {
                timeStr = String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + ":" + String.format("%2d", milliseconds / 10);
            }

            //Sending the broadcast out with the packaged intent
            Intent intent = new Intent("stopwatch time");
            intent.putExtra("timeStr", timeStr);
            intent.putExtra("minutes", minutes);
            StopwatchService.context.sendBroadcast(intent);
            //Calling the updateTimerThread every time using a handler
            customHandler.postDelayed(updateTimerThread, 0);

        }
    };


}
