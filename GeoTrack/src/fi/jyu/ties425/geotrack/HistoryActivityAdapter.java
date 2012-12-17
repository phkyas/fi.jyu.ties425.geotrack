package fi.jyu.ties425.geotrack;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

/**
 * class to build and provide the customized list
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class HistoryActivityAdapter extends BaseAdapter {
	/*
	 * define necessary variables
	 */
	private GeoPoint[] locations;
	private String[] time;
	private String locationAsString;
	private static LayoutInflater inflater = null;

	/*
	 * constructor
	 */
	public HistoryActivityAdapter(Activity activity, GeoPoint[] locations,
			String[] time) {
		this.locations = locations;
		this.time = time;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/*
	 * return the number of items (locations) in the list
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	public int getCount() {
		return locations.length;
	}

	/*
	 * return the position of the current object
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	public Object getItem(int position) {
		return position;
	}

	/*
	 * return the current row id
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	public long getItemId(int position) {
		return position;
	}

	/*
	 * define the actions to be done when corresponding list is created
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.list_row, null);
		}

		TextView location = (TextView) vi.findViewById(R.id.tv_location_list);
		TextView timestamp = (TextView) vi.findViewById(R.id.tv_timestamp_list);

		locationAsString = locations[position].getLatitudeE6() / 1e6 + ", "
				+ locations[position].getLongitudeE6() / 1e6;

		location.setText(locationAsString);
		timestamp.setText(time[position]);

		return vi;
	}
}