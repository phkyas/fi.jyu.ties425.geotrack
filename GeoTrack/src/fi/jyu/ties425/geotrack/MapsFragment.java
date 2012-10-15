package fi.jyu.ties425.geotrack;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class MapsFragment extends Fragment {
	private static final String MAP = "file:///android_asset/theMap.html";
	private WebView webView;

	final static String LATITUDE = "latitude";
	final static String LONGITUDE = "longitude";
	double latitude = 555;
	double longitude = 555;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			latitude = savedInstanceState.getDouble(LATITUDE);
			longitude = savedInstanceState.getDouble(LONGITUDE);
		}

		return inflater.inflate(R.layout.webview_maps, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		Bundle inState = getArguments();

		if (inState != null) {
			updateMapsView(inState.getDouble(LATITUDE),
					inState.getDouble(LONGITUDE));
		} else if (latitude != 555) {
			updateMapsView(latitude, longitude);
		}

	}

	public void updateMapsView(double latitude, double longitude) {

		this.latitude = latitude;
		this.longitude = longitude;

		webView = (WebView) getActivity().findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient());
		webView.addJavascriptInterface(new JavaScriptInterface(),
				"bridgeToMapsFragment");
		webView.loadUrl(MAP);

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble(LATITUDE, latitude);
		outState.putDouble(LONGITUDE, longitude);
	}

	private class JavaScriptInterface {
		@SuppressWarnings("unused")
		public double getLatitude() {
			return latitude;
		}

		@SuppressWarnings("unused")
		public double getLongitude() {
			return longitude;
		}
	}

}
