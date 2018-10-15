/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import utility.*;

/**
 *
 * @author Shahjahan
 */
public class MGRUTM2LatLon {

    public double degreeToRadian(double degree) {
        return degree * Math.PI / 180;
    }

    public double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }

    private double POW(double a, double b) {
        return Math.pow(a, b);
    }

    private double SIN(double value) {
        return Math.sin(value);
    }

    private double COS(double value) {
        return Math.cos(value);
    }

    private double TAN(double value) {
        return Math.tan(value);
    }

    protected String getLongZone(double longitude) {
        double longZone = 0;
        if (longitude < 0.0) {
            longZone = ((180.0 + longitude) / 6) + 1;
        } else {
            longZone = (longitude / 6) + 31;
        }
        String val = String.valueOf((int) longZone);
        if (val.length() == 1) {
            val = "0" + val;
        }
        return val;
    }

    protected double getNorthing(double latitude) {
        double northing = K1 + K2 * p * p + K3 * POW(p, 4);
        if (latitude < 0.0) {
            northing = 10000000 + northing;
        }
        return northing;
    }

    protected double getEasting() {
        return 500000 + (K4 * p + K5 * POW(p, 3));
    }

//    public double[] convertMGRUTMToLatLong(String mgrutm) {
//        double[] latlon = {0.0, 0.0};
//        // 02CNR0634657742
//        int zone = Integer.parseInt(mgrutm.substring(0, 2));
//        String latZone = mgrutm.substring(2, 3);
//
//        String digraph1 = mgrutm.substring(3, 4);
//        String digraph2 = mgrutm.substring(4, 5);
//        easting = Double.parseDouble(mgrutm.substring(5, 10));
//        northing = Double.parseDouble(mgrutm.substring(10, 15));
//
//        LatZones lz = new LatZones();
//        double latZoneDegree = lz.getLatZoneDegree(latZone);
//
//        double a1 = latZoneDegree * 40000000 / 360.0;
//        double a2 = 2000000 * Math.floor(a1 / 2000000.0);
//
//        Digraphs digraphs = new Digraphs();
//
//        double digraph2Index = digraphs.getDigraph2Index(digraph2);
//
//        double startindexEquator = 1;
//        if ((1 + zone % 2) == 1) {
//            startindexEquator = 6;
//        }
//
//        double a3 = a2 + (digraph2Index - startindexEquator) * 100000;
//        if (a3 <= 0) {
//            a3 = 10000000 + a3;
//        }
//        northing = a3 + northing;
//
//        zoneCM = -183 + 6 * zone;
//        double digraph1Index = digraphs.getDigraph1Index(digraph1);
//        int a5 = 1 + zone % 3;
//        double[] a6 = {16, 0, 8};
//        double a7 = 100000 * (digraph1Index - a6[a5 - 1]);
//        easting = easting + a7;
//
//        setVariables();
//
//        double latitude = 0;
//        latitude = 180 * (phi1 - fact1 * (fact2 + fact3 + fact4)) / Math.PI;
//
//        if (latZoneDegree < 0) {
//            latitude = 90 - latitude;
//        }
//
//        double d = _a2 * 180 / Math.PI;
//        double longitude = zoneCM - d;
//
//        if (getHemisphere(latZone).equals("S")) {
//            latitude = -latitude;
//        }
//
//        latlon[0] = latitude;
//        latlon[1] = longitude;
//        return latlon;
//    }

    // Lat Lon to UTM variables
    // equatorial radius
    double equatorialRadius = 6378137;

    // polar radius
    double polarRadius = 6356752.314;

    // flattening
    double flattening = 0.00335281066474748;// (equatorialRadius-polarRadius)/equatorialRadius;

    // inverse flattening 1/flattening
    double inverseFlattening = 298.257223563;// 1/flattening;

    // Mean radius
    double rm = POW(equatorialRadius * polarRadius, 1 / 2.0);

    // scale factor
    double k0 = 0.9996;

    // eccentricity
    double e = Math.sqrt(1 - POW(polarRadius / equatorialRadius, 2));

    double e1sq = e * e / (1 - e * e);

    double n = (equatorialRadius - polarRadius)
            / (equatorialRadius + polarRadius);

    // r curv 1
    double rho = 6368573.744;

    // r curv 2
    double nu = 6389236.914;

    // Calculate Meridional Arc Length
    // Meridional Arc
    double S = 5103266.421;

    double A0 = 6367449.146;

    double B0 = 16038.42955;

    double C0 = 16.83261333;

    double D0 = 0.021984404;

    double E0 = 0.000312705;

    // Calculation Constants
    // Delta Long
    double p = -0.483084;

    double sin1 = 4.84814E-06;

    // Coefficients for UTM Coordinates
    double K1 = 5101225.115;

    double K2 = 3750.291596;

    double K3 = 1.397608151;

    double K4 = 214839.3105;

    double K5 = -2.995382942;

    double A6 = -1.00541E-07;
}
