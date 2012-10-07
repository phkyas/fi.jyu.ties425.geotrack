package fi.jyu.ties425.geotrack;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

public class MapsActivity extends MapActivity {
	
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private MapsActivityItemizedOverlay itemizedoverlay;
	LocationDatabaseHandler ldbh = new LocationDatabaseHandler(this);

	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        mapView = (MapView)findViewById(R.id.myMapView);
        mapController = mapView.getController();
        mapView.setBuiltInZoomControls(true);
        mapOverlays = mapView.getOverlays();
		
		drawable = getResources().getDrawable(R.drawable.pin_gold);
        itemizedoverlay = new MapsActivityItemizedOverlay(drawable, this);
        
        Intent intent = getIntent();
        LoadMap lm = new LoadMap(this);
		if (intent.getBooleanExtra("showAllLocations", false)) {
			lm.execute();
		} else {
			lm.execute(
				new GeoPoint(
					(int) (intent.getDoubleExtra("latitude", 0)*1e6),
					(int) (intent.getDoubleExtra("longitude", 0)*1e6)
				)
			);
		}
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private class LoadMap extends AsyncTask<GeoPoint, Void, Void> {
		
		ProgressDialog dialog;
		GeoPoint[] locations;
		
		public LoadMap(Context context) {
			dialog = new ProgressDialog(context);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(GeoPoint... slocation) {
	        
	        if (slocation.length == 0) {
	        	
	        	//GeoPoint[] temp2 = (GeoPoint[]) ldbh.GetTopEntries(); This should work
	        	//locations = temp2;
	        	//showAllLocation
	        	// -> DB Query
	        	
	        	GeoPoint[] tmp = {new GeoPoint(60169845,24938551), new GeoPoint(59328930,18064910), new GeoPoint(62244747,25747218)};
	        	locations = tmp;
	        	
	        	//remove these two lines
	        	
	        } else {
	        	locations = slocation;
	        }
	        
	        for (GeoPoint gp : locations) {
	        	itemizedoverlay.addOverlay(new OverlayItem(gp, "Location Data:", gp.toString()));
	        }
	        
	        mapController.setCenter(locations[locations.length-1]);
		    mapController.setZoom(5);
		    mapOverlays.add(itemizedoverlay);
			return null;
		}
		
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dialog.isShowing()){
				dialog.dismiss();
			}
		}
	}
}