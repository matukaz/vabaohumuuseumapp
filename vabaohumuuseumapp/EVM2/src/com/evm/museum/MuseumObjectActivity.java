package com.evm.museum;

import util.ActionBarUtilities;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class MuseumObjectActivity extends ActionBarActivity {
	String objectName;
	String objectDescription;
	int objectPicture;
	TypedArray pictures;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Muidu ei saa onClick sees sellele viidata (onClickListener)
		final int objectIndex;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_museum_object);
		// Show the Up button in the action bar.
		setupActionBar();
		Bundle extras = getIntent().getExtras();
		objectIndex = extras.getInt("objectIndex");
		System.out.println("Indeks on: " + objectIndex);
		
		objectName=this.getResources().getStringArray(R.array.object_names)[objectIndex];
		objectDescription=this.getResources().getStringArray(R.array.object_descriptions)[objectIndex];
		pictures = this.getResources().obtainTypedArray(R.array.object_pictures);
		Drawable picture = pictures.getDrawable(objectIndex);
		
		//String taluNimi = this.getResources().getXml(R.array.objects).getAttributeValue(0); //Tegelikult ei ole object string-array
		System.out.println("Talu nimi on: " + objectName);
		//String taluKirjeldus = this.getResources().getXml(R.array.objects).getAttributeValue(1);
		System.out.println("Kirjeldus on: " + objectDescription);
		
		//RelativeLayout museumObjectLayout = (RelativeLayout) findViewById(R.id.museumObjectLayout);
		
		//ImageView imageV = new ImageView(MuseumObjectActivity.this);
		ImageView imageV = (ImageView) findViewById(R.id.imageView_museumObject);
        imageV.setImageDrawable(picture);
       // imageV.setScaleType(ImageView.ScaleType.FIT_START);
       // museumObjectLayout.addView(imageV, 0);
        
        
        TextView nameView = (TextView) findViewById(R.id.textView_objectName);
        nameView.setText(objectName);
        TextView descriptionView = (TextView) findViewById(R.id.textView_description);
        descriptionView.setText(objectDescription);
        
       Button buttonToMap = (Button) findViewById(R.id.button_show_on_map);
       buttonToMap.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View v) {
    	        showOnMap(objectIndex);
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
	
	private void showOnMap(int objectIndex){
		Intent intent = new Intent(this, MuseumMapActivity.class);
		intent.putExtra("objectIndex", objectIndex);
		startActivity(intent);
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		pictures.recycle();
	}

}
