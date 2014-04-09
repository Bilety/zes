package com.example.seeker;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.widget.CompoundButton.OnCheckedChangeListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;



public class MapsActivity extends MapActivity{
	private MapView mapView;
	private CheckBox cbSatellite;
	private CheckBox cbStreet;
	private MyLocationOverlay myLocationOverlay;
	private MapController mapController;
	private LocationManager locationManager;
	double latitude;
	double longitude;
	private TextView time;
	private TextView length;
	private Button me;
	private Button ref;
	private Place test;
	private Route route;
	private Drawable drawable;
	private MyItemizedOverlay itemizedoverlay;

	OnCheckedChangeListener cbViewListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton button, boolean state) {
			switch (button.getId()) {
			case R.id.cbSatellite:

				cbStreet.setSelected(false);
				cbStreet.setChecked(false);
				mapView.setSatellite(true);
				break;

			case R.id.cbStreet:

				cbSatellite.setSelected(false);
				cbSatellite.setChecked(false);
				mapView.setSatellite(false);
				break;
			default:
				break;
			}
		}
	};
	

	private LocationListener locationListener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {

		}

		public void onProviderDisabled(String provider) {
		}

		public void onLocationChanged(Location location) {
			latitude = myLocationOverlay.getMyLocation().getLatitudeE6() / 1E6;
			longitude = myLocationOverlay.getMyLocation().getLongitudeE6() / 1E6;
			
			
			/*System.out.println(latitude + " " + longitude);

			//test = new Place("nazwa", "adres", 5.4455, 5.5355);
			
				route = directions(new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6)),
						new GeoPoint((int) (test.getLatitude() * 1E6),
								(int) (test.getLongitude() * 1E6)));
				OverlayItem overlayitem = new OverlayItem(new GeoPoint(
						(int) (test.getLatitude() * 1E6),
						(int) (test.getLongitude() * 1E6)), test.getName(),
						"ul. " + test.getStreet());
				itemizedoverlay.clear();
				itemizedoverlay.addOverlay(overlayitem);
				// marker = new Marker(test.getLatitude(), test.getLongitude());

			RouteOverlay routeOverlay = new RouteOverlay(route, Color.BLUE);
			mapView.getOverlays().remove(1);
			mapView.getOverlays().remove(1);
			mapView.getOverlays().add(routeOverlay);
			mapView.getOverlays().add(itemizedoverlay);
			
			if (route.getLength() < 1000)
				time.setText(route.getLength() + " m");
			else
				time.setText((route.getLength() / 1000) + " km");

			length.setText(route.getDuration());
			mapView.invalidate();
			// length.setText(Double.toString(locationManager.getLastKnownLocation(
			// LocationManager.GPS_PROVIDER).getLatitude()));

			showLocation(location);

		}
	};
	*/

	private void showLocation(Location location) {

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		cbSatellite = (CheckBox) findViewById(R.id.cbSatellite);
		cbStreet = (CheckBox) findViewById(R.id.cbStreet);
		mapView = (MapView) findViewById(R.id.mapview);
		// Listeners

		cbSatellite.setOnCheckedChangeListener(cbViewListener);
		cbStreet.setOnCheckedChangeListener(cbViewListener);
		mapView.setBuiltInZoomControls(true);
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		drawable = this.getResources().getDrawable(R.drawable.bar);
		itemizedoverlay = new MyItemizedOverlay(drawable, this);

		myLocationOverlay.runOnFirstFix(new Runnable() {

			@Override
			public void run() {
				System.out.println("FUFFFF");

				mapView.getController().animateTo(
						myLocationOverlay.getMyLocation());

			}
		});

		if (locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {

			System.out.println("k");

			latitude = locationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER).getLatitude();
			longitude = locationManager.getLastKnownLocation(
					LocationManager.GPS_PROVIDER).getLongitude();
			System.out.println(latitude + " " + longitude);

				test = new Place("nazwa", "adres", 5.4455, 5.5355);
				//System.out.println(test.toString());
				route = directions(new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6)),
						new GeoPoint((int) (test.getLatitude() * 1E6),
								(int) (test.getLongitude() * 1E6)));
				OverlayItem overlayitem = new OverlayItem(new GeoPoint(
						(int) (test.getLatitude() * 1E6),
						(int) (test.getLongitude() * 1E6)), test.getName(),
						"ul. " + test.getStreet());
				itemizedoverlay.addOverlay(overlayitem);
				// marker = new Marker(test.getLatitude(), test.getLongitude());
			
			// Overlaye
			RouteOverlay routeOverlay = new RouteOverlay(route, Color.BLUE);

			mapView.getOverlays().add(myLocationOverlay);
			mapView.getOverlays().add(routeOverlay);
			mapView.getOverlays().add(itemizedoverlay);
			// mapView.getOverlays().add(marker);
			mapView.getController().setZoom(17);

			// Elementy
			mapView.setSatellite(true);
			time = (TextView) findViewById(R.id.czas);
			length = (TextView) findViewById(R.id.dlugosc);
			me = (Button) findViewById(R.id.me);
			ref = (Button) findViewById(R.id.refresh);

			// /////////////////////

			if (route.getLength() < 1000)
				time.setText(route.getLength() + " m");
			else
				time.setText((route.getLength() / 1000) + " km");

			length.setText(route.getDuration());
			mapView.invalidate();

		} else {
			System.out.println("BRAK");
			mapView.getOverlays().add(myLocationOverlay);
			mapView.getController().setZoom(17);
			mapView.setSatellite(true);
			time = (TextView) findViewById(R.id.czas);
			length = (TextView) findViewById(R.id.dlugosc);
			me = (Button) findViewById(R.id.me);
			ref = (Button) findViewById(R.id.refresh);
			mapView.invalidate();
		}
		me.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mapView.getController().animateTo(
						myLocationOverlay.getMyLocation());
			}
		});

		ref.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				latitude = myLocationOverlay.getMyLocation().getLatitudeE6() / 1E6;
				longitude = myLocationOverlay.getMyLocation().getLongitudeE6() / 1E6;
				System.out.println(latitude + " " + longitude);

					route = directions(new GeoPoint((int) (latitude * 1E6),
							(int) (longitude * 1E6)),
							new GeoPoint((int) (test.getLatitude() * 1E6),
									(int) (test.getLongitude() * 1E6)));
					OverlayItem overlayitem = new OverlayItem(new GeoPoint(
							(int) (test.getLatitude() * 1E6), (int) (test
									.getLongitude() * 1E6)), test.getName(),
							"ul. " + test.getStreet());
					itemizedoverlay.clear();
					itemizedoverlay.addOverlay(overlayitem);
					// marker = new Marker(test.getLatitude(),
					// test.getLongitude());
			

				RouteOverlay routeOverlay = new RouteOverlay(route, Color.BLUE);
				mapView.getOverlays().remove(1);
				mapView.getOverlays().remove(1);
				mapView.getOverlays().add(routeOverlay);
				mapView.getOverlays().add(itemizedoverlay);
				// mapView.getOverlays().add(marker);
				if (route.getLength() < 1000)
					time.setText(route.getLength() + " m");
				else
					time.setText((route.getLength() / 1000) + " km");

				length.setText(route.getDuration());
				mapView.invalidate();
			}
		});

	}

	private Route directions(final GeoPoint start, final GeoPoint dest) {
		Parser parser;

		String jsonURL = "http://maps.google.com/maps/api/directions/json?";
		final StringBuffer sBuf = new StringBuffer(jsonURL);
		sBuf.append("origin=");
		sBuf.append(start.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(start.getLongitudeE6() / 1E6);
		sBuf.append("&destination=");
		sBuf.append(dest.getLatitudeE6() / 1E6);
		sBuf.append(',');
		sBuf.append(dest.getLongitudeE6() / 1E6);
		sBuf.append("&sensor=true&mode=walking");
		parser = new GoogleParser(sBuf.toString());
		Route r = parser.parse();
		return r;
	}

	@Override
	protected boolean isRouteDisplayed() {

		return false;
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	protected void onResume() {
		super.onResume();
		myLocationOverlay.enableMyLocation();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 2, locationListener);

	}

	protected void onStop() {

		myLocationOverlay.disableMyLocation();
		locationManager.removeUpdates(locationListener);
		super.onStop();
	}


	
}
