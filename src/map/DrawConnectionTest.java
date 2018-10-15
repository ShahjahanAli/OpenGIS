/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import main.GameScene;
import main.Location;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.MapLayer;
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import main.PressedButtonEnum;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import utility.GeneralUtil;

/**
 *
 * @author Shahjahan
 */
public class DrawConnectionTest {

    double firstPointX, firstPointY, secondPointX, secondPointY, multiplierForDraw;
    double lon11, lat11;
    private static double lon, lat;

    public static double angle;
    private List<Location> Nodes;

    private Color cl;
    private Color DrawingColor;
    private String lineType;
    private float DrawingToolThickness;
    private DrawShapeFile DF;
    private static double FirstPointElevation = 0;
    private static double SecondPointElevation = 0;

    private static double Start_Lat;
    private static double Start_Lon;
    private static double End_Lat;
    private static double End_Lon;
    private List<ElevationWithLatLon> ElevationDataWithLatLon = new ArrayList<>();
    private Point center;

    public DrawConnectionTest() {
        DF = new DrawShapeFile();
    }

    public DrawConnectionTest(List<Location> Nodes) {
        this.Nodes = new ArrayList<>();
        for (Location loc : gameScene.TempDrawConnection) {     //assigns all nodes from temporary forest nodes
            this.Nodes.add(loc);
        }
    }

    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    public List<ElevationWithLatLon> getElevationDataWithLatLon() {
        return ElevationDataWithLatLon;
    }

    public void setElevationDataWithLatLon(List<ElevationWithLatLon> ElevationDataWithLatLon) {
        this.ElevationDataWithLatLon = ElevationDataWithLatLon;
    }

    public double getFirstPointElevation() {
        return FirstPointElevation;
    }

    public void setFirstPointElevation(double FirstPointElevation) {
        this.FirstPointElevation = FirstPointElevation;
    }

    public double getSecondPointElevation() {
        return SecondPointElevation;
    }

    public void setSecondPointElevation(double SecondPointElevation) {
        this.SecondPointElevation = SecondPointElevation;
    }

    public static double getStart_Lat() {
        return Start_Lat;
    }

    public static void setStart_Lat(double Start_Lat) {
        DrawConnectionTest.Start_Lat = Start_Lat;
    }

    public static double getStart_Lon() {
        return Start_Lon;
    }

    public static void setStart_Lon(double Start_Lon) {
        DrawConnectionTest.Start_Lon = Start_Lon;
    }

    public static double getEnd_Lat() {
        return End_Lat;
    }

    public static void setEnd_Lat(double End_Lat) {
        DrawConnectionTest.End_Lat = End_Lat;
    }

    public static double getEnd_Lon() {
        return End_Lon;
    }

    public static void setEnd_Lon(double End_Lon) {
        DrawConnectionTest.End_Lon = End_Lon;
    }

    public void adddrawline(GameScene gameScene, Point2D point, Point center) {
        setCenter(center);
        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        double Latitude = Double.parseDouble(df.format(latValue));
        double Longitude = Double.parseDouble(df.format(lonValue));

        int fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - MapLayer.MAIN.getWidth() / 2;
        fY -= center.y - MapLayer.MAIN.getHeight() / 2;

        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);

        Location SelectedPoint = new Location((x) * MapLayer.MAIN.one_nm_equivalent_pixel, (y) * MapLayer.MAIN.one_nm_equivalent_pixel, 0.0);
        MapLayer.TempDrawConnection.add(SelectedPoint);
        int totalTempPoints = MapLayer.TempDrawConnection.size();

        if (totalTempPoints == 1) {
            ElevationReader ER = new ElevationReader(latValue, lonValue);
            setFirstPointElevation(ER.getElevation());
            setStart_Lat(Latitude);
            setStart_Lon(Longitude);
        } else {
            setEnd_Lat(Latitude);
            setEnd_Lon(Longitude);
        }

        if (totalTempPoints == 2) {
            ElevationReader ER = new ElevationReader(latValue, lonValue);
            setSecondPointElevation(ER.getElevation());

            DrawConnectionTest newline = new DrawConnectionTest(MapLayer.TempDrawConnection);
            newline.setDrawingColor(MapLayer.MAIN.getDrawingToolColor());
            newline.setThikcness(MapLayer.MAIN.getDrawingToolThikcness());

//            CoordinateList CL = new CoordinateList(getStart_Lat(), getStart_Lon(), getEnd_Lat(), getEnd_Lon(), "No Graph");
//            this.ElevationDataWithLatLon = CL.getElevationDataWithLatLon();
//            newline.setElevationDataWithLatLon(ElevationDataWithLatLon);
            for (int k = 0; k < ElevationDataWithLatLon.size(); k++) {
//                System.out.println("Lat: " + ElevationDataWithLatLon.get(k).getLat() + " Lon: " + ElevationDataWithLatLon.get(k).getLon() + " Elevation: " + ElevationDataWithLatLon.get(k).getElevation());

            }
//            System.out.println("First Point Elevation: " + getFirstPointElevation() + " Second Point Elevation: " + getSecondPointElevation());
//            newline.setFirstPointElevation(ElevationDataWithLatLon.get(0).getElevation());
//            newline.setSecondPointElevation(ElevationDataWithLatLon.get(ElevationDataWithLatLon.size() - 1).getElevation());
//            newline.setCenter(this.getCenter());
            if (MapLayer.MAIN.lastPressedButton == PressedButtonEnum.DRAW_CONNECTION_TEST) {
                newline.setLineType("normal");
            }

//            DrawShapeFile DF = new DrawShapeFile();
//            DF.isBuildingExistsInLOS(MapLayer.TempDrawConnection.get(0), MapLayer.TempDrawConnection.get(1));
            MapLayer.DrawConnection.add(newline);
            MapLayer.TempDrawConnection.clear();
        }
    }

    public void DrawReflectedLine(Location P1) {

    }

    public void DrawTemporaryline(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : MapLayer.TempDrawConnection) {
            setDrawingColor(MapLayer.MAIN.getDrawingToolColor());
            prevPoint = loc;
            g2d.setColor(Color.BLACK);
            g.fillRect((int) (loc.getX() * one_px_equivalent_naut) - 4, (int) (loc.getY() * one_px_equivalent_naut) - 4, 8, 8);
            totPoint++;
        }
    }

    public void DrawAllline(Graphics2D g2d, Graphics g) {
        for (DrawConnectionTest line : MapLayer.DrawConnection) {
            g2d.setColor(line.getDrawingColor());
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new Line2D.Double(line.getNodes().get(0).getX() * one_px_equivalent_naut, line.getNodes().get(0).getY() * one_px_equivalent_naut, line.getNodes().get(1).getX() * one_px_equivalent_naut, line.getNodes().get(1).getY() * one_px_equivalent_naut));
            g2d.setStroke(new BasicStroke());
            MarkElevationPoint(g2d, line.getElevationDataWithLatLon(), line.getCenter());
        }
    }

    public void MarkElevationPoint(Graphics2D g2d, List<ElevationWithLatLon> EWLL, Point center) {
        for (int k = 0; k < EWLL.size(); k++) {
            double fLonValue = EWLL.get(k).getLon();
            double fLatValue = EWLL.get(k).getLat();

            int fX = OsmMercator.LonToX(fLonValue, OpenGIS_MAIN.currentZoom);
            int fY = OsmMercator.LatToY(fLatValue, OpenGIS_MAIN.currentZoom);

            fX -= center.x - MapLayer.MAIN.getWidth() / 2;
            fY -= center.y - MapLayer.MAIN.getHeight() / 2;

            double xI = (fX - OpenGIS_MAIN.translate_x);
            double yI = (-fY + OpenGIS_MAIN.translate_y);

            xI = xI * MapLayer.MAIN.one_nm_equivalent_pixel;
            yI = yI * MapLayer.MAIN.one_nm_equivalent_pixel;

            if (EWLL.get(0).getElevation() < EWLL.get(k).getElevation() && EWLL.get(EWLL.size() - 1).getElevation() < EWLL.get(k).getElevation()) {
                g2d.setColor(Color.red);
            } else {
                g2d.setColor(Color.black);
            }

            g2d.fillRect((int) (xI * one_px_equivalent_naut) + 4, (int) (yI * one_px_equivalent_naut) + 4, 8, 8);

        }

    }

    public void CheckBuildingExistInLOS() {

    }

    public void setDrawingColor(Color DrawingColor) {
        this.DrawingColor = DrawingColor;
    }

    public Color getDrawingColor() {
        return this.DrawingColor;
    }

    public void setThikcness(float Thikcness) {
        this.DrawingToolThickness = Thikcness;
    }

    public float getThikcness() {
        return this.DrawingToolThickness;
    }

    public List<Location> getNodes() {
        return this.Nodes;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLat() {
        return lat;
    }

    public void setX(double lat) {
        this.lat = lat;
    }

    public double getX() {
        return lat;
    }

    public double cartesianToTrueNorth(double angleCartesian) {
        double temp = 360 - (angleCartesian - 90);
        if (temp >= 360) {
            return (temp - 360);
        }
        return temp;
    }

    public float getAngle() {
        DecimalFormat oneDForm = new DecimalFormat("###.##");
        return Float.valueOf(oneDForm.format(angle));
    }

    public void setColor(Color color) {
        this.cl = color;
    }

    public Color getColor() {
        return cl;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getLineType() {
        return this.lineType;
    }

}
