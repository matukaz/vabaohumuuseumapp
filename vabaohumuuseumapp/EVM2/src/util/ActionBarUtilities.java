/**
 * 
 */
package util;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * @author Triin
 *
 */
public class ActionBarUtilities {
	//TODO: Lugeda keele variandid failist
    public static final CharSequence[] items = {"Eesti keel", "English", "по-русски"};
	
	public static void restartActivity(Activity activity) {
	    Intent intent = activity.getIntent();
	    activity.finish();
	    activity.startActivity(intent);
	}
	
	public static void switchLanguage(int item, Activity activity){

		//Kui clickitakse menüüs olevale valikule
		// items[item] == items[0]
		if (items[item] == "Eesti keel") {
			Locale locale = new Locale("et");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			activity.getBaseContext().getResources().updateConfiguration(config,
					activity.getBaseContext().getResources()
					.getDisplayMetrics());
			restartActivity(activity);
			System.out.println("Keel: eesti");
		}// end if
		else if (items[item] == "English") {

			Locale locale = new Locale("eng");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			activity.getBaseContext().getResources().updateConfiguration(config,
					activity.getBaseContext().getResources()
					.getDisplayMetrics());
			
			restartActivity(activity);
			System.out.println("Keel: inglise");

		}
		else if (items[item] == "по-русски") {

			Locale locale = new Locale("ru");
			Locale.setDefault(locale);
			Configuration config = new Configuration();
			config.locale = locale;
			activity.getBaseContext().getResources().updateConfiguration(config,
					activity.getBaseContext().getResources()
					.getDisplayMetrics());
			restartActivity(activity);
			System.out.println("Keel: vene");

		}
		
	}
	
	public static void showLanguageSettings(Activity activity){
		final Activity innerActivity = activity;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Language");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switchLanguage(item, innerActivity);
			}
		}); // end of on click listener
		AlertDialog alert = builder.create();
		alert.show();

	}
	

}
