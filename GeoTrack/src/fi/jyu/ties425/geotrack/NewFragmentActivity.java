package fi.jyu.ties425.geotrack;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * fragment class for maps and lists to enable to different layouts at 'normal'-
 * and 'large'-size screens
 * 
 * @author Philipp Kyas and Jouni Laitinen
 * @version 1.0
 */
public class NewFragmentActivity extends FragmentActivity implements
		HistoryFragment.OnLocationSelectedListener {

	/*
	 * render the ui: 1st the default the layout file is loaded and then the
	 * screen size is checked, depending on the size, either a two frame layout
	 * (large screen) or a single frame layout (not large screens) is then used
	 * 
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_maps);

		// normal screen
		if (findViewById(R.id.fragment_container) != null) {
			if (savedInstanceState != null) {
				return;
			}

			HistoryFragment firstFragment = new HistoryFragment();

			getSupportFragmentManager().beginTransaction()
					.add(R.id.fragment_container, firstFragment).commit();
		}
	}

	/*
	 * define steps at any click on a location in the location list, for large
	 * screens the location can be rendered easily, for all other screen sizes a
	 * new fragments needs to be created and the corresponding locations are
	 * delivered to the new view
	 * 
	 * @see fi.jyu.ties425.geotrack.HistoryFragment.OnLocationSelectedListener#
	 * onLocationSelected(double, double)
	 */
	public void onLocationSelected(double latitude, double longitude) {

		MapsFragment mapsFrag = (MapsFragment) getSupportFragmentManager()
				.findFragmentById(R.id.maps_fragment);

		if (mapsFrag != null) {
			// large screen
			mapsFrag.updateMapsView(latitude, longitude);

		} else {
			// small screen
			MapsFragment newFragment = new MapsFragment();
			Bundle args = new Bundle();
			args.putDouble(MapsFragment.LATITUDE, latitude);
			args.putDouble(MapsFragment.LONGITUDE, longitude);
			newFragment.setArguments(args);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);

			transaction.commit();

		}

	}
}
