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
	Button btn_showCurrentLocation;
	Button btn_showAllLocations;
	Button btn_showHistory;
	TextView tv_location;
	Date time = new Date();
	LocationManager locationManager;
	Location lastKnown;
	String provider;
	Boolean dbe;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_showCurrentLocation = (Button)findViewById(R.id.btn_showCurrentLocation);
        btn_showAllLocations = (Button)findViewById(R.id.btn_showAllLocations);
        btn_showHistory = (Button)findViewById(R.id.btn_showHistory);
    	tv_location = (TextView)findViewById(R.id.tv_location);
        
        provider = defineLocationSettings();
        //locationManager.requestLocationUpdates(provider, minTime, minDistance, listener)
        locationManager.requestLocationUpdates(provider, 30000, 100, locationListener);
        
        setLocationToUi(lastKnown);
        
        if (ldbh.isEmpty()){
        	dbe = true;
        } else {
        	dbe = false;
        }
        
        setButtons(dbe);
        
        btn_showCurrentLocation.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,MapsActivity.class));
			}
		});
      
    }

	LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			//getCurrentLocationAddress(location);
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
			tv_location.setText(location.toString());
			btn_showCurrentLocation.setEnabled(true);
		} else {
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
	
	
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}