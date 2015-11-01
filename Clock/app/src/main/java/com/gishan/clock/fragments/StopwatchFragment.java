package com.gishan.clock.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gishan.clock.R;
import com.gishan.clock.services.StopwatchService;

public class StopwatchFragment extends Fragment implements  View.OnClickListener{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private Button btnStart,btnReset,btnStop;
    private static TextView tvClock;
    private LinearLayout lyButtons;
    private StopwatchService.StopwatchBinder stopWatchService = null;
    private int STOPWATCH_STATE = 0;
    private final int STATE_START = 1,STATE_STOP = 2,STATE_RESET = 3;
    private final String STOP_WATCH_STATE_STORE = "STOP_WATCH_STATE_STORE";
    private final String STOP_WATCH_TEXT = "STOP_WATCH_TEXT";

    public static StopwatchFragment newInstance(int sectionId) {
        StopwatchFragment fragment = new StopwatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER,sectionId);
        fragment.setArguments(args);
        return fragment;
    }

    //Empty constructor required
    public StopwatchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(
                "stopwatch time");
        setRetainInstance(true);
        getActivity().getApplicationContext().registerReceiver(stopwatchReceiver, filter);
        getActivity().getApplicationContext().bindService(new Intent(getActivity(), StopwatchService.class),
                stopWatchServiceConnection, Context.BIND_AUTO_CREATE);


    }

    //Locating the views and setting actions
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        btnStart = (Button) view.findViewById(R.id.btn_start);
        btnReset = (Button) view.findViewById(R.id.btn_reset);
        btnStop = (Button) view.findViewById(R.id.btn_stop);
        lyButtons = (LinearLayout) view.findViewById(R.id.ly_buttons);
        tvClock = (TextView) view.findViewById(R.id.tv_clock);

        btnStart.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        restoreViewState();
        return view;

   }

    //Restoring the view state based of the state stored when onPause() called.
    public void restoreViewState(){
        SharedPreferences preferences = getActivity().getSharedPreferences("stopwatch_pref", Context.MODE_PRIVATE);
        STOPWATCH_STATE = preferences.getInt(STOP_WATCH_STATE_STORE,0);
        String storedText = preferences.getString(STOP_WATCH_TEXT,"00:00:00");
        switch (STOPWATCH_STATE){
            case STATE_START:
                showStop();
                break;
            case STATE_STOP:
                tvClock.setText(storedText);
               hideStop();
                break;
            case STATE_RESET:
                break;
            default:
                break;

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //Resetting the clock
    public void reset(){
        stopWatchService.reset();
        tvClock.setText("00:00:00");
    }

    //Showing the stop button when the clock stopped
    public void showStop(){
        lyButtons.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
    }

    //Hiding the stop button when the clock started or resumed
    public void hideStop(){
        lyButtons.setVisibility(View.VISIBLE);

    }

    //Actions for the clock buttons.
    //Saving the STATE of the watch for each action to be used to restore the view state.
    @Override
    public void onClick(View v) {
        if(v == btnStart){
            STOPWATCH_STATE = STATE_START;
            showStop();
            stopWatchService.start();
        }else if(v == btnReset){
            STOPWATCH_STATE = STATE_RESET;
            reset();
        }else if(v == btnStop){
            STOPWATCH_STATE = STATE_STOP;
            hideStop();
            stopWatchService.stop();
            btnStop.setVisibility(View.GONE);
        }
    }

    //Storing the view state
    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = getActivity().getSharedPreferences("stopwatch_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(STOP_WATCH_STATE_STORE,STOPWATCH_STATE);
        editor.putString(STOP_WATCH_TEXT, tvClock.getText().toString());
        editor.commit();

    }

    //Service Connection
    public ServiceConnection stopWatchServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            stopWatchService = (StopwatchService.StopwatchBinder)service;
            stopWatchService.init(getActivity().getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            stopWatchService.stop();
            stopWatchServiceConnection = null;
        }

    };

    //Broadcast Receiver - To receive the stop watch time
    private final BroadcastReceiver stopwatchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals("stopwatch time")) {
                int minutes = intent.getIntExtra("minutes",0);
                String timeStr = intent.getStringExtra("timeStr");
                tvClock.setText(timeStr);
                if(minutes == 99){
                    reset();
                }
            }
        }
    };

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }


}
