package fi.jyu.ties425.geotrack;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

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
		// to do
		btn_showHistory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,
						HistoryActivity.class));
			}
		});

	}

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

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

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

	private void setLocation(Location location) {
		if (location != null) {
			tv_latitude
					.setText(Integer.toString((int) (location.getLatitude() * 1e6)));
			tv_longitude.setText(Integer.toString((int) (location
					.getLongitude() * 1e6)));
			tv_timestamp.setText(sdf.format(location.getTime()));
			btn_showCurrentLocation.setEnabled(true);
		} else {
			tv_latitude.setText(R.string.tv_default);
			tv_longitude.setText(R.string.tv_default);
			tv_timestamp.setText(R.string.tv_default);
			btn_showCurrentLocation.setEnabled(false);
		}
	}

	private void setButtons(Boolean value) {
		if (value) {
			btn_showAllLocations.setEnabled(false);
			btn_showHistory.setEnabled(false);
		} else {
			btn_showAllLocations.setEnabled(true);
			btn_showHistory.setEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

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
			.setMessage("Are you sure, that you want to delete the whole location history?")
			.setCancelable(false)
			.setPositiveButton("Delete History", new DialogInterface.OnClickListener() {
        		public void onClick(DialogInterface dialog, int id) {
        			ldbh.clearDatabase();
        			dbe = true;
        			setLocation(null);
        			setButtons(dbe);
        			Toast.makeText(MainActivity.this, "Location History Deleted", Toast.LENGTH_SHORT).show();
        		}
 	   		})
 	   		.setNegativeButton("Abort", new DialogInterface.OnClickListener() {
	    		public void onClick(DialogInterface dialog, int id) {
	    			}
 	   			})
			.show();
			break;
		default:
			break;
		}
		return true;
	}
}