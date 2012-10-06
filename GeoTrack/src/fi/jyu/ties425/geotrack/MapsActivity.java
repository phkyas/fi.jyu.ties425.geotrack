package fi.jyu.ties425.geotrack;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;


public class MapsActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        
        MapView mapView = (MapView)findViewById(R.id.myMapView);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.pin_gold);
        MapsActivityItemizedOverlay itemizedoverlay = new MapsActivityItemizedOverlay(drawable, this);
        
        GeoPoint point = new GeoPoint(19240000,-99120000);
        OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        itemizedoverlay.addOverlay(overlayitem);
        mapOverlays.add(itemizedoverlay);
        
        
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

    
}


class LoadMap extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}