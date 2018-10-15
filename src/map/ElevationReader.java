package map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Shahjahan
 */

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.OpenGIS_MAIN;
import org.openstreetmap.gui.jmapviewer.OsmMercator;

public class ElevationReader {

    private static final int SECONDS_PER_MINUTE = 60;

    public static final String HGT_EXT = ".hgt";

    // alter these values for different SRTM resolutions
    public static final int HGT_RES = 3; // resolution in arc seconds
    public static final int HGT_ROW_LENGTH = 1201; // number of elevation values per line
    public static final int HGT_VOID = -32768; // magic number which indicates 'void data' in HGT file
    public static String Location = "";
    private final HashMap<String, ShortBuffer> cache = new HashMap<>();
    public static File[] HGTFiles = null;
    double Latitude;
    double Longitude;
    public int Elevation = 0;

    public int getElevation() {
        return Elevation;
    }

    public void setElevation(int Elevation) {
        this.Elevation = Elevation;
    }

    public ElevationReader() {

    }

    public ElevationReader(Double Lat, Double Lon, int i) {
        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        Latitude = Double.parseDouble(df.format(Lat));
        Longitude = Double.parseDouble(df.format(Lon));

        HGTFiles = new File(Location).listFiles();
        getElevationFromHgt(Latitude, Longitude);

        System.out.println("Point " + i + " Elevation: " + getElevation());

    }

    public ElevationReader(Double Lat, Double Lon) {

        try {
            Location = HeightFileLocation() + "/hgt/";

            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(7);

            Latitude = Double.parseDouble(df.format(Lat));
            Longitude = Double.parseDouble(df.format(Lon));

            HGTFiles = new File(Location).listFiles();
            getElevationFromHgt(Latitude, Longitude);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ElevationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String HeightFileLocation() throws FileNotFoundException {
        String MapAddress = null;
        FileReader fileReader = new FileReader("C:/EWTSS/EWSSDirectory/MapAddress.txt");
        Scanner scanner = new Scanner(fileReader);
        while (scanner.hasNext()) {
            MapAddress = scanner.next();
        }
        return MapAddress;
    }

    public ElevationReader(Point2D point) {
        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        Latitude = Double.parseDouble(df.format(latValue));
        Longitude = Double.parseDouble(df.format(lonValue));
//        System.out.println("Lat Lon: " + Latitude + " " + Longitude);
        HGTFiles = new File(Location).listFiles();
        getElevationFromHgt(Latitude, Longitude);
    }

    public static void main(String[] args) {
        HGTFiles = new File(Location).listFiles();
        ElevationReader HR = new ElevationReader();
        HR.getElevationFromHgt(23.7638033, 90.3873324);
    }

    public double getElevationFromHgt(Double Lat, Double Lon) {

        double NoElevation = 0;
        String[] Lat_Data = Double.toString(Lat).split("\\.");
        String[] Lon_Data = Double.toString(Lon).split("\\.");
//        System.out.println("Double to String: " + Lat_Data[0]);
        try {
            String file = getHgtFileName(Integer.parseInt(Lat_Data[0]), Integer.parseInt(Lon_Data[0]));
            if (!cache.containsKey(file)) {
                cache.put(file, null);

                for (int i = 0; i < HGTFiles.length; i++) {
                    File f = new File(Location + file);
                    if (f.exists()) {
                        ShortBuffer data = readHgtFile(Location + file);
                        cache.put(file, data);
                        break;
                    }
                }
            }
            return readElevation(Lat, Lon);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return NoElevation;
        } catch (Exception ioe) {
            ioe.printStackTrace(System.err);
            return NoElevation;
        }

    }

    @SuppressWarnings("resource")
    private ShortBuffer readHgtFile(String file) throws Exception {
        FileChannel fc = null;
        ShortBuffer sb = null;
        try {
            fc = new FileInputStream(file).getChannel();
            ByteBuffer bb = ByteBuffer.allocateDirect((int) fc.size());
            while (bb.remaining() > 0) {
                fc.read(bb);
            }
            bb.flip();
            sb = bb.order(ByteOrder.BIG_ENDIAN).asShortBuffer();
        } finally {
            if (fc != null) {
                fc.close();
            }
        }
        return sb;
    }

    int i = 0;

    public double readElevation(Double Lat, Double Lon) {
        double NoElevation = 0;
        String[] Lat_Data = Double.toString(Lat).split("\\.");
        String[] Lon_Data = Double.toString(Lon).split("\\.");
        String tag = getHgtFileName(Integer.parseInt(Lat_Data[0]), Integer.parseInt(Lon_Data[0]));
        ShortBuffer sb = cache.get(tag);
        if (sb == null) {
            return NoElevation;
        }
        double fLat = frac(Lat) * SECONDS_PER_MINUTE;
        double fLon = frac(Lon) * SECONDS_PER_MINUTE;
        int row = (int) Math.round(fLat * SECONDS_PER_MINUTE / HGT_RES);
        int col = (int) Math.round(fLon * SECONDS_PER_MINUTE / HGT_RES);

        row = HGT_ROW_LENGTH - row;
        int cell = (HGT_ROW_LENGTH * (row - 1)) + col;

        if (cell < sb.limit()) {
            short ele = sb.get(cell);
            if (ele == HGT_VOID) {
                return NoElevation;
            } else {
//                System.out.println("Elevation: " + ele);
                setElevation(ele);
                return ele;
            }
        } else {
            return NoElevation;
        }
    }

    public String getHgtFileName(int Lattitude, int Longitude) {
        int lat = Lattitude;
        int lon = Longitude;

        String latPref = "N";
        if (lat < 0) {
            latPref = "S";
        }

        String lonPref = "E";
        if (lon < 0) {
            lonPref = "W";
        }
//        System.out.println("HGT File Name: " + String.format("%s%02d%s%03d%s", latPref, lat, lonPref, lon, HGT_EXT));
        return String.format("%s%02d%s%03d%s", latPref, lat, lonPref, lon, HGT_EXT);
    }

    public static double frac(double d) {
        long iPart;
        double fPart;

        // Get user input
        iPart = (long) d;
        fPart = d - iPart;
        return fPart;
    }
}
