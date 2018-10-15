package utility;

import main.OpenGIS_MAIN;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import main.Location;

/**
 *
 * @author deb kanti
 */
public class GeneralUtil {

    private static final float angleOneQuadrant = 90f;
    private static final float angleThreeQuadrant = 270f;
    private static final float angleFourQuadrant = 360f;
//    private static final int range = 5; //range of clicked position
    public static double range = .57; //range of clicked position

    private static final int iffdistance = -1;
    private static final int iffheight = 6;
    public static int no_of_polygon = 0;
    public static int indexOfFirstPolygon = 0;

    Point center;
    int width;
    int height;

    public GeneralUtil(Point center, int width, int height) {
        this.center = center;
        this.width = width;
        this.height = height;

        //JOptionPane.showMessageDialog(null, "center:"+center+" width:"+width+" height"+height);
    }

    public static double square(double x) {
        return x * x;
    }

    public static double degreeToRadian(double angleInDegree) {
        float angleInRadian = (float) (Math.PI * angleInDegree / 180);
        return angleInRadian;
    }

    public static double radianToDegree(double angleInRadian) {
        float angleInDegree = (float) (180 * angleInRadian / Math.PI);
        return angleInDegree;
    }

    public static ObjectInputStream getObjectInputStreamFromFileStream(String fileName) {
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            return objectInputStream;
        } catch (Exception ex) {
            return null;
        }
    }

    public static ObjectOutputStream getObjectOutputStreamFromFileStream(String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            return objectOutputStream;
        } catch (Exception ex) {
            return null;
        }
    }

    public static boolean isLyingInCone(Location target, Location apex, Location base, double aperture) {
        aperture = GeneralUtil.degreeToRadian((float) aperture);
        double halfAperture = aperture / 2.f;

        // Vector pointing to target from apex
        Location apexToTargetVect = dif(apex, target);

        // Vector pointing from apex to circle-center point.
        Location axisVect = dif(apex, base);

        // target is lying in cone only if it's lying in infinite version of its cone
        // that is, not limited by "round basement".
        // We'll use dotProd() to determine angle between apexToTargetVect and axis.
        boolean isInInfiniteCone = dotProd(apexToTargetVect, axisVect)
                / magn(apexToTargetVect) / magn(axisVect)
                > //We can safely compare cos() of angle between vectors instead of bare angles.
                Math.cos(halfAperture);

        if (!isInInfiniteCone) {
            return false;
        }

        return true;

    }	// End of isLyingInCone function

    public static double dotProd(Location a, Location b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }	// End of dotProd function

    public static Location dif(Location a, Location b) {
        Location retValue = new Location((a.getX() - b.getX()), (a.getY() - b.getY()), 0);

        return retValue;
    }	// End of dif fucntion

    public static double magn(Location a) {
        return (Math.sqrt(a.getX() * a.getX() + a.getY() * a.getY()));
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2)));
    }

    public static double distance2D(double x1, double y1, double x2, double y2) {
        return (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }

    public static double nmToFeet(double distanceInNm) {
        return distanceInNm * 6080;
    }

    public static float getVectorX(float scalar, float heading) {
        return scalar * (float) Math.sin(degreeToRadian(heading));
    }

    public static float getVectorY(float scalar, float heading) {
        return scalar * (float) Math.cos(degreeToRadian(heading));
    }

    public static double getHeadingWithOther(double x1, double y1, double x2, double y2) {
        double intervalx = x2 - x1;
        double intervaly = y2 - y1;

        double angleInRadian = (double) Math.atan(intervaly / intervalx);

        angleInRadian = Math.abs(angleInRadian);
        double angleInDegree = Math.abs((double) (180 * angleInRadian / Math.PI));

        if (intervalx >= 0 && intervaly >= 0) // first quadrant
        {
            return angleOneQuadrant - angleInDegree;
        } else if (intervalx >= 0 && intervaly <= 0) //second quadrant
        {
            return angleOneQuadrant + angleInDegree;
        } else if (intervalx <= 0 && intervaly <= 0) // third quadrant
        {
            return angleThreeQuadrant - angleInDegree;
        } else // fourth quadrant
        {
            return angleThreeQuadrant + angleInDegree;
        }

    }

    public static boolean isPointCloseToLine(double pointX, double pointY, double line1X, double line1Y, double line2X, double line2Y) {
        double dist1 = distance2D(pointX, pointY, line1X, line1Y);
        double dist2 = distance2D(pointX, pointY, line2X, line2Y);

        double distanceWithPoint = dist1 + dist2;
        double distanceWithoutPoint = distance2D(line1X, line1Y, line2X, line2Y);

        return close(distanceWithPoint, distanceWithoutPoint);

    }

    private static boolean close(double p, double p1) {
        double ratio = Math.abs((p - p1) / p1);

        if (ratio < 0.02) {
            return true;
        } else {
            return false;
        }
    }

    public static List<Point2D> loadBoarderMapCoordinates() {
        try {
            List<Point2D> coordinatesXY = new ArrayList<Point2D>();
            FileInputStream fstream = new FileInputStream("src\\resources\\Map\\updateBangladesh.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Double dX = Double.parseDouble(strLine.split(",")[0]);
                Double dY = Double.parseDouble(strLine.split(",")[1]);
                //double g = dX-()game

                Point2D p = new Point2D.Double(dX, dY);

                //coordinatesXY.put(dX, dY);
                coordinatesXY.add(p);

            }
            //Close the input stream
            in.close();
            return coordinatesXY;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String a[]) {

    }

    
    public static int getRandomIntegerInRange(int start, int end, Random random) {
        if (start > end) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) end - (long) start + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * random.nextDouble());
        int randomNumber = (int) (fraction + start);
        return randomNumber;
    }

    public static void log(String aMessage) {

    }

    public static double getAngleFromHeading(double heading) {

        return (450 - heading) % 360;
    }

    public static double getIFFx1(double angle, double x, double distance) {
        double xChange1 = distance * Math.cos(degreeToRadian((float) angle));
        double xChange2 = iffheight * Math.sin(degreeToRadian((float) angle));

        double newx = x + xChange1 + xChange2;

        return newx;

    }

    public static double getIFFy1(double angle, double y, double distance) {
        double yChange1 = distance * Math.sin(degreeToRadian((float) angle));
        double yChange2 = iffheight * Math.cos(degreeToRadian((float) angle));

        double newy = y + (yChange1 - yChange2);

        return newy;

    }

    public static double getIFFx2(double angle, double x, double distance) {
        double xChange1 = distance * Math.cos(degreeToRadian((float) angle));
        double xChange2 = iffheight * Math.sin(degreeToRadian((float) angle));

        double newx = x + (xChange1 - xChange2);

        return newx;

    }

    public static double getIFFy2(double angle, double y, double distance) {
        double yChange1 = distance * Math.sin(degreeToRadian((float) angle));
        double yChange2 = iffheight * Math.cos(degreeToRadian((float) angle));

        double newy = y + yChange1 + yChange2;

        return newy;

    }

    public static Point getPointFromLocation(Location location) {
        Point point = new Point((int) location.getX(), (int) location.getY());
        return point;
    }


    public static int leftOrRight(double startAngle, double finishAngle) {
        double angleDifferenceClockWise = (finishAngle - startAngle + 360) % 360;

        if (angleDifferenceClockWise > 180) {
            return -1;
        } else {
            return 1;
        }
    }

    public static String makeHeight3Figure(String height) {
        int len = height.length();

        for (int i = 0; i < 3 - len; i++) {
            height = "0" + height;
        }
        return height;
    }

    public static String getDoubleTo2DecimalPoint(double p) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(6);// set as you need  
        String myString = nf.format(p);
        return myString;
    }

    public static double getDoubleTo2DecimalPoint2(double p) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(6);// set as you need  
        double temp = Double.parseDouble(nf.format(p));

        return temp;
    }
    
    

    public static double getDoubleTo2DecimalPoint4(double p) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(9);// set as you need  
        double temp = Double.parseDouble(nf.format(p));

        return temp;
    }

    public static double getDoubleTo2DecimalPoint6(double p) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(6);// set as you need  
        double temp = Double.parseDouble(nf.format(p));

        return temp;
    }

    

    private static boolean withinBounds(double x, double y, double x1, double y1, double x2, double y2) {
        if ((((x1 < x) && (x < x2)) || ((x1 > x) && (x > x2))) && (((y1 < y) && (y < y2)) || ((y1 > y) && (y > y2)))) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isPointCloseToPolygon(List<Point2D> pointList, double x, double y) {
        //throw new UnsupportedOperationException("Not yet implemented");
        int size = pointList.size();
        double fx, fy, sx, sy;
        boolean[] pointCloseToPolygon = new boolean[size];
        for (int i = 0; i < size; i++) {
            fx = pointList.get(i % size).getX();
            fy = pointList.get(i % size).getY();
            sx = pointList.get((i + 1) % size).getX();
            sy = pointList.get((i + 1) % size).getY();
            if (GeneralUtil.isPointCloseToLine(x, y, fx, fy, sx, sy)) {
                pointCloseToPolygon[i] = true;
            }
        }

        int pointCloseToPolygonCounter = 0;
        for (int j = 0; j < size; j++) {
            if (pointCloseToPolygon[j] == true) {
                pointCloseToPolygonCounter++;
            }
        }
        if (pointCloseToPolygonCounter > 0) {
            return true;
        } else {
            return false;
        }
    }

    //end of adding by ratan
    public static String convertSecondToHourMinuteSecond(long time) {
        long hour = time / 3600;
        String hourString = makeTime2digits(hour);

        long minuteSecond = time % 3600;
        long minute = minuteSecond / 60;
        String minuteString = makeTime2digits(minute);

        long second = minuteSecond % 60;
        String secondString = makeTime2digits(second);

        return hourString + ":" + minuteString + ":" + secondString;
    }


    public static String makeTime2digits(long time) {
        String strTime = String.valueOf(time);

        int len = strTime.length();

        for (int i = 0; i < 2 - len; i++) {
            strTime = "0" + strTime;
        }
        return strTime;
    }

    public static double getDecimalFromMinute(double value) {
        double fraction = value - (int) value;
        return (int) value + fraction / 0.6;
    }

    public static double getMinuteFromDecimal(double value) {
        double fraction = value - (int) value;
        return (int) value + fraction * 0.6;
    }

    //this function returns whether any text field is string or not
    public static boolean isStringChecker(String textFieldValue) {
        Pattern p = Pattern.compile("[^0-9]");   //pattern for String
        // Pattern p=Pattern.compile("[^[0-9]*\\.?[0-9]]");
        Matcher m = p.matcher(textFieldValue);
        boolean isStringMatched = m.find();
        if (isStringMatched) {
            return true;
        } else {
            return false;
        }
    }

    //rupak 25 nov
    //function for checking wheather any text field contains integer or float in it 
    public static boolean isFloatORIntegerChecker(String textFieldValue) {
        Pattern p = Pattern.compile("[0-9]*\\.?[0-9]");
        Matcher m = p.matcher(textFieldValue);
        boolean isStringMatched = m.find();
        if (isStringMatched) {
            return true;
        } else {
            return false;
        }
    }
    //onik 15 dec
    //function for checking wheather any text field contains integer in it

    public static boolean isIntegerChecker(String textFieldValue) {
        Pattern p = Pattern.compile("^[1-9]\\d*$");
        Matcher m = p.matcher(textFieldValue);
        boolean isStringMatched = m.find();
        if (isStringMatched) {
            return true;
        } else {
            return false;

        }
    }

    //rupak 25 nov
    //fuction for checking wheather any text field is double or not 
    //this is used for checking the fields like radarName,kpiName etc
    public static boolean isDoubleChecker(String textFieldValue) {
        double x = 0;
        try {
            x = Double.parseDouble(textFieldValue);
            //JOptionPane.showMessageDialog(this, "This is a Double");
            return true;
        } catch (NumberFormatException e) {
            //JOptionPane.showMessageDialog(this, "This is not a Double");
            return false;
        }
    }

    //function for checking an empty text field
    public static boolean isTextFieldEmpty(String textEmpty) {
        if (textEmpty.trim().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean fileAlreadyExists(String filePathString) {
        File f = new File(filePathString);
        if (f.exists() && !f.isDirectory()) {
            return true;
        }
        return false;
    }
}
