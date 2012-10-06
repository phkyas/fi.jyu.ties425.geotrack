package fi.jyu.ties425.geotrack;

import java.util.Date;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	LocationDatabaseHandler ldbh = new LocationDatabaseHandler(this);
	Date time = new Date();
	Button btn_showCurrentLocation;
	Button btn_showAllLocations;
	Button btn_showHistory;
	TextView tv_location;
	LocationManager locationManager;
	Location lastKnown;
	String provider;
	Boolean dbe;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //get UI elements
        btn_showCurrentLocation = (Button)findViewById(R.id.btn_showCurrentLocation);
        btn_showAllLocations = (Button)findViewById(R.id.btn_showAllLocations);
        btn_showHistory = (Button)findViewById(R.id.btn_showHistory);
    	tv_location = (TextView)findViewById(R.id.tv_location);
        //location
        provider = defineLocationSettings();
        locationManager.requestLocationUpdates(provider, 30000, 100, locationListener); //locationManager.requestLocationUpdates(provider, minTime, minDistance, listener)
        setLocationToUi(lastKnown);
        //set UI buttons
        if (ldbh.isEmpty()){
        	dbe = true;
        } else {
        	dbe = false;
        }
        setButtons(dbe);
        
        //button listener
        
        btn_showCurrentLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,MapsActivity.class).putExtra("latitude", lastKnown.getLatitude()).putExtra("longitude", lastKnown.getLongitude()));
			}
		});
        
        btn_showAllLocations.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,MapsActivity.class).putExtra("showAllLocations", true));
			}
		});
        
        //to do
        btn_showHistory.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//startActivity(new Intent(MainActivity.this,***.class));
			}
		});
      
    }

	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			lastKnown = location;
			ldbh.insertLocation(time.getTime(), location.getLatitude(), location.getLongitude());
			setLocationToUi(location);
			if(dbe){ //set to TRUE if we clear the DB
				dbe = false;
				setButtons(dbe);
			}
		}
		public void onProviderDisabled(String provider) {
			//getCurrentLocationAddress(null);
			
		}
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	};
	
	private String defineLocationSettings()  {
	    String context = Context.LOCATION_SERVICE;
	    locationManager = (LocationManager)getSystemService(context);

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
	
	private void setLocationToUi(Location location){
		if (location != null) {
			tv_location.setText(location.toString()); //rework 
			btn_showCurrentLocation.setEnabled(true);
		} else {
			tv_location.setText(R.string.tv_location);
			btn_showCurrentLocation.setEnabled(false);
		}
	}
	
	private void setButtons(Boolean value){
        if (value){
        	btn_showAllLocations.setEnabled(false);
        	btn_showHistory.setEnabled(false);
        } else {
        	btn_showAllLocations.setEnabled(true);
        	btn_showHistory.setEnabled(true);
        }
	}
	
	//rework menu
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}