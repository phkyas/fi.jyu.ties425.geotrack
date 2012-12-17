package fi.jyu.ties425.geotrack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.maps.GeoPoint;

/**
 * class to show a list of locations
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class HistoryFragment extends Fragment {
	/*
	 * define necessary variables
	 */
	OnLocationSelectedListener mCallback;

	private LocationDatabaseHandler ldbh;
	private ListView list;
	private HistoryActivityAdapter adapter;
	private GeoPoint[] locations;
	private String[] time;

	/*
	 * interface for location selected
	 */
	public interface OnLocationSelectedListener {
		public void onLocationSelected(double latitude, double longitude);
	}

	/*
	 * implement the interface OnLocationSelectedListener
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnLocationSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnLocationSelectedListener");
		}
	}

	/*
	 * get the locations-db and retrieve the corresponding locations (incl.
	 * time) which should be shown, afterwards, set the list adapter with this
	 * data
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ldbh = new LocationDatabaseHandler(getActivity());

		locations = ldbh.getTop50Locations();
		time = ldbh.getTop50Timestamps();

		adapter = new HistoryActivityAdapter(getActivity(), locations, time);
	}

	/*
	 * selected the 'layout' which will be extended later on
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_history, container, false);

	}

	/*
	 * load all the items (location + time) into the above selected layout
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		list = (ListView) getActivity().findViewById(R.id.locationList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				mCallback.onLocationSelected(
						locations[(int) id].getLatitudeE6() / 1e6,
						locations[(int) id].getLongitudeE6() / 1e6);
			}
		});
	}

}