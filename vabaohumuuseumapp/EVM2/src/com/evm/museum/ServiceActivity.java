package com.evm.museum;

import util.ActionBarUtilities;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class ServiceActivity extends ActionBarActivity {
	String serviceName;
	CharSequence serviceDescription;
	TypedArray pictures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int serviceIndex;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);
		
		Bundle extras = getIntent().getExtras();
		serviceIndex = extras.getInt("objectIndex");
		System.out.println("Indeks on: " + serviceIndex);
		
		/*
		pictures = this.getResources().obtainTypedArray(R.array.service_pictures);
		Drawable picture = pictures.getDrawable(serviceIndex);
		
		ImageView image = (ImageView) findViewById(R.id.service_image);
	    image.setImageDrawable(picture);
	    pictures.recycle();
	    */
	    
		serviceName=this.getResources().getStringArray(R.array.museum_services)[serviceIndex];
		serviceDescription=this.getResources().getTextArray(R.array.service_descriptions)[serviceIndex];
		
		TextView nameView = (TextView) findViewById(R.id.textView_service_name);
        nameView.setText(serviceName);
        TextView descriptionView = (TextView) findViewById(R.id.textView_service_description);
        descriptionView.setText(serviceDescription);
        //Make links in the description field clickable
        descriptionView.setMovementMethod(LinkMovementMethod.getInstance());
      
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
