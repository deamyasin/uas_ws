package utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.foodorder.BuildConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.foodorder.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created on 21-03-2020.
 */
public class DrawRoute {

    private Context context;

    public DrawRoute(Context context) {
        this.context = context;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        private OnRouteListener onRouteListener;

        public DownloadTask(OnRouteListener onRouteListener) {
            this.onRouteListener = onRouteListener;
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                URL mURL = new URL(url[0]);

                urlConnection = (HttpURLConnection) mURL.openConnection();

                urlConnection.connect();

                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();
                iStream.close();
                urlConnection.disconnect();

                return data;
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(onRouteListener);

            parserTask.execute(result);

        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        private JSONObject jObject;
        private OnRouteListener onRouteListener;

        public ParserTask(OnRouteListener onRouteListener) {
            this.onRouteListener = onRouteListener;
        }

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = null;

            List<HashMap<String, String>> path = new ArrayList<>();

            for (int i = 0; i < result.size(); i++) {
                lineOptions = new PolylineOptions();

                path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                    double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(ContextCompat.getColor(context, R.color.colorOrange));
                lineOptions.geodesic(true);

            }

            // Drawing polyline in the Google Map for the i-th route
            onRouteListener.loadingDone(jObject.toString(), path, lineOptions);
        }
    }

    public String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String mOriginal = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String mDestination = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = mOriginal + "&" + mDestination + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + BuildConfig.DIRECTION_KEY;

        Log.e("TEST", url);

        return url;
    }

    public interface OnRouteListener {
        public void loadingDone(String response, List<HashMap<String, String>> pathList, PolylineOptions lineOptions);
    }

}
