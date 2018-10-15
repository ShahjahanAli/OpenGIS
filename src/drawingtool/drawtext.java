package drawingtool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import main.GameScene;
import main.Location;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.MapLayer;
import static main.OpenGIS_MAIN.gameScene;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import utility.GeneralUtil;


/**
 *
 * @author Tanzil
 */
public class drawtext implements Serializable {

    double firstPointX, firstPointY, secondPointX, secondPointY, multiplierForDraw;
    int fpX, fpY, spX, spY;
    private Point point;
    private List<Location> Nodes;
    private int stringX, stringY;
    private Color DrawingColor;

    public drawtext() {

    }

    public drawtext(List<Location> Nodes) {
        this.Nodes = new ArrayList<>();
        for (Location loc : gameScene.temptextList) {
            this.Nodes.add(loc);
        }
    }

    public void adddrawline(GameScene gameScene, Point2D point, Point center) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        double lon = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(lonValue));
        double lat = GeneralUtil.getDoubleTo2DecimalPoint2(GeneralUtil.getMinuteFromDecimal(latValue));

        double fLon = GeneralUtil.getDecimalFromMinute(lon);
        double fLat = GeneralUtil.getDecimalFromMinute(lat);

        int fX = OsmMercator.LonToX(fLon, OpenGIS_MAIN.currentZoom);
        int fY = OsmMercator.LatToY(fLat, OpenGIS_MAIN.currentZoom);

        fX -= center.x - MapLayer.MAIN.getWidth() / 2;
        fY -= center.y - MapLayer.MAIN.getHeight() / 2;

        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);

        Location forestEdgePoint = new Location((x) * gameScene.HTBParent.one_nm_equivalent_pixel, (y) * gameScene.HTBParent.one_nm_equivalent_pixel, 0.0);
        gameScene.temptextList.add(forestEdgePoint);

        int totalTempPoints = gameScene.temptextList.size();

        if (totalTempPoints == 2) {
            drawtext newline = new drawtext(gameScene.temptextList);
            gameScene.textList.add(newline);
            gameScene.temptextList.clear();
        }

        if (totalTempPoints >= 4) {
            gameScene.temptextList.get(0);
        }

    }

    public void DrawTemporaryline(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : gameScene.temptextList) {
            g2d.setColor(Color.RED);
            prevPoint = loc;
            totPoint++;
        }
    }

    public void DrawAllline(Graphics2D g2d, Graphics g) {

        for (drawtext line : gameScene.textList) {
            g2d.setColor(Color.red);
            stringX = (int) (line.getNodes().get(0).getX() * one_px_equivalent_naut);
            stringY = (int) (line.getNodes().get(0).getY() * one_px_equivalent_naut);
            point = new Point((int) (stringX - 10), (int) (stringY + 1));
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

}
