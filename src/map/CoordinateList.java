/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;


import java.awt.Point;
import java.awt.geom.Point2D;
import static java.lang.Math.toDegrees;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import main.GameScene;

/**
 *
 * @author Shahjahan
 */
public class CoordinateList {

    private static final long RADIUS_OF_EARTH = 6371000;
    int i = 0;
    List<Integer> scores = new ArrayList<Integer>();
    List<Double> ElevationData = new ArrayList<Double>();
    List<ElevationWithLatLon> ElevationDataWithLatLon = new ArrayList<>();

    public List<ElevationWithLatLon> getElevationDataWithLatLon() {
        return ElevationDataWithLatLon;
    }

    public void setElevationDataWithLatLon(List<ElevationWithLatLon> ElevationDataWithLatLon) {
        this.ElevationDataWithLatLon = ElevationDataWithLatLon;
    }

    public CoordinateList(double Start_Lat, double Start_lon, double End_lat, double End_Lon, String NoGraph) {

        double lat1 = Start_Lat;
        double lng1 = Start_lon;
        String FirstPoint = "" + lat1 + "," + lng1;
        double lat2 = End_lat;
        double lng2 = End_Lon;
        String SecondPoint = "" + lat2 + "," + lng2;

        System.out.println("Distance between points: " + Distance(lat1, lng1, lat2, lng2) + " Km");

        int interval = 20;

        MockLocation start = new MockLocation(lat1, lng1);
        MockLocation end = new MockLocation(lat2, lng2);
        double azimuth = calculateBearing(start, end);
        ArrayList<MockLocation> coords = getLocations(interval, azimuth, start, end);
        for (MockLocation mockLocation : coords) {
            ElevationReader ER = new ElevationReader(mockLocation.lat, mockLocation.lng, i);
            ElevationWithLatLon EWLL = new ElevationWithLatLon();
            EWLL.setElevation((double) ER.getElevation());
            EWLL.setLat(mockLocation.lat);
            EWLL.setLon(mockLocation.lng);
            ElevationDataWithLatLon.add(EWLL);
            i++;
        }
        setElevationDataWithLatLon(ElevationDataWithLatLon);
//        for (int k = 0; k < ElevationDataWithLatLon.size(); k++) {
//            System.out.println("Lat: " + ElevationDataWithLatLon.get(k).getLat() + " Lon: " + ElevationDataWithLatLon.get(k).getLon() + " Elevation: " + ElevationDataWithLatLon.get(k).getElevation());
//        }
    }

    public CoordinateList(double Start_Lat, double Start_lon, double End_lat, double End_Lon) {

        double lat1 = Start_Lat;
        double lng1 = Start_lon;
        String FirstPoint = "" + lat1 + "," + lng1;
        double lat2 = End_lat;
        double lng2 = End_Lon;
        String SecondPoint = "" + lat2 + "," + lng2;

        System.out.println("Distance between points: " + Distance(lat1, lng1, lat2, lng2) + " Km");

        int interval = 20;

        MockLocation start = new MockLocation(lat1, lng1);
        MockLocation end = new MockLocation(lat2, lng2);
        double azimuth = calculateBearing(start, end);
        ArrayList<MockLocation> coords = getLocations(interval, azimuth, start, end);
        for (MockLocation mockLocation : coords) {
            ElevationReader ER = new ElevationReader(mockLocation.lat, mockLocation.lng, i);
            scores.add(ER.getElevation());
            ElevationData.add((double) ER.getElevation());
            i++;
        }
        int MaxElevation = Collections.max(scores);
        int MinElevation = Collections.min(scores);
        int Difference = MaxElevation - MinElevation;

        GraphPanel GP = new GraphPanel();
        java.text.DecimalFormat df = new java.text.DecimalFormat("####.##");
        double IntervelDistance = Double.parseDouble(df.format(UnitDistance));
        System.out.println("Formated Double: " + IntervelDistance);
        GP.createAndShowGui(ElevationData, MaxElevation, IntervelDistance, FirstPoint, SecondPoint);
    }

    public CoordinateList(double Start_Lat, double Start_lon, double End_lat, double End_Lon, GameScene gameScene, Point2D point, Point center) {
        DrawElevationProfile DEP = new DrawElevationProfile();
        int interval = 100;
        double lat1 = Start_Lat;
        double lng1 = Start_lon;

        double lat2 = End_lat;
        double lng2 = End_Lon;

        MockLocation start = new MockLocation(lat1, lng1);
        MockLocation end = new MockLocation(lat2, lng2);
        double azimuth = calculateBearing(start, end);
        System.out.println(azimuth);
        ArrayList<MockLocation> coords = getLocations(interval, azimuth, start, end);

        for (MockLocation mockLocation : coords) {
//            ElevationReader ER = new ElevationReader(mockLocation.lat, mockLocation.lng, "S");
            DEP.adddrawpolygon(mockLocation.lat, mockLocation.lng, gameScene, point, center);
        }
    }

    private double Distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.60934;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
 /*::	This function converts decimal degrees to radians						 :*/
 /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
 /*::	This function converts radians to decimal degrees						 :*/
 /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    static class MockLocation {

        double lat;
        double lng;

        public MockLocation(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public String toString() {
            return "(" + lat + "," + lng + ")";
        }
    }

    public static void main(String args[]) {
        // point interval in meters
        int interval = 10;
        // direction of line in degrees
        //start point
        double lat1 = 43.97076;
        double lng1 = 12.72543;
        // end point
        double lat2 = 43.969730;
        double lng2 = 12.728294;

        MockLocation start = new MockLocation(lat1, lng1);
        MockLocation end = new MockLocation(lat2, lng2);
        double azimuth = calculateBearing(start, end);
        System.out.println(azimuth);
        ArrayList<MockLocation> coords = getLocations(interval, azimuth, start, end);

        for (MockLocation mockLocation : coords) {
            System.out.println(mockLocation.lat + ", " + mockLocation.lng);
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(2);
        System.out.println("Formated Double: " + Double.parseDouble(df.format(UnitDistance)));

    }

    static double UnitDistance = 0;

    private static ArrayList<MockLocation> getLocations(int interval, double azimuth, MockLocation start, MockLocation end) {
        double d = getPathLength(start, end);
        System.out.println("Distance: " + d + " meter");
        UnitDistance = d / interval;
        System.out.println("Unit Distance: " + UnitDistance + " meter");
        double coveredDist = UnitDistance;
        System.out.println("Distance covered: " + coveredDist + " meter");
        ArrayList<MockLocation> coords = new ArrayList<>();
        coords.add(new MockLocation(start.lat, start.lng));
        for (double distance = UnitDistance; distance < d - UnitDistance; distance += UnitDistance) {
            System.out.println("Distance covered: " + coveredDist + " meter");
            MockLocation coord = getDestinationLatLng(start.lat, start.lng, azimuth, coveredDist);
            coveredDist += UnitDistance;
            coords.add(coord);
        }
        System.out.println("Distance covered: " + coveredDist + " meter");
//        for (int distance = 0; distance < dist; distance += interval) {
//            MockLocation coord = getDestinationLatLng(start.lat, start.lng, azimuth, coveredDist);
//            coveredDist += interval;
//            System.out.println("Distance covered: " + coveredDist + " meter");
//            coords.add(coord);
//        }
        coords.add(new MockLocation(end.lat, end.lng));
        return coords;
    }

    private static double getPathLength(MockLocation start, MockLocation end) {
        double lat1Rads = Math.toRadians(start.lat);
        double lat2Rads = Math.toRadians(end.lat);
        double deltaLat = Math.toRadians(end.lat - start.lat);

        double deltaLng = Math.toRadians(end.lng - start.lng);
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.cos(lat1Rads) * Math.cos(lat2Rads) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = RADIUS_OF_EARTH * c;
        return d;
    }

    private static MockLocation getDestinationLatLng(double lat, double lng, double azimuth, double distance) {
        double radiusKm = RADIUS_OF_EARTH / 1000; //Radius of the Earth in km
        double brng = Math.toRadians(azimuth); //Bearing is degrees converted to radians.
        double d = distance / 1000; //Distance m converted to km
        double lat1 = Math.toRadians(lat); //Current dd lat point converted to radians
        double lon1 = Math.toRadians(lng); //Current dd long point converted to radians
        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / radiusKm) + Math.cos(lat1) * Math.sin(d / radiusKm) * Math.cos(brng));
        double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d / radiusKm) * Math.cos(lat1), Math.cos(d / radiusKm) - Math.sin(lat1) * Math.sin(lat2));
        //convert back to degrees
        lat2 = Math.toDegrees(lat2);
        lon2 = Math.toDegrees(lon2);
        return new MockLocation(lat2, lon2);
    }

    private static double calculateBearing(MockLocation start, MockLocation end) {
        double startLat = Math.toRadians(start.lat);
        double startLong = Math.toRadians(start.lng);
        double endLat = Math.toRadians(end.lat);
        double endLong = Math.toRadians(end.lng);
        double dLong = endLong - startLong;
        double dPhi = Math.log(Math.tan((endLat / 2.0) + (Math.PI / 4.0)) / Math.tan((startLat / 2.0) + (Math.PI / 4.0)));
        if (Math.abs(dLong) > Math.PI) {
            if (dLong > 0.0) {
                dLong = -(2.0 * Math.PI - dLong);
            } else {
                dLong = (2.0 * Math.PI + dLong);
            }
        }
        double bearing = (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
        return bearing;
    }

}
