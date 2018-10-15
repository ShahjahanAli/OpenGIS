package main;

import map.DrawShapeFile;
import main.OpenGIS_MAIN;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import drawingtool.*;
import map.DrawConnectionTest;
import map.DrawElevationProfile;

/**
 *
 * @author deb kanti
 */
public class GameScene implements Serializable {

    public transient OpenGIS_MAIN HTBParent;

    public List<Location> tempcloudList;
    public List<Location> temppolygonList;
    public List<Location> tempCoordinateList;

    public List<DrawElevationProfile> ElevationProfile;
    //--------------------- Shapefile List ----------------------------------//
    public List<Location> tempShapeList;
    public List<List<DrawShapeFile>> ShapeListAll;

    public List<Location> tempShapeFileList;
    public List<DrawShapeFile> ShapeFileList;

    public List<Location> tempShapeFilePolygonList;
    public List<DrawShapeFile> ShapeFilePolygonList;

    public List<Location> tempShapeFilePolylineList;
    public List<DrawShapeFile> ShapeFilePolylineList;

    //--------------------- Shapefile List ----------------------------------//
    //--------------------- Draw Connection ---------------------------------//
    public List<DrawConnectionTest> DrawConnection;
    public List<Location> TempDrawConnection;
    //--------------------- Draw Connection ---------------------------------//

    public List<drawcircle> circleList;
    public List<Location> tempcircleList;
    public List<drawrectangle> rectangleList;
    public List<Location> temprectangleList;


    public List<Location> temptriangleList;

    public List<drawtext> textList;
    public List<Location> temptextList;


    public List<drawline> lineList;
    public List<Location> templineList;

    public GameScene() {
        tempcircleList = new ArrayList<>();

        circleList = new ArrayList<>();
        temprectangleList = new ArrayList<>();
        rectangleList = new ArrayList<>();
        temppolygonList = new ArrayList<>();


        ElevationProfile = new ArrayList<>();
        tempCoordinateList = new ArrayList<>();

        //--------------------- Shapefile List ----------------------------------//
        ShapeListAll = new ArrayList<>();
        tempShapeList = new ArrayList<>();

        ShapeFileList = new ArrayList<>();
        tempShapeFileList = new ArrayList<>();

        ShapeFilePolygonList = new ArrayList<>();
        tempShapeFilePolygonList = new ArrayList<>();

        ShapeFilePolylineList = new ArrayList<>();
        tempShapeFilePolylineList = new ArrayList<>();

        //--------------------- Shapefile List ----------------------------------//
        //--------------------- Draw Connection ---------------------------------//
        DrawConnection = new ArrayList<>();
        TempDrawConnection = new ArrayList<>();
        //--------------------- Draw Connection ---------------------------------//

        temptriangleList = new ArrayList<>();

        temptextList = new ArrayList<>();
        textList = new ArrayList<>();
        tempcloudList = new ArrayList<>();
        lineList = new ArrayList<>();
        templineList = new ArrayList<>();

    }
}
