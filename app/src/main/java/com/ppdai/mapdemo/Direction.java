package com.ppdai.mapdemo;

import android.text.TextUtils;

import com.amap.api.services.route.RouteSearch;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunhuahui on 2017/10/31.
 */

public class Direction {
    public String status;

    public ArrayList<GeocodedWayPoint> geocoded_waypoints;

    public ArrayList<Route> routes;
    private RouteSearch routeSearch;

    public static class GeocodedWayPoint {
        public String geocoder_status;
        public String place_id;
    }

    public static class Route implements Serializable {
        public LatLngBounds latLgnBounds;
        public Bound bounds;
        public List<LatLng> points;
        public List<Leg> legs;
    }

    public static class Leg implements Serializable {
        public D distance;
        public D duration;
        public LatLn end_location;
        public LatLn start_location;
        public List<Step> steps;
        public String end_address;
        public String start_address;
    }

    public static class Step implements Serializable {
        public D distance;
        public D duration;
        public LatLn end_location;
        public LatLn start_location;
        public String travel_mode;
        public Polyline polyline;
    }

    public static class Polyline implements Serializable {
        public String points;
    }

    public static class D implements Serializable {
        public String text;
        public String value;
    }

    public static class Bound implements Serializable {
        public LatLn northeast;
        public LatLn southwest;
    }

    public static class LatLn implements Serializable {
        public double lat;
        public double lng;
    }

    public void parse() {
        if (routes == null) return;
        for (Route route : routes) {
            route.points = new ArrayList<>();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(new LatLng(route.bounds.northeast.lat, route.bounds.northeast.lng));
            builder.include(new LatLng(route.bounds.southwest.lat, route.bounds.southwest.lng));
            route.latLgnBounds = builder.build();
            if (route.legs == null) return;
            for (Leg leg : route.legs) {
                for (Step step : leg.steps) {
                    if (step.polyline == null || TextUtils.isEmpty(step.polyline.points)) return;
                    route.points.addAll(decodePolyLine(step.polyline.points));
                }
            }
        }
    }

    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
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

            decoded.add(new LatLng(lat / 100000d, lng / 100000d));
        }

        return decoded;
    }
}
