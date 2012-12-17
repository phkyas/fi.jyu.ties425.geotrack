package fi.jyu.ties425.geotrack;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * class to show one or many location/s via the google maps api for android
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class MapsActivity extends MapActivity {
	/*
	 * define necessary variables
	 */
	private LocationDatabaseHandler ldbh = new LocationDatabaseHandler(this);
	private MapView mapView;
	private MapController mapController;
	private List<Overlay> mapOverlays;
	private Drawable drawable;
	private MapsActivityItemizedOverlay itemizedoverlay;

	/*
	 * render the map at startup with the set of locations
	 * 
	 * @see com.google.android.maps.MapActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);

		/*
		 * instantiations
		 */
		mapView = (MapView) findViewById(R.id.myMapView);
		mapController = mapView.getController();
		mapView.setBuiltInZoomControls(true);
		mapOverlays = mapView.getOverlays();

		drawable = getResources().getDrawable(R.drawable.pin_gold);
		itemizedoverlay = new MapsActivityItemizedOverlay(drawable, this);

		/*
		 * load the map in an new thread, so that the app-ui is not blocked (and
		 * in case the loading will take longer than 5s, the crash is avoided
		 * here)
		 */
		Intent intent = getIntent();
		LoadMap lm = new LoadMap(this);
		if (intent.getBooleanExtra("showAllLocations", false)) {
			lm.execute();
		} else {
			lm.execute(new GeoPoint(
					(int) (intent.getDoubleExtra("latitude", 0) * 1e6),
					(int) (intent.getDoubleExtra("longitude", 0) * 1e6)));
		}

	}

	/*
	 * no route displaying tasks
	 * 
	 * @see com.google.android.maps.MapActivity#isRouteDisplayed()
	 */
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * class to load the map outside the main ui thread
	 * 
	 * @author Philipp Kyas and Jouni Laitinen
	 * @version 1.0
	 */
	private class LoadMap extends AsyncTask<GeoPoint, Void, Void> {

		/*
		 * define necessary variables
		 */
		ProgressDialog dialog;
		GeoPoint[] locations;

		/*
		 * constructor
		 */
		public LoadMap(Context context) {
			dialog = new ProgressDialog(context);
		}

		/*
		 * show a processdialog to the user
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog.setMessage("Loading...");
			dialog.show();
		}

		/*
		 * load one or many locations, show them in the map (incl. markers), set
		 * the center and zoom
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Void doInBackground(GeoPoint... slocation) {

			if (slocation.length == 0) {
				locations = ldbh.getTop50Locations();
			} else {
				locations = slocation;
			}

			for (GeoPoint gp : locations) {
				itemizedoverlay.addOverlay(new OverlayItem(gp,
						"Location Data:", gp.toString()));
			}

			mapController.setCenter(locations[0]);
			mapController.setZoom(10);
			mapOverlays.add(itemizedoverlay);
			return null;
		}

		/*
		 * close the process dialog afterwards
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
}