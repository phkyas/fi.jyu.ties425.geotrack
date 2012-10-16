package fi.jyu.ties425.geotrack;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class NewFragmentActivity extends FragmentActivity implements
		HistoryFragment.OnLocationSelectedListener {

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
