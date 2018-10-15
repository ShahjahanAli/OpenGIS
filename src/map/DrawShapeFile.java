/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import main.Location;
import main.OpenGIS_MAIN;
import static main.OpenGIS_MAIN.MapLayer;
import static main.OpenGIS_MAIN.one_px_equivalent_naut;
import org.openstreetmap.gui.jmapviewer.OsmMercator;
import utility.GeneralUtil;
import map.MapLayerClass;

/**
 *
 * @author Tanzil
 */
public class DrawShapeFile extends MouseAdapter implements MouseListener, Serializable {

    private List<Location> Nodes;
    private Thread t = new Thread();
    private double firstPointX, firstPointY, secondPointX, secondPointY;
    private Path2D pathShape;
    private int TotalVertices = 0;
    private String ShapeType = "POLYGON";
    private String ShapefileType = "Building";
    private boolean Selected = false;
    private boolean Editing = false;
    private Color ShapeColor = Color.BLACK;
    private boolean Drawable = true;
    private String ShapefileName = null;
    private boolean ExistInLOS = false;
    private Graphics2D g2d;
    private Location Point1;
    private Location Point2;

    public DrawShapeFile() {

    }

    public DrawShapeFile(String ShapeType, String ShapefileType, Color ShapeColor, String FileName) {
        setShapeType(ShapeType);
        setShapefileType(ShapefileType);
        setShapeColor(ShapeColor);
        setShapefileName(FileName);
    }

    public Location getPoint1() {
        return Point1;
    }

    public void setPoint1(Location Point1) {
        this.Point1 = Point1;
    }

    public Location getPoint2() {
        return Point2;
    }

    public void setPoint2(Location Point2) {
        this.Point2 = Point2;
    }

    public boolean isExistInLOS() {
        return ExistInLOS;
    }

    public void setExistInLOS(boolean ExistInLOS) {
        this.ExistInLOS = ExistInLOS;
    }

    public String getShapefileName() {
        return ShapefileName;
    }

    public void setShapefileName(String ShapefileName) {
        this.ShapefileName = ShapefileName;
    }

    public boolean isDrawable() {
        return Drawable;
    }

    public void setDrawable(boolean Drawable) {
        this.Drawable = Drawable;
    }

    public Color getShapeColor() {
        return ShapeColor;
    }

    public void setShapeColor(Color ShapeColor) {
        this.ShapeColor = ShapeColor;
    }

    public String getShapeType() {
        return ShapeType;
    }

    public void setShapeType(String ShapefileType) {
        this.ShapeType = ShapefileType;
    }

    public String getShapefileType() {
        return ShapefileType;
    }

    public void setShapefileType(String ShapefileType) {
        this.ShapefileType = ShapefileType;
    }

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean Selected) {
        this.Selected = Selected;
    }

    public boolean isEditing() {
        return Editing;
    }

    public void setEditing(boolean Editing) {
        this.Editing = Editing;
    }

    public DrawShapeFile(List<Location> Nodes) {
        this.Nodes = new ArrayList<>();
        int cont = 0;
        pathShape = new Path2D.Double();
        for (Location loc : MapLayer.tempShapeFileList) {
            if (cont < MapLayer.tempShapeFileList.size() - 1) {
                this.Nodes.add(loc);
                if (cont == 0) {
                    pathShape.moveTo(loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut);
                } else {
                    pathShape.lineTo(loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut);
                }
            }
            cont++;
        }
        pathShape.closePath();
        MapLayer.tempShapeFileList.clear();

    }

    public void AdddRawShape(double latValue, double lonValue, MapLayerClass MapLayer, Point center, int TotalVertices) {
        this.TotalVertices = TotalVertices;
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

        Location SpaheFilePolygon = new Location((x) * MapLayer.MAIN.one_nm_equivalent_pixel, (y) * MapLayer.MAIN.one_nm_equivalent_pixel, 0.0);
        MapLayer.tempShapeFileList.add(SpaheFilePolygon);

        int totalTempPoints = MapLayer.tempShapeFileList.size();
        if (totalTempPoints == TotalVertices) {
            DrawShapeFile newShapeFilePolygon = new DrawShapeFile(MapLayer.tempShapeFileList);
            newShapeFilePolygon.setShapeType(this.getShapeType());
            newShapeFilePolygon.setShapefileType(this.getShapefileType());
            newShapeFilePolygon.setShapeColor(this.getShapeColor());
            newShapeFilePolygon.setShapefileName(this.getShapefileName());
            newShapeFilePolygon.setDrawable(this.isDrawable());
            newShapeFilePolygon.setExistInLOS(false);
            MapLayer.ShapeFileList.add(newShapeFilePolygon);
        }
    }

    public void DrawTemporaryShape(Graphics2D g2d, Graphics g) {
        int totPoint = 0;
        Location prevPoint = new Location();
        for (Location loc : MapLayer.tempShapeFileList) {
            if (this.getShapefileType().equals("Building")) {
                g2d.setColor(Color.BLACK);
            } else if (this.getShapefileType().equals("Road")) {
                g2d.setColor(Color.BLUE);
            } else if (this.getShapefileType().equals("Railways")) {
                g2d.setColor(Color.DARK_GRAY);
            }

            g2d.setColor(this.getShapeColor());

            if (totPoint > 0) {
                g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut));
            }
            prevPoint = loc;
            totPoint++;
        }
    }

    public void DrawAllShape(Graphics2D g2d, Graphics g) {
        this.g2d = g2d;
        int totPoint = 0;
        Location prevPoint = new Location();
        Location firstPoint = new Location();
        Location loc = new Location();
        for (DrawShapeFile poly : MapLayer.ShapeFileList) {
            if (poly.isDrawable()) {
                totPoint = 0;
                Polygon pbase = new Polygon();  //this is for the base contour
                for (int i = 0; i < poly.getNodes().size(); i++) {
                    firstPoint = poly.getNodes().get(0);
                    loc = poly.getNodes().get(i);
                    pbase.addPoint((int) (loc.getX() * one_px_equivalent_naut), (int) (loc.getY() * one_px_equivalent_naut));

                    if (poly.getShapefileType().equals("Building")) {
                        g2d.setColor(Color.BLACK);
                    } else if (poly.getShapefileType().equals("Road")) {
                        g2d.setColor(Color.BLUE);
                    } else if (poly.getShapefileType().equals("Railways")) {
                        g2d.setColor(Color.DARK_GRAY);
                    }

                    g2d.setColor(poly.getShapeColor());

                    if (poly.isExistInLOS()) {
                        g2d.setStroke(new BasicStroke(5));
                        g2d.setColor(Color.ORANGE);
                        g2d.draw(new Line2D.Double(poly.getPoint1().getX() * one_px_equivalent_naut, poly.getPoint1().getY() * one_px_equivalent_naut, poly.getPoint2().getX() * one_px_equivalent_naut, poly.getPoint2().getY() * one_px_equivalent_naut));
                        g2d.setColor(Color.BLACK);
                        g2d.setStroke(new BasicStroke(2));
                    } else {
//                        g2d.setColor(Color.BLACK);
//                        g2d.setStroke(new BasicStroke(2));
                    }

                    if (poly.isSelected()) {
                        g2d.setStroke(new BasicStroke(4));
                        g2d.setColor(Color.RED);
                    } else {
                        g2d.setStroke(new BasicStroke(2));
                    }

                    if (poly.isEditing()) {
                        for (int ii = 0; ii < poly.getNodes().size(); ii++) {
                            g2d.fillRect((int) (poly.getNodes().get(ii).getX() * one_px_equivalent_naut) - 5, (int) (poly.getNodes().get(ii).getY() * one_px_equivalent_naut) - 5, 10, 10);
                        }
                    }

//                    if (totPoint > 0) {
//                        g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, loc.getX() * one_px_equivalent_naut, loc.getY() * one_px_equivalent_naut));
//                    }
                    prevPoint = loc;
                    totPoint++;
//                    if (poly.getShapeType().equals("POLYGON") || poly.getShapeType().equals("POLYLINE")) {
//                        if (totPoint == poly.getNodes().size()) {
//                            g2d.draw(new Line2D.Double(prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut, firstPoint.getX() * one_px_equivalent_naut, firstPoint.getY() * one_px_equivalent_naut));
//                        }
//                    }
                }
//                if (poly.getShapeType().equals("POLYGON") || poly.getShapeType().equals("POLYLINE")) {
//                    g2d.draw(new Line2D.Double(firstPoint.getX() * one_px_equivalent_naut, firstPoint.getY() * one_px_equivalent_naut, prevPoint.getX() * one_px_equivalent_naut, prevPoint.getY() * one_px_equivalent_naut));
//                }
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(Color.BLUE);
                g2d.fillPolygon(pbase);   //base contour


            }
        }

    }

    Location StartPoint;
    Location EndPoint;

    public boolean isBuildingExistsInLOS(Location p1, Location q1) {
        DrawShapeFile tempBuilding;
        StartPoint = p1;
        EndPoint = q1;
        for (int it = 0; it < MapLayer.ShapeFileList.size(); it++) {
            tempBuilding = MapLayer.ShapeFileList.get(it);
            List<Location> BuildingPoint = tempBuilding.getNodes();
            for (int it2 = 0; it2 < BuildingPoint.size(); it2++) {
                Location firstNode = BuildingPoint.get(it2);
                Location secondNode;
                if (it2 == BuildingPoint.size() - 1) {     //if it is the last point in the list
                    secondNode = BuildingPoint.get(0);
                } else {
                    secondNode = BuildingPoint.get(it2 + 1);
                }
                if (doIntersect(p1, q1, firstNode, secondNode) == true) {  //building exists
                    MapLayer.ShapeFileList.get(it).setExistInLOS(true);
                    tempBuilding.setPoint1(firstNode);
                    tempBuilding.setPoint2(secondNode);
                }
            }
        }
        return false;   //no building exists in between
    }

    // The function that returns true if line segment 'p1q1' and 'p2q2' intersect.
    boolean doIntersect(Location p1, Location q1, Location p2, Location q2) {
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases
        // p1, q1 and p2 are colinear and p2 lies on segment p1q1
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }

        // p1, q1 and p2 are colinear and q2 lies on segment p1q1
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }

        // p2, q2 and p1 are colinear and p1 lies on segment p2q2
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }

        // p2, q2 and q1 are colinear and q1 lies on segment p2q2
        if (o4 == 0 && onSegment(p2, q1, q2)) {
            return true;
        }

        return false; // Doesn't fall in any of the above cases
    }

    // Given three colinear points p, q, r, the function checks if
    // point q lies on line segment 'pr'
    boolean onSegment(Location p, Location q, Location r) {
        if (q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
                && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY())) {
            return true;
        }

        return false;
    }

    // To find orientation of ordered triplet (p, q, r).
    // The function returns following values
    // 0 --> p, q and r are colinear
    // 1 --> Clockwise
    // 2 --> Counterclockwise
    int orientation(Location p, Location q, Location r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) {
            return 0; // colinear
        }
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    public void DrawReflectedLine(double X1, double Y1, double X2, double Y2) {
        double angle1 = Math.atan2(Y1 - (StartPoint.getX() * one_px_equivalent_naut), X1 - (StartPoint.getY() * one_px_equivalent_naut));
        double angle2 = Math.atan2(Y2 - (EndPoint.getX() * one_px_equivalent_naut), X2 - (EndPoint.getY() * one_px_equivalent_naut));
        double Angle = 0;
        Angle = angle1 - angle2;
        System.out.println("Angle is: " + Angle);
    }

    public List<Location> getNodes() {
        return this.Nodes;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        super.mouseMoved(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        super.mouseWheelMoved(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e); //To change body of generated methods, choose Tools | Templates.
    }

}
