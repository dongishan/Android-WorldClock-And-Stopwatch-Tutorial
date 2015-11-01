package com.gishan.clock;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.gishan.clock.fragments.NavigationDrawerFragment;
import com.gishan.clock.fragments.StopwatchFragment;
import com.gishan.clock.fragments.TimerFragment;
import com.gishan.clock.fragments.WorldClockFragment;


public class HomeActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        WorldClockFragment.OnFragmentInteractionListener,
        StopwatchFragment.OnFragmentInteractionListener,
        TimerFragment.OnFragmentInteractionListener{

    private NavigationDrawerFragment navigationDrawerFragment;
    private CharSequence title;
    private final String TAG = getClass().getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        locateViews();
        setUpViews();

    }

    //Referencing to the views
    public void locateViews(){
        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

    }

    //Initializing the views and setting up the drawer
    public void setUpViews(){
        title = getTitle();
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    //Calling the specific fragment based on the index selected in the navigation drawer
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
       switch (position){
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, WorldClockFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, StopwatchFragment.newInstance(position + 1))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container,TimerFragment.newInstance(position+1))
                    .commit();
                break;
            default:
                break;
        }
        onSectionAttached(position);

    }
    //Updating the Navigation Title
    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                title = getString(R.string.title_section1);
                break;
            case 1:
                title = getString(R.string.title_section2);
                break;
            case 2:
                title = getString(R.string.title_section3);
                break;
        }
    }

    //Restoring the action bar, when option menu created
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }

    //Inflating the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Calling the About activity when the menu button clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent intent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //Delegate methods
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onFragmentInteraction(int id) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
