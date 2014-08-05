package com.evm.museum;

import util.ActionBarUtilities;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ObjectListActivity extends ActionBarActivity {
	String[] museumObjectsList;
	String[] objectNames;
	
	private void showDetailedInfo(int objectIndex){
		Intent intent = new Intent(this, MuseumObjectActivity.class);
		intent.putExtra("objectIndex", objectIndex);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_object_list);
		// Show the Up button in the action bar.
		setupActionBar();
		
		objectNames = this.getResources().getStringArray(R.array.object_names);
		ListView list = (ListView)findViewById(R.id.museumObjects);  
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, objectNames);
				list.setAdapter(adapter); 
				
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						showDetailedInfo(position);
						
					}  
				});  
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		/// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
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

}
