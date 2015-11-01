package com.gishan.clock;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class AboutActivity extends ActionBarActivity implements View.OnClickListener{
    private Button btnBack;

    //Setting up the layout for the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        locateViews();
        setActions();
    }

    //Referencing to the views
    private void locateViews(){
        btnBack = (Button) findViewById(R.id.btn_back);
    }

    //Setting the action listeners
    private void setActions(){
        btnBack.setOnClickListener(this);
    }

    //Onclick to handle the actions
    @Override
    public void onClick(View v) {
        if(v ==btnBack){
            onBackPressed();
        }
    }

    //Delegate methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
