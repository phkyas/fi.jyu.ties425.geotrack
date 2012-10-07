package fi.jyu.ties425.geotrack;

import com.google.android.maps.GeoPoint;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.app.Activity;
import android.content.Intent;

public class HistoryActivity extends Activity {
	
	private LocationDatabaseHandler ldbh = new LocationDatabaseHandler(this);
	private ListView list;
	private HistoryActivityAdapter adapter;
	private GeoPoint[] locations;
	private String[] time;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		locations = ldbh.getTop50Locations();
		time = ldbh.getTop50Timestamps();


		list = (ListView) findViewById(R.id.locationList);
		adapter = new HistoryActivityAdapter(this, locations, time);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				startActivity(new Intent(HistoryActivity.this,MapsActivity.class).putExtra("latitude", locations[(int) id].getLatitudeE6()/1e6).putExtra("longitude", locations[(int) id].getLongitudeE6()/1e6));
			}	
		});
		
	}

}
