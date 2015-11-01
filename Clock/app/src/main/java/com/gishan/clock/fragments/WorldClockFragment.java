package com.gishan.clock.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.gishan.clock.HelperModels.ClockContent;
import com.gishan.clock.R;
import com.gishan.clock.adapters.WorldClockAdapter;
import com.gishan.clock.services.WorldClockService;
import com.gishan.clock.utils.Constants;
import com.gishan.clock.utils.Utils;

import java.util.ArrayList;

public class WorldClockFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ViewGroup listFooter;
    private Button btnAddClock;
    private boolean shouldAddAuClock, shouldAddUkClock,shouldAddUsClock,shouldAddNzClock;
    private ClockContent.ClockItem ukClock;
    private ClockContent.ClockItem usClock;
    private ClockContent.ClockItem auClock;
    private ClockContent.ClockItem nzClock;
    private  ArrayList<ClockContent.ClockItem> clocks = new ArrayList<>();
    private WorldClockService.WorldClockBinder worldClockService = null;

    //Adapter for the list view
    private WorldClockAdapter adapter;

    //Returns the instance of the fragment to the activity
    public static WorldClockFragment newInstance(int sectionId) {
        WorldClockFragment fragment = new WorldClockFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionId);
        fragment.setArguments(args);
        return fragment;
    }


    //Empty constructor required
    public WorldClockFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new WorldClockAdapter(getActivity(),
                R.layout.adapter_worldclock,clocks);
        IntentFilter filter = new IntentFilter(
               "updated times");
        getActivity().getApplicationContext().registerReceiver(timeReceiver, filter);
        getActivity().getApplicationContext().bindService(new Intent(getActivity(), WorldClockService.class),
                worldClockServiceConnection, Context.BIND_AUTO_CREATE);

        //Allowing to show the UK clock initially
        shouldAddUkClock = true;

        //Restoring the user selection of clocks
        SharedPreferences preferences = getActivity().getSharedPreferences("world_clock_pref", Context.MODE_PRIVATE);
        shouldAddUsClock = preferences.getBoolean(Constants.COUNTRY_US, false);
        shouldAddAuClock = preferences.getBoolean(Constants.COUNTRY_AU, false);
        shouldAddNzClock = preferences.getBoolean(Constants.COUNTRY_NZ, false);

    }

    @Override
    public void onPause() {
        super.onPause();
        //Storing the user selection of clocks
        SharedPreferences preferences = getActivity().getSharedPreferences("world_clock_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.COUNTRY_US,shouldAddUsClock);
        editor.putBoolean(Constants.COUNTRY_AU,shouldAddAuClock);
        editor.putBoolean(Constants.COUNTRY_NZ, shouldAddNzClock);
        editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_worldclock, container, false);
        listView = (ListView) view.findViewById(android.R.id.list);

        //Inflating the footer for the list view for Add Clock button
        listFooter = (ViewGroup) inflater.inflate(R.layout.footer_listview, listView,
                false);
        //Adding the footer view to the list view and the action for the Add Clock button
        listView.addFooterView(listFooter, null, false);
        listView.setAdapter(adapter);
        listFooter.setOnClickListener(this);

        //Add clock button and its actions
        btnAddClock = (Button) listFooter.findViewById(R.id.add_clock);
        btnAddClock.setOnClickListener(this);

        return view;
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

    @Override
    public void onClick(View v) {
        if(v == btnAddClock || v == listFooter){
            final String[] filteredCountryList = {getActivity().getString(R.string.uk),getActivity().getString(R.string.us),getActivity().getString(R.string.au),getActivity().getString(R.string.nz)};
            //Showing user a list of clocks to choose in an alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose a country");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int item) {
                        }
                    });
            //OnClick listener for the items in the alert dialog
            builder.setItems(filteredCountryList,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int item) {
                          //When user selects an item and if that item does not exists in the list already adding it to the list view
                          if(filteredCountryList[item].equals(getActivity().getString(R.string.uk))){
                              if(!shouldAddUkClock) {
                                  shouldAddUkClock = true;
                              }
                          }else if(filteredCountryList[item].equals(getActivity().getString(R.string.us))){
                              if(!shouldAddUsClock) {
                                  shouldAddUsClock = true;
                              }
                           }else if(filteredCountryList[item].equals(getActivity().getString(R.string.au))){
                              if(!shouldAddAuClock) {
                                  shouldAddAuClock = true;
                              }
                          }else{
                              if(!shouldAddNzClock) {
                                  shouldAddNzClock = true;
                              }
                          }

                        }
                    });
            //Creating and showing the alert
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(int id);
    }

    //Service Connection
     public ServiceConnection worldClockServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            worldClockService = (WorldClockService.WorldClockBinder)service;
            worldClockService.getUpdatedTime();//Calling getUpdatedTime to receive updated clock objects every second
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            worldClockServiceConnection = null;
        }

    };

    //Broadcast Receiver - To receive the clock object every second
    private final BroadcastReceiver timeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals("updated times")){
                ArrayList<ClockContent.ClockItem> updatedClocks = intent.getParcelableArrayListExtra("clocks");
                ukClock = updatedClocks.get(0);
                usClock = updatedClocks.get(1);
                auClock = updatedClocks.get(2);
                nzClock = updatedClocks.get(3);
                //Replacing the clocks in the list with the updated clocks
                if(clocks.size()>3){
                    clocks.remove(3);
                }
                if(clocks.size()>2){
                    clocks.remove(2);
                }
                if(clocks.size()>1){
                    clocks.remove(1);
                }
                if(clocks.size()>0){
                    clocks.remove(0);
                }

                if(shouldAddUkClock) {
                    clocks.add(ukClock);
                }
                if(shouldAddUsClock){
                    clocks.add(usClock);
                }
                if(shouldAddAuClock) {
                    clocks.add(auClock);
                }
                if(shouldAddNzClock) {
                    clocks.add(nzClock);
                }
                //Refresh the list view by notifying the adapter
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
       // getActivity().unregisterReceiver(timeReceiver);
    }
}
