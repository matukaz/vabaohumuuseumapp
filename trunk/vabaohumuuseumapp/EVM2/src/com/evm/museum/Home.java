package com.evm.museum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import util.ActionBarUtilities;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.*;
import android.support.v7.internal.view.menu.MenuView.ItemView;

public class Home extends ActionBarActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		if(android.os.Build.VERSION.SDK_INT >= 11){
			getActionBar().setDisplayUseLogoEnabled(true);
		}else{
			getSupportActionBar().setDisplayUseLogoEnabled(true);
		}
		
		setOpeningHours();
		
	}
	@Override
	protected void onStart(){
		super.onStart();
		createExternalStoragePrivateFile();	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//ActionBarUtilities.showLanguageSettings(this);

		switch(item.getItemId())
		{
		case R.id.action_language_est:
			ActionBarUtilities.switchLanguage(0, this);
			break;

		case R.id.action_language_eng:
			ActionBarUtilities.switchLanguage(1, this);
			break;
			
		case R.id.action_language_rus:
			ActionBarUtilities.switchLanguage(2, this);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		//getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.backrepeat_stripe));
		return super.onCreateOptionsMenu(menu);
		//return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode){
		case KeyEvent.KEYCODE_MENU:
			ActionBarUtilities.showLanguageSettings(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	
	public void openMapView(View view){
		Intent intent = new Intent(this, MuseumMapActivity.class);
	   // EditText editText = (EditText) findViewById(R.id.edit_message);
	   // String message = editText.getText().toString();
	   // intent.putExtra(EXTRA_MESSAGE, message);
	    startActivity(intent);

	}
	public void openTicketsView(View view){
		Intent intent = new Intent(this, GeneralInfoActivity.class);
		startActivity(intent);
	}
	
	public void openServicesView(View view){
		Intent intent = new Intent(this, ServicesActivity.class);
		startActivity(intent);
	}
	
	public void openJourneysView(View view){
		Intent intent = new Intent(this, JourneysActivity.class);
		startActivity(intent);
	}
	
	/*
	public void openMuseumObjectActivity(View view){
		Intent intent = new Intent(this, MuseumObjectActivity.class);
		startActivity(intent);
	}
	*/
	
	public void openObjectListActivity(View view){
		Intent intent = new Intent(this, ObjectListActivity.class);
		startActivity(intent);
	}
	/**
	 * Taken from: http://developer.android.com/reference/android/content/Context.html#getExternalFilesDir%28java.lang.String%29
	 */
	private void createExternalStoragePrivateFile() {
	    // Create a path where we will place our private file on external
	    // storage.
	    File file = new File(getExternalFilesDir(null), "museum.map");

	    try {
	        // Very simple code to copy a picture from the application's
	        // resource into the external file.  Note that this code does
	        // no error checking, and assumes the picture is small (does not
	        // try to copy it in chunks).  Note that if external storage is
	        // not currently mounted this will silently fail.
	        InputStream is = getResources().getAssets().open("museum1.map");
	        OutputStream os = new FileOutputStream(file);
	        byte[] data = new byte[is.available()];
	        is.read(data);
	        os.write(data);
	        is.close();
	        os.close();
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.w("ExternalStorage", "Error writing " + file, e);
	    }
	}
	
	private void setOpeningHours(){
		Time now = new Time();
		now.setToNow();
		System.out.println("Praegune kuu on: " + now.month);
		System.out.println("Praegune päev on: " + now.monthDay);
		
		TextView textView = (TextView) findViewById(R.id.opening_hours);
		
		//June 24 closed
		if(now.month==5 && now.monthDay==24){
			textView.setText(R.string.opening_hours_closed);
			
		//Dec 24, 25 and 31 closed
		}else if(now.month==11 && (now.monthDay==24 || now.monthDay==25 || now.monthDay==31)){
			textView.setText(R.string.opening_hours_closed);
		//Summer season - April 23 to September 28. 
		}else if(now.month == 3 && now.monthDay>=23 || now.month > 3 && now.month <8 || now.month==8 && now.monthDay<=28){
			textView.setText(R.string.opening_hours_summer);
		//Winter season
		}else{
			textView.setText(R.string.opening_hours_winter);
		}
	}
}
