package com.gishan.clock.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gishan.clock.HelperModels.ClockContent;
import com.gishan.clock.R;

import java.util.ArrayList;

/**
 * Created by Gishan on 24/02/15.
 */


//List adapter for the world clock list view
public class WorldClockAdapter extends ArrayAdapter<ClockContent.ClockItem> {

    Activity activity;
    ArrayList<ClockContent.ClockItem> items = null;
    int resourceId;

    //Construct to initialise variables
    public WorldClockAdapter(Activity activity,int resourceId,ArrayList<ClockContent.ClockItem> items){
        super(activity,resourceId,items);
            this.activity = activity;
            this.items = items;
            this.resourceId = resourceId;

    }

    //Initialising the view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);
        }
        ClockContent.ClockItem item = items.get(position);
        TextView tvCountry = (TextView) convertView.findViewById(R.id.tv_country);
        TextView tvClock = (TextView) convertView.findViewById(R.id.tv_clock);
        tvClock.setText(item.getTime());
        tvCountry.setText(item.getCountry());

        return convertView;
    }


}
