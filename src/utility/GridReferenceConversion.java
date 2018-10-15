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
public class GridReferenceConversion {

    public GridReferenceConversion() {

    }

    public double[] utm2LatLon(String UTM) {
        UTM2LatLon c = new UTM2LatLon();
        return c.convertUTMToLatLong(UTM);
    }

    public String latLon2UTM(double latitude, double longitude) {
        LatLon2UTM c = new LatLon2UTM();
        return c.convertLatLonToUTM(latitude, longitude);

    }

    public String latLon2MGRUTM(double latitude, double longitude) {
        LatLon2MGRUTM c = new LatLon2MGRUTM();
        return c.convertLatLonToMGRUTM(latitude, longitude);
    }


    //------- Returns only Easting Value
    public String latLon2MGRUTM_Easting(double latitude, double longitude) {
        LatLon2MGRUTM c = new LatLon2MGRUTM();
        return c.convertLatLonToMGRUTM_Easting(latitude, longitude);
    }

    //-------Returns only Northing Value
    public String latLon2MGRUTM_Northing(double latitude, double longitude) {
        LatLon2MGRUTM c = new LatLon2MGRUTM();
        return c.convertLatLonToMGRUTM_Northing(latitude, longitude);
    }
    
//    public double[] mgrutm2LatLon(String MGRUTM) {
//        MGRUTM2LatLon c = new MGRUTM2LatLon();
//        return c.convertMGRUTMToLatLong(MGRUTM);
//    }
//    public static void main(String args[]) {
//        GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();
//
//        String UTM = gridReferenceConversion.latLon2UTM(23.176957, 89.162669);
//        String MGRS = gridReferenceConversion.latLon2MGRUTM(23.176957, 89.162669);
//        System.out.println("UTM: " + UTM);
//        System.out.println("MGRS: " + MGRS);
//    }
}
