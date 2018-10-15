/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package map;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import main.GameScene;
import main.Location;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import utility.GeneralUtil;

/**
 *
 * @author Tanzil
 */
public class DrawElevationProfile implements Serializable {

    private List<Location> Nodes;
    Thread t = new Thread();
    double firstPointX, firstPointY, secondPointX, secondPointY;
    private Path2D pathPolygon;

    public DrawElevationProfile() {
    }

    public DrawElevationProfile(List<Location> Nodes) {

        this.Nodes = new ArrayList<>();
        pathPolygon = new Path2D.Double();
        int cont = 0;
        for (Location loc : gameScene.tempCoordinateList) {     //assigns all nodes from temporary forest nodes
            if (cont < gameScene.tempCoordinateList.size() - 1) {
                this.Nodes.add(loc);
                if (cont == 0) {
                    pathPolygon.moveTo(loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut);
                } else {
                    pathPolygon.lineTo(loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut);
                }
            }
            cont++;
        }
        pathPolygon.closePath();

        gameScene.tempCoordinateList.clear();
    }

    public void adddrawpolygon(double latValue, double lonValue, GameScene gameScene, Point2D point, Point center) {
//        double lonValue = OsmMercator.XToLon((int) (point.getX()), HTB_MAIN.currentZoom);
//        double latValue = OsmMercator.YToLat((int) (point.getY()), HTB_MAIN.currentZoom);

        double lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        double lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        int fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - gameScene.HTBParent.getWidth() / 2;
        fY -= center.y - gameScene.HTBParent.getHeight() / 2;

        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);

        //last point added in the forest edge
        Location polygonEdgePoint = new Location((x) * gameScene.HTBParent.one_nm_equivalent_pixel, (y) * gameScene.HTBParent.one_nm_equivalent_pixel, 0.0);
        gameScene.tempCoordinateList.add(polygonEdgePoint);

        int totalTempPoints = gameScene.tempCoordinateList.size();
        if (totalTempPoints >= 3) {     //polygon must be consist of at least threepoints
            Location firstPoint, lastPoint;
            firstPoint = gameScene.tempCoordinateList.get(0);
            lastPoint = gameScene.tempCoordinateList.get(totalTempPoints - 1);
            double physicalDistance = (double) GeneralUtil.distance2D(firstPoint.getX(), firstPoint.getY(), lastPoint.getX(), lastPoint.getY());    //is in nautical mile
            physicalDistance *= 1.852;  //1 nautical mile = 1.852 kilometer
            if (physicalDistance < 0.1) {
                DrawElevationProfile newPolygon = new DrawElevationProfile(gameScene.tempCoordinateList);
                gameScene.ElevationProfile.add(newPolygon);
//                gameScene.temppolygonList.clear();
            }
        }

    }

    public void DrawTemporarypolygon(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : gameScene.tempCoordinateList) {
            g2d.setColor(Color.BLACK);
            g.fillRect((int) (loc.getX() * one_px_equivalent_naut) - 4, (int) (loc.getY() * one_px_equivalent_naut) - 4, 8, 8);

            if (totPoint > 0) {
                g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut));
            }
            prevPoint = loc;
            totPoint++;
        }
    }

    public void DrawAllpolygon(final Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (DrawElevationProfile poly : gameScene.ElevationProfile) {
            totPoint = 0;
            for (Location loc : poly.getNodes()) {
                g2d.setColor(Color.BLACK);
                if (totPoint > 0) {
                    g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut));
                }
                prevPoint = loc;
                totPoint++;
            }

            Location firstPoint = poly.getNodes().get(0);
            g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, firstPoint.getX() * one_px_equivalent_naut, firstPoint.getY() * one_px_equivalent_naut));

        }

    }

    public List<Location> getNodes() {
        return this.Nodes;
    }

}
