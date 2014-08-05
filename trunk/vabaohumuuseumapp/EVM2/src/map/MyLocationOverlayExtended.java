/**
 * 
 */
package map;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.map.android.layer.MyLocationOverlay;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.model.MapViewPosition;

import android.content.Context;
import android.location.LocationManager;

/**
 * @author Triin
 *
 */
public class MyLocationOverlayExtended extends MyLocationOverlay {
	 public LocationManager locationManager;
     public boolean myLocationEnabled;

	
	public MyLocationOverlayExtended(Context context, MapViewPosition mapViewPosition, Bitmap bitmap){
		super(context, mapViewPosition, bitmap);
	}
	
	@Override
    public void onProviderEnabled(String provider) {
            enableBestAvailableProvider();
            requestRedraw();
    }
	
	
	//Had to copy-paste from parent because private
	private synchronized boolean enableBestAvailableProvider() {
        disableMyLocation();

        boolean result = false;
        for (String provider : locationManager.getProviders(true)) {
                if (LocationManager.GPS_PROVIDER.equals(provider)
                                || LocationManager.NETWORK_PROVIDER.equals(provider)) {
                        result = true;
                        this.locationManager.requestLocationUpdates(provider, 1000, 0, this);
                }
        }
        this.myLocationEnabled = result;
        return result;
}



}
