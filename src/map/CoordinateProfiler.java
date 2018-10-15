/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import static java.lang.Math.*;
import static java.lang.Math.toRadians;
import main.GameScene;

/**
 *
 * @author Shahjahan
 */
public class CoordinateProfiler {

    public CoordinateProfiler() {
    }

    public void CalculateLatLon(double latitude, double longitude, double distanceInMetres, double bearing) {
        double brngRad = toRadians(bearing);
        double latRad = toRadians(latitude);
        double lonRad = toRadians(longitude);
        int earthRadiusInMetres = 6371000;
        double distFrac = distanceInMetres / earthRadiusInMetres;

        double latitudeResult = asin(sin(latRad) * cos(distFrac) + cos(latRad) * sin(distFrac) * cos(brngRad));
        double a = atan2(sin(brngRad) * sin(distFrac) * cos(latRad), cos(distFrac) - sin(latRad) * sin(latitudeResult));
        double longitudeResult = (lonRad + a + 3 * PI) % (2 * PI) - PI;

        System.out.println("latitude: " + toDegrees(latitudeResult) + ", longitude: " + toDegrees(longitudeResult));
    }

    public void CalculateLatLon360(double latitude, double longitude, double distanceInMetres, double bearing) {
        for (int Bearing = 0; Bearing < 360.0; Bearing++) {
            double brngRad = toRadians(Bearing);
            double latRad = toRadians(latitude);
            double lonRad = toRadians(longitude);
            int earthRadiusInMetres = 6371000;
            double distFrac = distanceInMetres / earthRadiusInMetres;

            double latitudeResult = asin(sin(latRad) * cos(distFrac) + cos(latRad) * sin(distFrac) * cos(brngRad));
            double a = atan2(sin(brngRad) * sin(distFrac) * cos(latRad), cos(distFrac) - sin(latRad) * sin(latitudeResult));
            double longitudeResult = (lonRad + a + 3 * PI) % (2 * PI) - PI;
            CoordinateList CL = new CoordinateList(latitude, longitude, toDegrees(latitudeResult), toDegrees(longitudeResult));
            System.out.println("Bearing " + Bearing + " Degree has " + "latitude: " + toDegrees(latitudeResult) + ", longitude: " + toDegrees(longitudeResult));
        }
    }

    public void CalculateLatLon360(double latitude, double longitude, double distanceInMetres, GameScene gameScene, Point2D point, Point center) {
//        DrawElevationProfile DEP = new DrawElevationProfile();
        for (int Bearing = 0; Bearing < 360.0; Bearing+=10) {
            double brngRad = toRadians(Bearing);
            double latRad = toRadians(latitude);
            double lonRad = toRadians(longitude);
            int earthRadiusInMetres = 6371000;
            double distFrac = distanceInMetres / earthRadiusInMetres;

            double latitudeResult = asin(sin(latRad) * cos(distFrac) + cos(latRad) * sin(distFrac) * cos(brngRad));
            double a = atan2(sin(brngRad) * sin(distFrac) * cos(latRad), cos(distFrac) - sin(latRad) * sin(latitudeResult));
            double longitudeResult = (lonRad + a + 3 * PI) % (2 * PI) - PI;
            CoordinateList CL = new CoordinateList(latitude, longitude, toDegrees(latitudeResult), toDegrees(longitudeResult),gameScene, point, center);
            System.out.println("Bearing " + Bearing + " Degree has " + "latitude: " + toDegrees(latitudeResult) + ", longitude: " + toDegrees(longitudeResult));

//            DEP.adddrawpolygon(toDegrees(latitudeResult), toDegrees(longitudeResult), gameScene, point, center);
        }
    }

    public static void main(String[] arg) {
        new CoordinateProfiler().CalculateLatLon360(23.7903713, 90.3792, 2980.0, 164.41);
    }

}
