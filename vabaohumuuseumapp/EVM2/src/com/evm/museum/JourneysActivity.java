package com.evm.museum;

import util.ActionBarUtilities;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JourneysActivity extends AppCompatActivity {
	String[] journeys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_journeys);
		
		
		
		//Set up a listener for list items
		journeys = this.getResources().getStringArray(R.array.journey_names);
		ListView list = (ListView)findViewById(R.id.listView_journeys);  
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				        android.R.layout.simple_list_item_1, journeys);
				list.setAdapter(adapter); 
				
				list.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
						showJourneyInfo(position);
						
					}  
				});  
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
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		} else if (itemId == R.id.action_language_est) {
			ActionBarUtilities.switchLanguage(0, this);
		} else if (itemId == R.id.action_language_eng) {
			ActionBarUtilities.switchLanguage(1, this);
		} else if (itemId == R.id.action_language_rus) {
			ActionBarUtilities.switchLanguage(2, this);
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void showJourneyInfo(int journeyIndex){
		Intent intent = new Intent(this, JourneyInfoActivity.class);
		intent.putExtra("journeyIndex", journeyIndex);
		startActivity(intent);
	}
}
