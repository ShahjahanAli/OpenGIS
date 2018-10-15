package drawingtool;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.openstreetmap.gui.jmapviewer.OsmMercator;

import java.awt.BasicStroke;
import java.awt.Stroke;
import main.GameScene;
import main.Location;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.MapLayer;
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import utility.GeneralUtil;

/**
 *
 * @author Tanzil
 */
public class drawcircle implements Serializable {

    double firstPointX, firstPointY, secondPointX, secondPointY, multiplierForDraw;
    static double lon, lat;
    private List<Location> Nodes;
    int fX;
    double centerX, centerY, radius, Longitude, fLon;
    private Color DrawingColor;
    private float DrawingToolThickness;

    public drawcircle() {

    }

    public drawcircle(List<Location> Nodes) {

        this.Nodes = new ArrayList<>();
        for (Location loc : gameScene.tempcircleList) {
            this.Nodes.add(loc);
        }
    }

    public void adddrawcircle(GameScene gameScene, Point2D point, Point center) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - MapLayer.MAIN.getWidth() / 2;
        fY -= center.y - MapLayer.MAIN.getHeight() / 2;

        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);

        Location CircleEndPoint = new Location((x) * gameScene.HTBParent.one_nm_equivalent_pixel, (y) * gameScene.HTBParent.one_nm_equivalent_pixel, 0.0);
        gameScene.tempcircleList.add(CircleEndPoint);

        int totalTempPoints = MapLayer.tempcircleList.size();

        if (totalTempPoints == 2) {
            drawcircle newCircle = new drawcircle(gameScene.tempcircleList);
            newCircle.setDrawingColor(gameScene.HTBParent.getDrawingToolColor());
            newCircle.setThikcness(gameScene.HTBParent.getDrawingToolThikcness());
            gameScene.circleList.add(newCircle);
            gameScene.tempcircleList.clear();
        }

    }

    public void DrawTemporarycircle(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : gameScene.tempcircleList) {
            g2d.setColor(gameScene.HTBParent.getDrawingToolColor());
            setDrawingColor(gameScene.HTBParent.getDrawingToolColor());
            prevPoint = loc;
            g2d.setColor(Color.BLACK);
            g.fillRect((int) (loc.getX() * one_px_equivalent_naut) - 4, (int) (loc.getY() * one_px_equivalent_naut) - 4, 8, 8);
            totPoint++;
        }
    }

    public void DrawAllcircle(Graphics2D g2d, Graphics g) {
        for (drawcircle circle : gameScene.circleList) {
            g2d.setColor(circle.getDrawingColor());
            g2d.setStroke(new BasicStroke(circle.getThikcness()));
            centerX = circle.getNodes().get(0).getX() * one_px_equivalent_naut;
            centerY = circle.getNodes().get(0).getY() * one_px_equivalent_naut;
            double fpX = circle.getNodes().get(0).getX() * one_px_equivalent_naut;;
            double fpY = circle.getNodes().get(0).getY() * one_px_equivalent_naut;;
            double spX = circle.getNodes().get(1).getX() * one_px_equivalent_naut;;
            double spY = circle.getNodes().get(1).getY() * one_px_equivalent_naut;
            radius = (int) Math.sqrt((spX - fpX) * (spX - fpX) + (spY - fpY) * (spY - fpY));
            int cX = (int) (centerX);
            int cY = (int) (centerY);
            int rad = (int) (radius);
            g2d.drawOval(cX - rad, cY - rad, rad * 2, rad * 2);
        }
    }

    public void setDrawingColor(Color DrawingColor) {
        this.DrawingColor = DrawingColor;
    }

    public Color getDrawingColor() {
        return this.DrawingColor;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
    
    public void setThikcness(float Thikcness) {
        this.DrawingToolThickness = Thikcness;
    }

    public float getThikcness() {
        return this.DrawingToolThickness;
    }
    
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public List<Location> getNodes() {
        return this.Nodes;
    }

}
