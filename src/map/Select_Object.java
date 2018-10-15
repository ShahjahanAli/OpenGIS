/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.Point;
import java.awt.geom.Point2D;
import javax.swing.JOptionPane;
import main.GameScene;
import main.Location;
import main.OpenGIS_MAIN;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import utility.GeneralUtil;

/**
 *
 * @author Shahjahan
 */
public class Select_Object {

    private GameScene gameScene;
    private MapLayerClass MapLayer;
    private Point2D point;
    private double lon;
    private double lat;
    protected Point center;
    int MapID;
    double x;
    double y;

    public Select_Object(MapLayerClass MapLayer, Point2D point, Point center) {
        this.gameScene = gameScene;
        this.MapLayer = MapLayer;
        this.point = point;
        this.center = center;

        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);

        this.lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        this.lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        int fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - MapLayer.MAIN.getWidth() / 2;
        fY -= center.y - MapLayer.MAIN.getHeight() / 2;

        x = (fX - OpenGIS_MAIN.translate_x) * MapLayer.MAIN.one_nm_equivalent_pixel;
        y = (-fY + OpenGIS_MAIN.translate_y) * MapLayer.MAIN.one_nm_equivalent_pixel;

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        double Latitude = Double.parseDouble(df.format(latValue));
        double Longitude = Double.parseDouble(df.format(lonValue));

        this.lon = Longitude;
        this.lat = Latitude;
    }

    public void SelectShapefile() {
        Location clickedLocation = new Location((x), (y), 0.0);
        int totPoint;
        double polygonDeleteDist;
        boolean polygonDeleteFlag = false;
        Location prevPoint = new Location();
        for (DrawShapeFile Shape : MapLayer.ShapeFileList) {
            totPoint = 0;
            for (Location loc : Shape.getNodes()) {
                if (totPoint > 0) {
                    polygonDeleteDist = this.pointToSegmentDistance(clickedLocation, prevPoint, loc);
                    if (polygonDeleteDist < 10) {
                        if (Shape.getShapeType().equals("POLYGON")) {
                            System.out.println("Polygon Selected");
                            Shape.setSelected(true);
                        } else if (Shape.getShapeType().equals("POLYLINE")) {
                            System.out.println("Polyline Selected");
                            Shape.setSelected(true);
                        }
                        polygonDeleteFlag = true;
                    }
                }
                prevPoint = loc;
                totPoint++;
                if (polygonDeleteFlag == true) {
                    break;
                }
            }

            Location firstPoint = Shape.getNodes().get(0);
            polygonDeleteDist = this.pointToSegmentDistance(clickedLocation, prevPoint, firstPoint);
            if (polygonDeleteDist < 10) {
                polygonDeleteFlag = true;
            }
            if (polygonDeleteFlag == true) {
                break;
            }
        }
    }

    public void EditShapefile() {
        Location clickedLocation = new Location((x), (y), 0.0);
        int totPoint;
        double polygonDeleteDist;
        boolean polygonDeleteFlag = false;
        Location prevPoint = new Location();
        for (DrawShapeFile Shape : MapLayer.ShapeFileList) {
            totPoint = 0;
            for (Location loc : Shape.getNodes()) {
                if (totPoint > 0) {
                    polygonDeleteDist = this.pointToSegmentDistance(clickedLocation, prevPoint, loc);
                    if (polygonDeleteDist < 10) {
                        if (Shape.getShapeType().equals("POLYGON")) {
                            System.out.println("Polygon Selected");
                            Shape.setEditing(true);
                        } else if (Shape.getShapeType().equals("POLYLINE")) {
                            System.out.println("Polyline Selected");
                            Shape.setEditing(true);
                        }
                        polygonDeleteFlag = true;
                    }
                }
                prevPoint = loc;
                totPoint++;
                if (polygonDeleteFlag == true) {
                    break;
                }
            }

            Location firstPoint = Shape.getNodes().get(0);
            polygonDeleteDist = this.pointToSegmentDistance(clickedLocation, prevPoint, firstPoint);
            if (polygonDeleteDist < 10) {
                polygonDeleteFlag = true;
            }
            if (polygonDeleteFlag == true) {
                break;
            }
        }
    }

    public double pointToSegmentDistance(Location P, Location M, Location N) {
        double A = P.getX() * MapLayer.MAIN.one_px_equivalent_naut - M.getX() * MapLayer.MAIN.one_px_equivalent_naut;
        double B = P.getY() * MapLayer.MAIN.one_px_equivalent_naut - M.getY() * MapLayer.MAIN.one_px_equivalent_naut;
        double C = N.getX() * MapLayer.MAIN.one_px_equivalent_naut - M.getX() * MapLayer.MAIN.one_px_equivalent_naut;
        double D = N.getY() * MapLayer.MAIN.one_px_equivalent_naut - M.getY() * MapLayer.MAIN.one_px_equivalent_naut;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) //in case of 0 length line
        {
            param = dot / len_sq;
        }

        double xx, yy;

        if (param < 0) {
            xx = M.getX() * MapLayer.MAIN.one_px_equivalent_naut;
            yy = M.getY() * MapLayer.MAIN.one_px_equivalent_naut;
        } else if (param > 1) {
            xx = N.getX() * MapLayer.MAIN.one_px_equivalent_naut;
            yy = N.getY() * MapLayer.MAIN.one_px_equivalent_naut;
        } else {
            xx = M.getX() * MapLayer.MAIN.one_px_equivalent_naut + param * C;
            yy = M.getY() * MapLayer.MAIN.one_px_equivalent_naut + param * D;
        }

        double dx = P.getX() * MapLayer.MAIN.one_px_equivalent_naut - xx;
        double dy = P.getY() * MapLayer.MAIN.one_px_equivalent_naut - yy;
        return Math.sqrt(dx * dx + dy * dy);
    }

}
