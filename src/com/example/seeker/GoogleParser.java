package com.example.seeker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import android.util.Log;


public class GoogleParser extends XMLParser implements Parser{
	
	private int distance;

	public GoogleParser(String feedUrl) {
		super(feedUrl);
	}

	//TODO delete this shit
	public int dist() {
		int distance = 0;
		try {
			final String result = convertStreamToString(this.getInputStream());
			final JSONObject json = new JSONObject(result);
			final JSONObject jsonRoute = json.getJSONArray("routes")
					.getJSONObject(0);
			final JSONObject leg = jsonRoute.getJSONArray("legs")
					.getJSONObject(0);
			distance = leg.getJSONObject("distance").getInt("value");
		} catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " + feedUrl);
		}
		return distance;
	}

	public Route parse() {
		
		final String result = convertStreamToString(this.getInputStream());
		final Route route = new Route();
		final Segment segment = new Segment();
		try {
			final JSONObject json = new JSONObject(result);
			final JSONObject jsonRoute = json.getJSONArray("routes")
					.getJSONObject(0);
			final JSONObject leg = jsonRoute.getJSONArray("legs")
					.getJSONObject(0);
			final JSONArray steps = leg.getJSONArray("steps");
			final int numSteps = steps.length();
			route.setName(leg.getString("start_address") + " to "
					+ leg.getString("end_address"));
			route.setCopyright(jsonRoute.getString("copyrights"));
			route.setLength(leg.getJSONObject("distance").getInt("value"));
			route.setDuration(leg.getJSONObject("duration").getString("text"));
			if (!jsonRoute.getJSONArray("warnings").isNull(0)) {
				route.setWarning(jsonRoute.getJSONArray("warnings")
						.getString(0));
			}

		
			for (int i = 0; i < numSteps; i++) {
				final JSONObject step = steps.getJSONObject(i);
				final JSONObject start = step.getJSONObject("start_location");
				final GeoPoint position = new GeoPoint(
						(int) (start.getDouble("lat") * 1E6),
						(int) (start.getDouble("lng") * 1E6));
				segment.setPoint(position);
				final int length = step.getJSONObject("distance").getInt(
						"value");
				distance += length;
				segment.setLength(length);
				segment.setDistance(distance / 1000);
				segment.setInstruction(step.getString("html_instructions")
						.replaceAll("<(.*?)*>", ""));
				
				route.addPoints(decodePolyLine(step.getJSONObject("polyline")
						.getString("points")));
				route.addSegment(segment.copy());
			}

		} catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " + feedUrl);
		}
		return route;
	}


	private static String convertStreamToString(final InputStream input) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				input));
		final StringBuilder sBuf = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sBuf.append(line);
			}
		} catch (IOException e) {
			Log.e(e.getMessage(), "Google parser, stream2string");
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				Log.e(e.getMessage(), "Google parser, stream2string");
			}
		}
		return sBuf.toString();
	}

	private List<GeoPoint> decodePolyLine(final String poly) {
		int len = poly.length();
		int index = 0;
		List<GeoPoint> decoded = new ArrayList<GeoPoint>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
			int b;
			int shift = 0;
			int result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = poly.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			decoded.add(new GeoPoint((int) (lat * 1E6 / 1E5),
					(int) (lng * 1E6 / 1E5)));
		}

		return decoded;
	}
}
