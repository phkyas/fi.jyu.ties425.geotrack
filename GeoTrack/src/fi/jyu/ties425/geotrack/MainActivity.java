package fi.jyu.ties425.geotrack;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * main class which starts and stops the location service (incl. DB) as well as
 * renders the start view
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class MainActivity extends Activity {

	/*
	 * definition of the necessary variables
	 */
	LocationDatabaseHandler ldbh = new LocationDatabaseHandler(this);
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
	Date time;
	Button btn_showCurrentLocation;
	Button btn_showAllLocations;
	Button btn_showHistory;
	TextView tv_latitude;
	TextView tv_longitude;
	TextView tv_timestamp;
	LocationManager locationManager;
	Location lastKnown;
	String provider;
	Boolean dbe;

	/*
	 * render the layout at startup, activate the location service, and set
	 * onClickListeners
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// get UI elements
		btn_showCurrentLocation = (Button) findViewById(R.id.btn_showCurrentLocation);
		btn_showAllLocations = (Button) findViewById(R.id.btn_showAllLocations);
		btn_showHistory = (Button) findViewById(R.id.btn_showHistory);
		tv_latitude = (TextView) findViewById(R.id.tv_latitude_data);
		tv_longitude = (TextView) findViewById(R.id.tv_longitude_data);
		tv_timestamp = (TextView) findViewById(R.id.tv_timestamp_data);

		// location provider
		provider = defineLocationSettings();

		// button listener
		btn_showCurrentLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MapsActivity.class)
						.putExtra("latitude", lastKnown.getLatitude())
						.putExtra("longitude", lastKnown.getLongitude()));
			}
		});
		btn_showAllLocations.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MapsActivity.class)
						.putExtra("showAllLocations", true));
			}
		});
		btn_showHistory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						NewFragmentActivity.class));
			}
		});

	}

	/*
	 * activate the location service again after returning from another view
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		// location
		locationManager.requestLocationUpdates(provider, 30000, 100,
				locationListener); // locationManager.requestLocationUpdates(provider,
									// minTime, minDistance, listener)
		setLocation(lastKnown);
		// set UI buttons
		if (ldbh.isEmpty()) {
			dbe = true;
		} else {
			dbe = false;
		}
		setButtons(dbe);
	}

	/*
	 * deactivate the location service when the current view is left
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	/*
	 * create and define the location listener and define the actions to be done
	 * at any location change event, here set a few variables and write the
	 * location to the db
	 */
	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			time = new Date();
			lastKnown = location;
			lastKnown.setTime(time.getTime());
			ldbh.insertLocation(sdf.format(time.getTime()),
					location.getLatitude(), location.getLongitude());
			setLocation(location);
			if (dbe) {
				dbe = false;
				setButtons(dbe);
			}
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};

	/*
	 * define the exact location settings, e.g. distance, accuracy,...
	 */
	private String defineLocationSettings() {
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(false);

		String provider = locationManager.getBestProvider(criteria, true);

		lastKnown = locationManager.getLastKnownLocation(provider);
		return provider;
	}

	/*
	 * set the current location data to the view
	 */
	private void setLocation(Location location) {
		if (location != null) {
			tv_latitude.setText(Double.toString(location.getLatitude()));
			tv_longitude.setText(Double.toString(location.getLongitude()));
			tv_timestamp.setText(sdf.format(location.getTime()));
			btn_showCurrentLocation.setEnabled(true);
		} else {
			tv_latitude.setText(R.string.tv_default);
			tv_longitude.setText(R.string.tv_default);
			tv_timestamp.setText(R.string.tv_default);
			btn_showCurrentLocation.setEnabled(false);
		}
	}

	/*
	 * activate and deactivate the buttons showAllLocations and showHistory
	 */
	private void setButtons(Boolean value) {
		if (value) {
			btn_showAllLocations.setEnabled(false);
			btn_showHistory.setEnabled(false);
		} else {
			btn_showAllLocations.setEnabled(true);
			btn_showHistory.setEnabled(true);
		}
	}

	/*
	 * create an options menu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/*
	 * define the actions to be done when an options menu item is selected
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			Toast.makeText(this, "A nice application...", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.menu_clearDatabase:
			new AlertDialog.Builder(this)
					.setTitle("Attention - Delete Location History")
					.setMessage(
							"Are you sure, that you want to delete the whole location history?")
					.setCancelable(false)
					.setPositiveButton("Delete History",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									ldbh.clearDatabase();
									dbe = true;
									setLocation(null);
									setButtons(dbe);
									Toast.makeText(MainActivity.this,
											"Location History Deleted",
											Toast.LENGTH_SHORT).show();
								}
							})
					.setNegativeButton("Abort",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
								}
							}).show();
			break;
		default:
			break;
		}
		return true;
	}
}