/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import main.OpenGIS_MAIN;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PageAttributes;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author Asus
 */
public class MeasureHelper implements Serializable {

    double firstPointX, firstPointY, secondPointX, secondPointY, temporaryPointX, temporaryPointY;
    public static double angle, distance;
    PointSet lastPointSet;
    double finalPointX, finalPointY;

    enum PointSet {
        FIRST_POINT,
        SECOND_POINT,
        NONE
    };

    public MeasureHelper() {
        lastPointSet = PointSet.NONE;
    }

    public void clickedPosition(Point2D point, Point center, int width, int height) {
        int fX = (int) point.getX();
        int fY = (int) point.getY();
        fX -= center.x - width / 2;
        fY -= center.y - height / 2;
        double x = (fX - OpenGIS_MAIN.translate_x);
        double y = (-fY + OpenGIS_MAIN.translate_y);
        firstPointX = x * OpenGIS_MAIN.one_nm_equivalent_pixel;
        firstPointY = y * OpenGIS_MAIN.one_nm_equivalent_pixel;
        lastPointSet = PointSet.FIRST_POINT;

    }//jend of click position

    public void updateCursorPosition(double x, double y) {
        temporaryPointX = x;
        temporaryPointY = y;
    }//end of update cursor position

    public void draw(Graphics g) {
        Color c = g.getColor();
        //  g.setColor(new Color(73, 26, 16));

        g.setColor(Color.black);

        switch (lastPointSet) {
            case NONE:
                return;

            case FIRST_POINT:
                finalPointX = temporaryPointX;
                finalPointY = temporaryPointY;
                finalPointX = temporaryPointX;
                finalPointY = temporaryPointY;
                break;
        }
        int crossHairSize = 5;

//        g.drawLine((int) (firstPointX * HTB_MAIN.one_px_equivalent_naut - crossHairSize),
//                (int) (firstPointY * HTB_MAIN.one_px_equivalent_naut),
//                (int) (firstPointX * HTB_MAIN.one_px_equivalent_naut + crossHairSize),
//                (int) (firstPointY * HTB_MAIN.one_px_equivalent_naut));
//        g.drawLine((int) (firstPointX * HTB_MAIN.one_px_equivalent_naut),
//                (int) (firstPointY * HTB_MAIN.one_px_equivalent_naut - crossHairSize),
//                (int) (firstPointX * HTB_MAIN.one_px_equivalent_naut),
//                (int) (firstPointY * HTB_MAIN.one_px_equivalent_naut + crossHairSize));
        //drawig cross hair uppore portion

        g.drawLine((int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut - crossHairSize),
                (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut),
                (int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut + crossHairSize),
                (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut));
        g.drawLine((int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut),
                (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut - crossHairSize),
                (int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut),
                (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut + crossHairSize));
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);

        g2d.setStroke(new BasicStroke(1));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawLine((int) (firstPointX * OpenGIS_MAIN.one_px_equivalent_naut), (int) (firstPointY * OpenGIS_MAIN.one_px_equivalent_naut), (int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut), (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut));
        double xd, yd;
        xd = finalPointX - firstPointX;
        yd = finalPointY - firstPointY;
        angle = (double) (Math.atan2(yd, xd) * 180 / 3.1416);
        angle = cartesianToTrueNorth(angle);

        distance = distance(finalPointX, finalPointY, firstPointX, firstPointY);
        String dis = String.valueOf(getDistance() * 1.852);
        String dis1 = dis.length() <= 5 ? dis : dis.substring(0, 5);

        OpenGIS_MAIN.customDrawString3(g, "" + dis1 + " Km", new Point((int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut), (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut)));
        OpenGIS_MAIN.customDrawString2(g, "" + getAngle(), new Point((int) (finalPointX * OpenGIS_MAIN.one_px_equivalent_naut), (int) (finalPointY * OpenGIS_MAIN.one_px_equivalent_naut)));
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return (double) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }//end of distance

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

    public float getDistance() {

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(2);
        DecimalFormat oneDForm = new DecimalFormat("###.##");
        return Float.valueOf(oneDForm.format(distance));
    }

    public void getfirstpointX() {

    }

}//end of class

