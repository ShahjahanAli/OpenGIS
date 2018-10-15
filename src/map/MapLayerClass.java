/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import drawingtool.drawcircle;
import drawingtool.drawline;
import drawingtool.drawrectangle;
import drawingtool.drawtext;
import java.util.ArrayList;
import java.util.List;
import main.Location;
import main.OpenGIS_MAIN;

/**
 *
 * @author Shahjahan
 */
public class MapLayerClass {

    public transient OpenGIS_MAIN MAIN;
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

    public List<drawtext> textList;
    public List<Location> temptextList;

    public List<drawline> lineList;
    public List<Location> templineList;

    public MapLayerClass() {
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
        lineList = new ArrayList<>();
        templineList = new ArrayList<>();

        tempcircleList = new ArrayList<>();

        circleList = new ArrayList<>();
        temprectangleList = new ArrayList<>();
        rectangleList = new ArrayList<>();

    }

}
