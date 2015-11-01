package com.gishan.clock.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.gishan.clock.HelperModels.ClockContent;
import com.gishan.clock.R;
import com.gishan.clock.utils.Constants;
import com.gishan.clock.utils.Utils;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class WorldClockService extends Service {
    private ClockContent.ClockItem ukClock,usClock,auClock,nzClock;
    private Timer timeUpdateTimer;

    //Binder object to return to the activity
    private final IBinder binder = new WorldClockBinder();

    //Empty constructor
    public WorldClockService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public class WorldClockBinder extends Binder{

       public void getUpdatedTime(){
                WorldClockService.this.getUpdatedWorldTimes();
        }
    }

    //Returning the clock objects with current time on them
    public void getUpdatedWorldTimes(){
        timeUpdateTimer = new Timer();
        timeUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ArrayList<ClockContent.ClockItem> clocks = new ArrayList<ClockContent.ClockItem>();
                ukClock = new ClockContent.ClockItem(Constants.COUNTRY_ID_UK,getApplicationContext().getString(R.string.uk),Utils.getTimeStrForCountry(Constants.COUNTRY_ID_UK));
                usClock = new ClockContent.ClockItem(Constants.COUNTRY_ID_US,getApplicationContext().getString(R.string.us),Utils.getTimeStrForCountry(Constants.COUNTRY_ID_US));
                auClock = new ClockContent.ClockItem(Constants.COUNTRY_ID_AU,getApplicationContext().getString(R.string.au),Utils.getTimeStrForCountry(Constants.COUNTRY_ID_AU));
                nzClock = new ClockContent.ClockItem(Constants.COUNTRY_ID_NZ,getApplicationContext().getString(R.string.nz),Utils.getTimeStrForCountry(Constants.COUNTRY_ID_NZ));
                clocks.add(ukClock);
                clocks.add(usClock);
                clocks.add(auClock);
                clocks.add(nzClock);
                Intent intent = new Intent("updated times");
                intent.putParcelableArrayListExtra("clocks",clocks);
                sendBroadcast(intent);
            }

        }, 0, 1000);// sending updated times every 1 second
    }

    //Cancelling the timer when activity destroyed
    @Override
    public void onDestroy() {
        super.onDestroy();
        timeUpdateTimer.cancel();
    }
}
