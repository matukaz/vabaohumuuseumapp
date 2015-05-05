package com.evm.museum;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import map.MyLocationOverlayExtended;
import map.MyMarker;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.map.android.AndroidPreferences;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.layer.MyLocationOverlay;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.PreferencesFacade;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.reader.header.FileOpenResult;
import org.mapsforge.map.reader.header.MapFileInfo;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderThemeStyleMenu;
import org.mapsforge.map.scalebar.DefaultMapScaleBar;
import org.mapsforge.map.scalebar.DefaultMapScaleBar.ScaleBarMode;
import org.mapsforge.map.scalebar.MapScaleBar;

import android.graphics.drawable.Drawable;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
/*
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;
*/
import util.MapsforgeExampleUtils;
import util.MyPreferences;
//import util.XmlRenderThemeStyleMenu;
//import org.mapsforge.map.rendertheme.XmlRenderThemeStyleMenu;
import util.OverlayMapViewerUtils;




public class MuseumMapActivity extends Activity implements OnSharedPreferenceChangeListener{
    // name of the map file in the external storage
   // private static final String MAPFILE = "estonia.map";
	private MyLocationOverlay myLocationOverlay;
    //private MapView mapView;        
    private TileCache tileCache;
    private TileRendererLayer tileRendererLayer;
    
    protected static final int DIALOG_ENTER_COORDINATES = 2923878;
    protected ArrayList<LayerManager> layerManagers = new ArrayList<LayerManager>();
    protected ArrayList<MapViewPosition> mapViewPositions = new ArrayList<MapViewPosition>();
    protected ArrayList<MapView> mapViews = new ArrayList<MapView>();
    protected PreferencesFacade preferencesFacade;
    protected SharedPreferences sharedPreferences;
    protected XmlRenderThemeStyleMenu renderThemeStyleMenu;

	LocationManager lm;

	private String[] poiLats;
	private String[] poiLons;
	
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		  
		  
//	  if (android.os.Build.VERSION.SDK_INT >= 11) {
	//		  getSupportActionBar().hide();
		//  }
		 
		  
		  //Compulsory before using any mapsforge stuff
          AndroidGraphicFactory.createInstance(this.getApplication());
          //Instantiate location manager for getting user's GPS position. Can't do before onCreate
          lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
          

          this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

          // problem that the first call to getAll() returns nothing, apparently the
          // following two calls have to be made to read all the values correctly
          // http://stackoverflow.com/questions/9310479/how-to-iterate-through-all-keys-of-shared-preferences
          this.sharedPreferences.edit().clear();
          PreferenceManager.setDefaultValues(this, R.xml.preferences, true);

          this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
          
          poiLats = getResources().getStringArray(R.array.poi_latitudes);
          poiLons = getResources().getStringArray(R.array.poi_longitudes);

          createSharedPreferences();
          createMapViews();
          createMapViewPositions();
          createLayerManagers();
          createTileCaches();
          //createControls();
         // logUser("Finished loading graph. Press long to define where to start and end the route.");
          
          
          //this.mapView = new MapView(this);
          //setContentView(this.mapView);

          /*this.mapView.setClickable(true);
          this.mapView.getMapScaleBar().setVisible(true);
          this.mapView.setBuiltInZoomControls(true);
          this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
          this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);*/

          // create a tile cache of suitable size
          /*
          this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                          mapView.getModel().displayModel.getTileSize(), 1f, 
                          this.mapView.getModel().frameBufferModel.getOverdrawFactor());

        */
        
        
	}
	
	protected void createMapViews() {
		System.out.println("Jõudsime createMapViews meetodisse!");
        MapView mapView = getMapView();
        mapView.getModel().init(this.preferencesFacade);
        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(hasZoomControls());
        mapView.setBuiltInZoomControls(true);

        mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        mapView.getMapZoomControls().setZoomLevelMax((byte) 20);
        this.mapViews.add(mapView);
	}
	
	protected void createTileCaches() {
        this.tileCache = AndroidUtil.createTileCache(this, getPersistableId(),
                        this.mapViews.get(0).getModel().displayModel.getTileSize(), this.getScreenRatio(), this.mapViews.get(0)
                                        .getModel().frameBufferModel.getOverdrawFactor());
	}
	
	protected void destroyLayers() {
        for (LayerManager layerManager : this.layerManagers) {
                for (Layer layer : layerManager.getLayers()) {
                        layerManager.getLayers().remove(layer);
                        layer.onDestroy();
                }
        }
	}

	protected void destroyMapViewPositions() {
	        for (MapViewPosition mapViewPosition : mapViewPositions) {
	                mapViewPosition.destroy();
	        }
	}
	
	protected void destroyMapViews() {
	        for (MapView mapView : mapViews) {
	                mapView.destroy();
	        }
	}
	
	protected void destroyTileCaches() {
	        this.tileCache.destroy();
	}

	protected MapPosition getInitialPosition() {
        MapDatabase mapDatabase = new MapDatabase();
        final FileOpenResult result = mapDatabase.openFile(getMapFile());
        if (result.isSuccess()) {
                final MapFileInfo mapFileInfo = mapDatabase.getMapFileInfo();
                if (mapFileInfo != null && mapFileInfo.startPosition != null) {
                        return new MapPosition(mapFileInfo.startPosition, (byte) mapFileInfo.startZoomLevel);
                } else {
                        return new MapPosition(new LatLong(59.431466, 24.6382674), (byte) 15);
                }
        }
        throw new IllegalArgumentException("Invalid Map File " + getMapFileName());
	}
	
	/**
     * @return a map file
     */
    protected File getMapFile() {
            File file = new File(getExternalFilesDir(null), this.getMapFileName());
            //Log.i(SamplesApplication.TAG, "Map file is " + file.getAbsolutePath());
            return file;
    }

    /**
     * @return the map file name to be used
     */
    protected String getMapFileName() {
            return "museum.map";
    }

    /**
     * @return the id that is used to save this mapview
     */
    protected String getPersistableId() {
            return this.getClass().getSimpleName();
    }
    /**
     * @return the rendertheme for this viewer
     */
    protected XmlRenderTheme getRenderTheme() {
            return InternalRenderTheme.OSMARENDER;
    }

    /**
     * @return the screen ratio that the mapview takes up (for cache calculation)
     */
    protected float getScreenRatio() {
            return 1.0f;
    }

    protected boolean hasZoomControls() {
            return true;
    }

    /**
     * initializes the map view position.
     *
     * @param mvp
     *            the map view position to be set
     * @return the mapviewposition set
     */
    protected MapViewPosition initializePosition(MapViewPosition mvp) {
            LatLong center = mvp.getCenter();

            if (center.equals(new LatLong(0, 0))) {
                    mvp.setMapPosition(this.getInitialPosition());
            }
            mvp.setZoomLevelMax((byte) 24);
            mvp.setZoomLevelMin((byte) 7);
            return mvp;
    }

    
	
	@Override
	 protected void onStart() {
        super.onStart();
        
        //always create base layers
        createLayers();
        
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	//If we have a specific POI id, then we highlight that POI on the map
			if(extras.containsKey("objectIndex")){
				int objectIndex = extras.getInt("objectIndex");
				showPoiOnMap(objectIndex);
			}
        	
			
			//If we have a journey, then show it on map
			if(extras.containsKey("journeyIndex")){
				int journeyIndex = extras.getInt("journeyIndex");
				addJourney(journeyIndex);
			}	
        }
	}
	
	@Override
    protected void onStop() {
		super.onStop();
        destroyLayers();

        //Vana kood
        /*
        this.mapView.getLayerManager().getLayers().remove(this.tileRendererLayer);
        this.tileRendererLayer.onDestroy();*/
    }

	 @Override
     protected void onDestroy() {
		 super.onDestroy();
         destroyTileCaches();
         destroyMapViewPositions();
         destroyMapViews();
         this.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
         org.mapsforge.map.android.graphics.AndroidResourceBitmap.clearResourceBitmaps();

         this.tileCache.destroy();
         
         //Ise pandud
         destroyLayers();
     }
     
	 /*
     private File getMapFile() {
             File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
             return file;
     }*/


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
		getMenuInflater().inflate(R.menu.museum_map, menu);
		return true;
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
    public void onPause() {
            myLocationOverlay.disableMyLocation();
            
            super.onPause();
            for (MapView mapView : mapViews) {
                    mapView.getModel().save(this.preferencesFacade);
            }
            this.preferencesFacade.save();

    }
	
	public void onResume() {
        super.onResume();
        this.myLocationOverlay.enableMyLocation(true);
        
	}
	
	
    protected void createLayers() {
		TileRendererLayer tileRendererLayer = MapsforgeExampleUtils.createTileRendererLayer(this.tileCache,
                this.mapViewPositions.get(0), getMapFile(), getRenderTheme(), false);
		this.layerManagers.get(0).getLayers().add(tileRendererLayer);

		//Show exhibits on map
		Drawable drawable2 = getResources().getDrawable(R.drawable.hand_gray);
        Bitmap bitmap2 = AndroidGraphicFactory.convertToBitmap(drawable2);
        
        //Check that latitude and longitude list have the same number of elements
        if(poiLats.length != poiLons.length){
        	System.out.println("VIGA! LAT JA LON LISTIDE PIKKUS ON ERINEV!");
        	return;
        }
        
        //Display markers on map
        MyMarker marker;
        double tempLat;
        double tempLon;
        for(int i=0;i<poiLats.length;i++){
        	tempLat = Double.parseDouble(poiLats[i]);
        	tempLon = Double.parseDouble(poiLons[i]);
        	marker = new MyMarker(new LatLong(tempLat, tempLon), bitmap2, 0, 0, this, i);
        	this.layerManagers.get(0).getLayers().add(marker);
        }
		
		//Show user's position
            // a marker to show at the position
            Drawable drawable = getResources().getDrawable(R.drawable.person_small);
            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);

            
            // create the overlay and tell it to follow the location
            this.myLocationOverlay = new MyLocationOverlayExtended(this,
                            this.mapViewPositions.get(0), bitmap);
            
            //this.myLocationOverlay.setSnapToLocationEnabled(true);
            this.layerManagers.get(0).getLayers().add(this.myLocationOverlay);
            
    }
    
    protected void showPoiOnMap(int objectIndex) {
    	//Add special marker for the POI user wanted to see
    	Drawable specialDrawable = getResources().getDrawable(R.drawable.hand_red);
        Bitmap specialBitmap = AndroidGraphicFactory.convertToBitmap(specialDrawable);
        LatLong coordinates = getPOILatLong(objectIndex);
        MyMarker marker = new MyMarker(coordinates, specialBitmap, 1, 1, this, objectIndex);
        this.layerManagers.get(0).getLayers().add(marker);
    }
	
	/**
     * @return the layout to be used
     */
    protected int getLayoutId() {
            return R.layout.activity_museum_map;
    }

	
	
    protected MapView getMapView() {
            // in this example the mapview is defined in the layout file
            // mapviewer.xml
            setContentView(getLayoutId());
            return (MapView) findViewById(R.id.mapView);
    }

	protected void createLayerManagers() {
        for (MapView mapView : mapViews) {
                this.layerManagers.add(mapView.getLayerManager());
        }
	}
	
	protected void createMapViewPositions() {
        for (MapView mapView : mapViews) {
            MapViewPosition position = new MapViewPosition(new DisplayModel());
            this.mapViewPositions.add(position);
        	//this.mapViewPositions.add(initializePosition(mapView.getModel().mapViewPosition));
             // mapView.getModel().mapViewPosition.setCenter(new LatLong(59.431466, 24.6382674));
              byte zoomLevel_normal = 16;
              byte zoomLevel_journey = 14;
              LatLong currentLatLong = getCurrentLatLong();
              LatLong museumGate = new LatLong(59.431466,24.6382674);
              LatLong museumCenter = new LatLong(59.4350, 24.6371);
              Bundle extras = getIntent().getExtras();
              if(extras != null && (extras.containsKey("journeyIndex") || extras.containsKey("objectIndex"))){
            	  mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(museumCenter, zoomLevel_journey));
              }else if(currentLatLong != null){
            	  mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(currentLatLong, zoomLevel_normal));
              }else{
            	  mapView.getModel().mapViewPosition.setMapPosition(new MapPosition(museumGate, zoomLevel_normal));
              }
         }
	}
	
	protected void redrawLayers() {
        for (LayerManager layerManager : this.layerManagers) {
                layerManager.redrawLayers();
        }
	}

	/**
	 * sets the content view if it has not been set already.
	 */
	protected void setContentView() {
	        setContentView(this.mapViews.get(0));
	}
	
	@Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
            if (MyPreferences.SETTING_SCALE.equals(key)) {
                    destroyTileCaches();
                    for (MapView mapView : mapViews) {
                            mapView.getModel().displayModel.setUserScaleFactor(DisplayModel.getDefaultUserScaleFactor());
                    }
                    Log.d(MyPreferences.TAG, "Tilesize now " + mapViews.get(0).getModel().displayModel.getTileSize());
                    createTileCaches();
                    redrawLayers();
            }
            if (MyPreferences.SETTING_SCALEBAR.equals(key)) {
                    setScaleBars();
            }

    }
	
	protected void setScaleBars() {
        String value = this.sharedPreferences.getString(MyPreferences.SETTING_SCALEBAR, "both");

        for (MapView mapView : mapViews) {

                // TODO the setting of the scale bars is somewhat clunky.
                if (MyPreferences.SETTING_SCALEBAR_NONE.equals(value)) {
                        mapView.setMapScaleBar(null);
                } else {
                        MapScaleBar scalebar = mapView.getMapScaleBar();
                        if (scalebar == null) {
                                mapView.setMapScaleBar(new DefaultMapScaleBar(mapView.getModel().mapViewPosition, mapView.getModel().mapViewDimension,
                                                AndroidGraphicFactory.INSTANCE, mapView.getModel().displayModel));
                                scalebar = mapView.getMapScaleBar();
                        }
                        if (scalebar instanceof DefaultMapScaleBar) {
                                if (MyPreferences.SETTING_SCALEBAR_BOTH.equals(value)) {
                                        ((DefaultMapScaleBar) scalebar).setScaleBarMode(ScaleBarMode.BOTH);
                                } else if (MyPreferences.SETTING_SCALEBAR_METRIC.equals(value)) {
                                        ((DefaultMapScaleBar) scalebar).setScaleBarMode(ScaleBarMode.METRIC);
                                } else if (MyPreferences.SETTING_SCALEBAR_IMPERIAL.equals(value)) {
                                        ((DefaultMapScaleBar) scalebar).setScaleBarMode(ScaleBarMode.IMPERIAL);
                                }
                        }
                }
        }
	}

	
	protected void createSharedPreferences() {
        SharedPreferences sp = this.getSharedPreferences(getPersistableId(), MODE_PRIVATE);
        this.preferencesFacade = new AndroidPreferences(sp);
	}
	
	protected LatLong getCurrentLatLong(){
		try{
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			return new LatLong(latitude, longitude);
			
		}catch(Exception e){
			System.out.println("Asukoht puudub!");
			logUser(getResources().getString(R.string.message_gps_off));
			e.printStackTrace();
			return null;
		}
		
	}
/*
	private void setupGraphHopper(){
		GraphHopper hopper = new GraphHopper().forMobile();
		hopper.setInMemory(false);
		hopper.setOSMFile("Tallin.osm");
		// where to store graphhopper files?
		hopper.setGraphHopperLocation("gh");
		hopper.setEncodingManager(new EncodingManager("car"));

		// now this can take minutes if it imports or a few seconds for loading
		// of course this is dependent on the area you import
		hopper.importOrLoad();

		GHRequest req = new GHRequest(59.402105,24.6587339, 59.39668,24.670035).setVehicle("car");
		GHResponse rsp = hopper.route(req);

		// first check for errors
		if(rsp.hasErrors()) {
		   // handle them!
		   // rsp.getErrors()
		   return;
		}

		// route was found? e.g. if disconnected areas (like island) 
		// no route can ever be found
		if(!rsp.isFound()) {
		   // handle properly
		   return;
		}

		// points, distance in meters and time in millis of the full path
		PointList pointList = rsp.getPoints();
		double distance = rsp.getDistance();
		long millis = rsp.getMillis();

		
		// get the turn instructions for the path
		InstructionList il = rsp.getInstructions();
		/*Translation tr = trMap.getWithFallBack(Locale.US);
		List<String> iList = il.createDescription(tr);
		

		// or get the result as gpx entries:
		List<GPXEntry> list = il.createGPXList();
	}

*/

	
	
	private void logUser( String str )
    {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }
	public void openInfo(int objectIndex){
		System.out.println("Jõudsin objekti info avamise juurde!");
		Intent intent = new Intent(this, MuseumObjectActivity.class);
		intent.putExtra("objectIndex", objectIndex);
		startActivity(intent);	
	}
	private LatLong getPOILatLong(int objectIndex){
		String latAsString = getResources().getStringArray(R.array.poi_latitudes)[objectIndex];
		String lonAsString = getResources().getStringArray(R.array.poi_longitudes)[objectIndex];
		double lat = Double.parseDouble(latAsString);
		double lon = Double.parseDouble(lonAsString);
		return new LatLong(lat, lon);
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static String convertStreamToString(InputStream is)
            throws IOException {
            Writer writer = new StringWriter();

            char[] buffer = new char[2048];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            String text = writer.toString();
            return text;
    }
	
	
	
	private void addJourney(int journeyIndex){
		TypedArray journeyIds1 = getResources().obtainTypedArray(R.array.journeys_lats);
		int idLat = journeyIds1.getResourceId(journeyIndex, -1);
		TypedArray journeyIds2 = getResources().obtainTypedArray(R.array.journeys_lons);
		int idLon = journeyIds2.getResourceId(journeyIndex, -1);
		System.out.println("IdLat on: " + idLat + " ja idLon on " + idLon);
		
		String[] lats = getResources().getStringArray(idLat);
		String[] lons = getResources().getStringArray(idLon);
		double lat;
		double lon;
		
		
		if(lats.length!=lons.length){
			return;
		}
		
		Polyline polyline = new Polyline(OverlayMapViewerUtils.createPaint(
                AndroidGraphicFactory.INSTANCE.createColor(Color.BLUE), 4,
                Style.STROKE), AndroidGraphicFactory.INSTANCE);
		List<LatLong> latLongs = polyline.getLatLongs();

		
		for(int i=0;i<lats.length;i++){
			lat = Double.parseDouble(lats[i]);
			lon = Double.parseDouble(lons[i]);
			LatLong latLong = new LatLong(lat,lon);
			latLongs.add(latLong);
			
		}
        this.layerManagers.get(0).getLayers().add(polyline);
		//System.out.println("Polyline'is elemente: " + polyline.get)
        journeyIds1.recycle();
        journeyIds2.recycle();
		return;
	}

}
