package fi.jyu.ties425.geotrack;

import com.google.android.maps.GeoPoint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class HistoryFragment extends Fragment {
	OnLocationSelectedListener mCallback;

	private LocationDatabaseHandler ldbh;
	private ListView list;
	private HistoryActivityAdapter adapter;
	private GeoPoint[] locations;
	private String[] time;

	public interface OnLocationSelectedListener {
		public void onLocationSelected(double latitude, double longitude);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnLocationSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnHeadlineSelectedListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ldbh = new LocationDatabaseHandler(getActivity());

		locations = ldbh.getTop50Locations();
		time = ldbh.getTop50Timestamps();

		adapter = new HistoryActivityAdapter(getActivity(), locations, time);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_history, container, false);

	}

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