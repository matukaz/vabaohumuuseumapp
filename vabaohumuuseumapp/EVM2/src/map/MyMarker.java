/**
 * 
 */
package map;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.core.util.MercatorProjection;
import org.mapsforge.map.layer.overlay.Marker;

import com.evm.museum.MuseumMapActivity;
import com.evm.museum.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

/**
 * @author Triin
 *
 */
public class MyMarker extends Marker{
	private MuseumMapActivity activity;
	private int poiIndex;
	/**
	 * 
	 * @param latLong
	 * @param bitmap
	 * @param horizontalOffset
	 * @param verticalOffset
	 * @param activity - activity that calls for creating the marker
	 * @param poiIndex - index of the POI this marker points to
	 */
	public MyMarker(LatLong latLong, Bitmap bitmap, int horizontalOffset,
			int verticalOffset, MuseumMapActivity activity, int poiIndex) {
		super(latLong, bitmap, horizontalOffset, verticalOffset);
		this.activity=activity;
		this.poiIndex=poiIndex;
		System.out.println("Uue markeri poiIndex on: " + this.poiIndex);
	}

	@Override
	public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
		
		System.out.println("Kas tap oli markeri peal?" + this.contains(layerXY, tapXY));
		
		/*
		//If the list of latitudes and longitudes have different number of elements, something is wrong
		if(poiLats.length!=poiLons.length){
			System.out.println("Jama! Lat ja Lon listid olid eri pikkusega!");
			return false;
		}*/
		
		//If user touched marker, show detail view
		if(this.contains(layerXY, tapXY)){
			activity.openInfo(poiIndex);
			return true;
			
		}
		/*if(poiLats.length!=poiLons.length){
			System.out.println("Jama! Lat ja Lon listid olid eri pikkusega!");
			return false;
		}
		System.out.println("Päris lat on: " + this.getLatLong().latitude);
		System.out.println("Päris lon on: " + this.getLatLong().longitude);

		//Get the index of touched marker
		for(int i=0;i<poiLats.length;i++){
			tempLat = Double.parseDouble(poiLats[i]);
			tempLon = Double.parseDouble(poiLons[i]);
			if(markerLat == tempLat && markerLon == tempLon){
				System.out.println("Koordinaadid on võrdsed!");
				System.out.println("Kas marker on nähtav: " + this.isVisible());
				activity.openInfo(i);
				return true;
				
			}
		}

		*/
		System.out.println("Jõudsin MyMarkeri lõppu!");
        return false;
	}

	public int getPoiIndex() {
		return poiIndex;
	}

	public void setPoiIndex(int poiIndex) {
		this.poiIndex = poiIndex;
	}
	


}
