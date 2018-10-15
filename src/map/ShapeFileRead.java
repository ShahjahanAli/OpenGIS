/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import main.GameScene;
import main.OpenGIS_MAIN;
import org.nocrala.tools.gis.data.esri.shapefile.ShapeFileReader;
import org.nocrala.tools.gis.data.esri.shapefile.ValidationPreferences;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import org.nocrala.tools.gis.data.esri.shapefile.header.ShapeFileHeader;
import org.nocrala.tools.gis.data.esri.shapefile.shape.AbstractShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.*;
import map.MapLayerClass;
import org.nocrala.tools.gis.data.esri.shapefile.exception.DataStreamEOFException;

/**
 *
 * @author Shahjahan
 */
public class ShapeFileRead {

    private static final Logger LOG = Logger.getLogger(ShapeFileRead.class.getName());
    GameScene gameScene;
    MapLayerClass MapLayer;
    Point center;
    String FileAddress = "E:\\Map Work\\bangladesh-latest-free.shp\\gis.osm_buildings_a_free_1.shp";
    List<String> FilesAddress = new ArrayList<>();
    int MaximumShape = 10;
    String ShapefileType = "Building";
    int TotalShapeFile = 1;
    Color[] ColorList = {Color.BLACK, Color.BLUE, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.DARK_GRAY, Color.pink, Color.orange};
    public List<String> ShapefileNameList = new ArrayList<>();

    public ShapeFileRead() {

    }

    public ShapeFileRead(GameScene gameScene, Point center) {
        this.center = center;
        this.gameScene = gameScene;
    }

    public ShapeFileRead(MapLayerClass MapLayer, Point center, List<String> FileAddress, List<String> FileName, int MaximumShape, String ShapefileType) {
        this.center = center;
        this.MapLayer = MapLayer;
        this.TotalShapeFile = FileAddress.size();
        this.ShapefileNameList = FileName;
        this.FilesAddress = FileAddress;
        this.MaximumShape = MaximumShape;
        this.ShapefileType = ShapefileType;
    }

//    public ShapeFileRead(GameScene gameScene, Point center, List<String> FileAddress, List<String> FileName, int MaximumShape, String ShapefileType) {
//        this.center = center;
//        this.gameScene = gameScene;
////        this.FileAddress = FileAddress;
//        this.TotalShapeFile = FileAddress.size();
//        this.ShapefileNameList = FileName;
//        this.FilesAddress = FileAddress;
//        this.MaximumShape = MaximumShape;
//        this.ShapefileType = ShapefileType;
//    }
    public void ReadShapeFile() throws FileNotFoundException, InvalidShapeFileException, IOException, DataStreamEOFException {

        for (int k = 0; k < TotalShapeFile; k++) {
            FileInputStream is = new FileInputStream(FilesAddress.get(k).toString());
            Color ShapeColor = GetColor();
            ValidationPreferences prefs = new ValidationPreferences();
            prefs.setMaxNumberOfPointsPerShape(26650);
            prefs.setAllowBadContentLength(true);
//            ShapeFileReader r = new ShapeFileReader(is, prefs);
            ShapeFileReader r = new ShapeFileReader(is);
            ShapeFileHeader h = r.getHeader();

            System.out.println("The shape type of this files is " + h.getShapeType());
            int total = 0;
            AbstractShape s;
            while ((s = r.next()) != null) {
                switch (s.getShapeType()) {
                    case POLYLINE:
                        DrawShapeFile DSF_2 = new DrawShapeFile("POLYLINE", this.ShapefileType, ShapeColor, ShapefileNameList.get(k));
                        PolylineShape aPolyline = (PolylineShape) s;
                        int[] FirstPoints_Polyline = aPolyline.getPartFirstPoints();
                        for (int i = 0; i < aPolyline.getNumberOfPoints(); i++) {
                            PointData[] points = aPolyline.getPoints();
                            DSF_2.AdddRawShape(points[i].getY(), points[i].getX(), this.MapLayer, this.center, aPolyline.getNumberOfPoints());
                        }

                        break;

                    case POLYLINE_Z:
                        DrawShapeFile DSF_1 = new DrawShapeFile("POLYLINE", this.ShapefileType, ColorList[k], ShapefileNameList.get(k));
                        PolylineZShape aPolline_Z = (PolylineZShape) s;
                        for (int i = 0; i < aPolline_Z.getNumberOfPoints(); i++) {
                            PointData[] points = aPolline_Z.getPoints();
                            double[] Elevation = aPolline_Z.getZ();
//                            System.out.println("Elevation Data: " + Elevation[i]);
                            DSF_1.AdddRawShape(points[i].getY(), points[i].getX(), this.MapLayer, this.center, aPolline_Z.getNumberOfPoints());
                        }
                        break;

                    case POLYGON_Z:
                        PolygonZShape aPolygon_Z = (PolygonZShape) s;
                        DrawShapeFile DSF_Z = new DrawShapeFile("POLYGON", this.ShapefileType, ColorList[k], ShapefileNameList.get(k));
                        int[] FirstPoints_Z = aPolygon_Z.getPartFirstPoints();
                        for (int i = 0; i < aPolygon_Z.getNumberOfPoints(); i++) {
                            PointData[] points = aPolygon_Z.getPoints();
                            double[] Elevation = aPolygon_Z.getZ();
//                            System.out.println("Elevation Data: " + Elevation[i]);
                            DSF_Z.AdddRawShape(points[i].getY(), points[i].getX(), this.MapLayer, this.center, aPolygon_Z.getNumberOfPoints());
                        }
                        break;

                    case POLYGON:
                        DrawShapeFile DSF = new DrawShapeFile("POLYGON", this.ShapefileType, ShapeColor, ShapefileNameList.get(k));
                        PolygonShape aPolygon = (PolygonShape) s;
                        int[] FirstPoints = aPolygon.getPartFirstPoints();
                        for (int i = 0; i < aPolygon.getNumberOfPoints(); i++) {
                            PointData[] points = aPolygon.getPoints();
                            DSF.AdddRawShape(points[i].getY(), points[i].getX(), this.MapLayer, this.center, aPolygon.getNumberOfPoints());
                        }
                        break;
                    default:
                        System.out.println("Read other type of shape.");
                }

                total++;
                if (total == MaximumShape) {
                    break;
                }
                MapLayer_Form MLF = new MapLayer_Form(total);

            }
            OpenGIS_MAIN.setShapefileLoadComplete(true);
            System.out.println("Total shapes read: " + total);
            is.close();
        }
    }

    double Point1_Lat;
    double Point1_Lon;
    double Point2_Lat;
    double Point2_Lon;

    public void ReadShapeFileTemp() throws FileNotFoundException, InvalidShapeFileException, IOException, DataStreamEOFException {
        FileInputStream is = new FileInputStream("E:\\Map Work\\Map_20180103\\Test_4_20180111_Polygon.shp");
//        DrawShapeFile DSF = new DrawShapeFile();
        // This file has shapes with more than 10000 points each. Therefore, we need
        // to change the validation preferences to increase the limit of points per
        // shape beyond that number. If we don't use the customized preferences, the
        // reader will throw an InvalidShapeFileException.
        ValidationPreferences prefs = new ValidationPreferences();
        prefs.setMaxNumberOfPointsPerShape(16650);
        ShapeFileReader r = new ShapeFileReader(is, prefs);

        ShapeFileHeader h = r.getHeader();
        System.out.println("The shape type of this files is " + h.getShapeType());
        int total = 0;
        AbstractShape s;
        while ((s = r.next()) != null) {
            switch (s.getShapeType()) {
                case POLYLINE:
                    PolylineShape aPolyline = (PolylineShape) s;

                    break;

                case POLYGON:
                    PolygonShape aPolygon = (PolygonShape) s;
                    System.out.println("I read a Polygon with " + aPolygon.getNumberOfParts() + " parts and " + aPolygon.getNumberOfPoints() + " points");
//                    System.out.println("Get Box Max X: " + aPolyline.getBoxMaxX());
//                    System.out.println("Get Box Max Y: " + aPolyline.getBoxMaxY());
//                    System.out.println("Get Box Mini X: " + aPolyline.getBoxMinX());
//                    System.out.println("Get Box Mini Y: " + aPolyline.getBoxMinY());
                    int[] FirstPoints = aPolygon.getPartFirstPoints();
//                    System.out.println("----------------XXXXXXXXXXXX-------------------");
//                    for (int i = 0; i < FirstPoints.length; i++) {
//                        System.out.println("First Points: " + FirstPoints[i]);
//                    }
//                    System.out.println("----------------XXXXXXXXXXXX-------------------");

                    for (int i = 0; i < aPolygon.getNumberOfPoints(); i++) {
                        PointData[] points = aPolygon.getPoints();
//                        System.out.println("-----------------------------------");
//                        System.out.println("Point " + (i + 1) + " X: " + points[i].getX());
//                        System.out.println("Point " + (i + 1) + " Y: " + points[i].getY());
//                        System.out.println("-----------------------------------");
                        if (i == 0) {
                            Point1_Lat = points[i].getX();
                            Point1_Lon = points[i].getY();
                        } else if (i == aPolygon.getNumberOfPoints() - 1) {
                            Point2_Lat = points[i].getX();
                            Point2_Lon = points[i].getY();
                        }

                        if (Point1_Lat == Point2_Lat || Point1_Lon == Point2_Lon) {
                            System.out.println(total + " Closest Point: X: " + Point2_Lat + " Y: " + Point2_Lon);
                        }
                    }
                    Point1_Lat = 0;
                    Point1_Lon = 0;
                    Point2_Lat = 0;
                    Point2_Lon = 0;
                    System.out.println("---------------XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX--------------------");
                    break;
                default:
                    System.out.println("Read other type of shape.");
            }
            total++;

            if (total == MaximumShape) {
                break;
            }
        }
        System.out.println("Total shapes read: " + total);
        is.close();
    }

    public void CheckClosestPoints(double Point1_Lat, double Point1_Lon, double Point2_Lat, double Point2_Lon) {

    }
    Random rand = new Random();
    Color randomColor = null;

    public Color GetColor() {

        // Java 'Color' class takes 3 floats, from 0 to 1.
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        randomColor = new Color(r, g, b);
        return randomColor;
    }

}
