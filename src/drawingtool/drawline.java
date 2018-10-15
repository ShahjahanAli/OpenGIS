/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import main.GameScene;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import main.Location;
import main.PressedButtonEnum;
import utility.GeneralUtil;

/**
 *
 * @author Tanzil
 */
public class drawline implements Serializable {

    double firstPointX, firstPointY, secondPointX, secondPointY, multiplierForDraw;
    double lon11, lat11;
    static double lon, lat;
    int fpX, fpY, spX, spY;
    public static double angle;
    private List<Location> Nodes;

    Color cl;
    private Color DrawingColor;
    String lineType;
    private float DrawingToolThickness;



    public drawline() {

    }

    public drawline(List<Location> Nodes) {
        this.Nodes = new ArrayList<>();
        for (Location loc : gameScene.templineList) {     //assigns all nodes from temporary forest nodes
            this.Nodes.add(loc);
        }
    }

    public void adddrawline(GameScene gameScene, Point2D point, Point center) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        int fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - gameScene.HTBParent.getWidth() / 2;
        fY -= center.y - gameScene.HTBParent.getHeight() / 2;

        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);

        getLon();
        Location forestEdgePoint = new Location((x) * gameScene.HTBParent.one_nm_equivalent_pixel, (y) * gameScene.HTBParent.one_nm_equivalent_pixel, 0.0);
        gameScene.templineList.add(forestEdgePoint);
        int totalTempPoints = gameScene.templineList.size();


        if (totalTempPoints == 2) {
            drawline newline = new drawline(gameScene.templineList);
            newline.setDrawingColor(gameScene.HTBParent.getDrawingToolColor());
            newline.setThikcness(gameScene.HTBParent.getDrawingToolThikcness());
            if (gameScene.HTBParent.lastPressedButton == PressedButtonEnum.DRAW_LINE) {
                newline.setLineType("normal");
            } else if (gameScene.HTBParent.lastPressedButton == PressedButtonEnum.DRAW_DASHED_LINE) {
                newline.setLineType("dashed");
            }

            gameScene.lineList.add(newline);
            gameScene.templineList.clear();
           
        }

        if (totalTempPoints >= 4) {
            gameScene.templineList.get(0);
        }

    }

    public void DrawTemporaryline(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : gameScene.templineList) {
            setDrawingColor(gameScene.HTBParent.getDrawingToolColor());
            prevPoint = loc;
            g2d.setColor(Color.BLACK);
            g.fillRect((int) (loc.getX() * one_px_equivalent_naut) - 4, (int) (loc.getY() * one_px_equivalent_naut) - 4, 8, 8);
            totPoint++;
        }
    }

    public void DrawAllline(Graphics2D g2d, Graphics g) {

        for (drawline line : gameScene.lineList) {
            if (line.getLineType().equals("dashed")) {
                float dash[] = {10.0f};
                BasicStroke bs11 = new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
                g2d.setStroke(bs11);
                g2d.setColor(line.getDrawingColor());
                g2d.draw(new Line2D.Double(line.getNodes().get(0).getX() * one_px_equivalent_naut, line.getNodes().get(0).getY() * one_px_equivalent_naut, line.getNodes().get(1).getX() * one_px_equivalent_naut, line.getNodes().get(1).getY() * one_px_equivalent_naut));
                g2d.setStroke(new BasicStroke());
            } else {
                g2d.setColor(line.getDrawingColor());
                g2d.setStroke(new BasicStroke(line.getThikcness()));
                g2d.draw(new Line2D.Double(line.getNodes().get(0).getX() * one_px_equivalent_naut, line.getNodes().get(0).getY() * one_px_equivalent_naut, line.getNodes().get(1).getX() * one_px_equivalent_naut, line.getNodes().get(1).getY() * one_px_equivalent_naut));
                g2d.setStroke(new BasicStroke());
            }

        }

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
