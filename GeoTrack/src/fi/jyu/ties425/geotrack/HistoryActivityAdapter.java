package fi.jyu.ties425.geotrack;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryActivityAdapter extends BaseAdapter {
	
	private GeoPoint[] locations;
	private String[] time;
	private String locationAsString;
	private static LayoutInflater inflater=null;
	
	public HistoryActivityAdapter(Activity activity, GeoPoint[] locations, String[] time){
		this.locations = locations;
		this.time = time;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return locations.length;
	}

	public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.list_row, null);
        }
		
		TextView location = (TextView)vi.findViewById(R.id.tv_location_list);
		TextView timestamp = (TextView)vi.findViewById(R.id.tv_timestamp_list);
		
		locationAsString = locations[position].getLatitudeE6()/1e6 + ", " + locations[position].getLongitudeE6()/1e6;
		
		location.setText(locationAsString);
		timestamp.setText(time[position]);
		
		return vi;
	}
}