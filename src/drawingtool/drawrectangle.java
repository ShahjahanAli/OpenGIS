package drawingtool;


import main.*;

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
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import utility.GeneralUtil;

/**
 *
 * @author Tanzil
 */
public class drawrectangle implements Serializable {

    double firstPointX, firstPointY, secondPointX, secondPointY, height, width;
    double multiplierForDraw;
    private List<Location> Nodes;
    double centerX, centerY, radius;
    private Color DrawingColor;
    private float DrawingToolThickness;

    public drawrectangle() {

    }

    public drawrectangle(List<Location> Nodes) {
        this.Nodes = new ArrayList<>();
        for (Location loc : gameScene.temprectangleList) {
            this.Nodes.add(loc);
        }
    }

    public void adddrawrectangle(GameScene gameScene, Point2D point, Point center) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

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

        Location forestEdgePoint = new Location((x) * gameScene.HTBParent.one_nm_equivalent_pixel, (y) * gameScene.HTBParent.one_nm_equivalent_pixel, 0.0);
        gameScene.temprectangleList.add(forestEdgePoint);

        int totalTempPoints = gameScene.temprectangleList.size();

        if (totalTempPoints == 2) {
            drawrectangle newRectangle = new drawrectangle(gameScene.temprectangleList);
            newRectangle.setDrawingColor(gameScene.HTBParent.getDrawingToolColor());
            newRectangle.setThikcness(gameScene.HTBParent.getDrawingToolThikcness());
            gameScene.rectangleList.add(newRectangle);
            gameScene.temprectangleList.clear();
        }

    }

    public void DrawTemporaryrectangle(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : gameScene.temprectangleList) {
            g2d.setColor(gameScene.HTBParent.getDrawingToolColor());
            g2d.setColor(Color.BLACK);
            g.fillRect((int) (loc.getX() * one_px_equivalent_naut) - 4, (int) (loc.getY() * one_px_equivalent_naut) - 4, 8, 8);
            prevPoint = loc;
            totPoint++;
        }
    }

    public void DrawAllrectangle(Graphics2D g2d, Graphics g) {

        for (drawrectangle newRectangle : gameScene.rectangleList) {
            g2d.setColor(newRectangle.getDrawingColor());
            g2d.setStroke(new BasicStroke(newRectangle.getThikcness()));
            this.firstPointX = newRectangle.getNodes().get(0).getX() * one_px_equivalent_naut;
            this.firstPointY = newRectangle.getNodes().get(0).getY() * one_px_equivalent_naut;
            this.secondPointX = newRectangle.getNodes().get(1).getX() * one_px_equivalent_naut;
            this.secondPointY = newRectangle.getNodes().get(1).getY() * one_px_equivalent_naut;
            int fpX = (int) Math.abs(firstPointX);
            int fpY = (int) Math.abs(firstPointY);
            int sX = (int) Math.abs(secondPointX);
            int sY = (int) Math.abs(secondPointY);
            int wdt = (int) Math.abs(width);
            int hgt = (int) Math.abs(height);
            g2d.drawLine(fpX, fpY, sX, fpY);
            g2d.drawLine(fpX, fpY, fpX, sY);
            g2d.drawLine(sX, fpY, sX, sY);
            g2d.drawLine(fpX, sY, sX, sY);
        }

    }

    public List<Location> getNodes() {
        return this.Nodes;
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

}
