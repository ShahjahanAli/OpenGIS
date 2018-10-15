//main htb class
package main;

import map.DrawShapeFile;
import map.DrawElevationProfile;
import map.CoordinateList;
import drawingtool.*;
import drawingtool.drawline;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.tilesources.*;
import org.openstreetmap.gui.jmapviewer.OsmMercator;

import java.text.DecimalFormat;

import java.awt.BasicStroke;
import java.awt.RenderingHints;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JDesktopPane;
import map.DrawConnectionTest;
import map.ElevationReader;
import map.MapLayerClass;
import map.Select_Object;
import map.ShapeFileRead;
import map.ShapefileList_Form;
import org.nocrala.tools.gis.data.esri.shapefile.exception.DataStreamEOFException;
import org.nocrala.tools.gis.data.esri.shapefile.exception.InvalidShapeFileException;
import utility.GeneralUtil;
import utility.GridReferenceConversion;

public class OpenGIS_MAIN extends JMapViewer implements ActionListener, Serializable {

    double longitude = 23.29;
    double latitude = 91.08;
    private double lon;
    private double lat;
    double xI, yI;
    static int mouseX, mouseY;

    public static String RadioOperatorCallsign;
    public boolean Sweepjammedflag = false;

    OpenGIS_MAIN htbParent;

    private static int showZoomScale = 0;
    public static double zoomShow = 3;
    public static int displayAreaWidth = 26761; // Km
    public static int displayAreaHeight = 7170;

    public static int areaLevel = 3;
    public static int areaLevelU = 14;
    public static int areaLevelD = 3;
    public static int slarx = 0;
    public static int slary = 0;
    public static int airchk = 0;
    public static double selectedRadarClicked = 1;
    public static double selectedRadarX;
    public static double selectedRadarY;
    public static int cloudChoice = 0;
    public static int playButtonClick = 0;
    private double rangeOnLavel = 10;
    public static int rangeBearingShow = 1;
    public ArrayList<Double> LopLeadList;
    final int range = 5;
    List<Point2D> point2D;
    Graphics2D g2d;
    static double centerX, centerY;
    public boolean lastpressedbutton;

    public static HashMap<Integer, Integer> zoomMap;
    float lookAtX_NM, lookAtY_NM = 0;
    int RECT_SIZE = 10;
    int AREA_SIZE = 15;
    public String traineeName;
    public boolean flagDrawRadar = false;
    public boolean flagDrawCurrentRadar = false;
    public PressedButtonEnum lastPressedButton = PressedButtonEnum.NONE;
    public static MeasureHelper measureHelper;

    static JLabel showtext;
    static JLabel showtextmouse;
    static JLabel showtextvoice;
    static JLabel showEquipmentInfoInLabel;
    public static double translate_x = 0, translate_y = 0;
    static int screen_height, screen_width;
    int flag = 0;
    int measureButtonClick = 0;
    int measureLineRemove = 1;
    public static int drawSelectedObject = 0;
    Point2D point;
    int clickCount = 0;
    double distance, angle;
    final AlsXYMouseLabelComponent alsXYMouseLabel = new AlsXYMouseLabelComponent();
    final int NUM_PTS = 3;
    int xpos[] = new int[NUM_PTS];
    int ypos[] = new int[NUM_PTS];
    int arrind = 0;
    boolean isHideTerrinMap = false;

    private boolean flagDrawRoute = false;
    private boolean flagDrawEdge = false;
    JInternalFrame myframe = null;
    public static int identity;
    static int current_zoom_level = 6;
    public static float one_nm_equivalent_pixel;
    public static float one_px_equivalent_naut;
    JLabel mousePosition;
    public static JLabel label;

    private int min_zoom = 1;
    private int max_zoom = 34;
    static AffineTransform affineTransform;

    public static GameScene gameScene;
    public boolean gameStarted = false;

    public static int currentZoom;
    public static int PreviousZoom;

    public boolean iffSignalCheck = false;
    public boolean iffSignalSelection = false;
    List<Point2D> coordinateMap;
    public static boolean gameLoadedFromFile = false;
    public static double distanceOfTwoClicks = 60;
    public static boolean gameReplaying = false;

    public static double lonm = 0;
    public static double latm = 0;

    public static String MGRS;
    public static String MGR_Easting;
    public static String MGR_Northing;

    public static int Elevation;

    private boolean flagDrawConnection = false;
    private boolean DrawNISFilterConnection = false;
    private boolean drawGR = true;

    private boolean HideRadioDevice = false;

    private String inputPreviousCommand = "";
    public boolean flagSelectedObjectShow = false;
    public int clickedXforZoom;
    public int clickedYforZoom;
    int rotationCounter = 0;
    public static int flg_for_aircraftDrag = 0;
    public static int flg_for_hfDrag = 0;
    public static boolean initializeSaveFile = false;
    public static FileOutputStream fileStream;
    public static ObjectOutputStream os;

    public static boolean currentSGC1 = true;
    public static int saveGameStartTime = -1;
    public static boolean radarDegreeView = false;

    public static boolean LayerView = false;
    public static boolean ScanLineDraw = true;
    public static boolean DrawConnectionForMS;
    public static boolean DrawConnectionForDF = false;
    private static boolean AddHfDevice = false;
    public static boolean BusStart = false;
    public static boolean drawConnectionPS = false;
    public static boolean tempFlag = false;
    public static boolean dfTgtLocationShow = false;

    public static int stationNo = 0;
    public static double[] tempArray = new double[2];
    public static Graphics g;
    public static int MAPID;
    private String teamName;
    float[] dash1 = {2f, 0f, 2f};

    private boolean timerStartHF = false;
    private boolean timerStartJammer = false;
    private boolean timerStartVHF = false;
    private boolean timerStartUHF = false;
    private boolean timerStartRR = false;
    private int MapWidth;
    private int MapHeight;
    public static javax.swing.Timer timerForSweep;

    public boolean DFScanAnimation = true;
    public boolean JammerScanAnimation = true;

    public static int SimulationHour = 1;
    public static int SimulationMonth = 1;

    public static String SimulationEnvironment_DayTime = "Day";
    public static String SimulationEnvironment_Weather = "Not Selected";
    public static String SimulationEnvironment_Fog = "Not Selected";
    public static int SimulationEnvironment_Humidity = 0;
    public static int SimulationEnvironment_Temperature = -273;
    public static Color DrawingToolColor;
    public static float DrawingToolThickness = (float) 0.5;
    public static boolean Enable_Drag_Map = false;

    public static int JammingSequenceNumber = 0;
    public boolean JammingStart = false;

    java.util.Map<String, String> envAttenuationData = new HashMap<String, String>();
    public boolean ShowMilitaryFlag = true;
    public boolean ShowMilitaryMarks = true;
    public boolean ShowOnlyFriendlyFlag = false;

    public static String ArmyFlag_Name = null;
    public static String ArmyFlag_Type = null;
    public static String ArmyFlag_Command = null;

    public static JDesktopPane desk = null;

    public static List<String> ShapefileList;
    public static MapLayerClass MapLayer;
    public static boolean ShapefileLoadComplete = false;

    public OpenGIS_MAIN(JInternalFrame htb, int identity) throws FileNotFoundException, IOException, ClassNotFoundException {
        setMapID(identity);
        this.MapWidth = (int) htb.getSize().getWidth();
        this.MapHeight = (int) htb.getSize().getHeight();

        this.myframe = htb;
        this.identity = identity;
        setZoomMap();
        showtext = new JLabel();
        showtext.setSize(300, 50);
        showtextmouse = new JLabel();
        showtextmouse.setSize(300, 50);
        showtextvoice = new JLabel();
        showtextvoice.setSize(300, 50);
        showEquipmentInfoInLabel = new JLabel();
        showEquipmentInfoInLabel.setSize(100, 300);
        GeneralUtil gen = new GeneralUtil(center, this.getWidth(), this.getHeight());
        mousePosition = new JLabel();
        mousePosition = new JLabel();
        mousePosition.setSize(300, 50);
        this.setZoomContolsVisible(false);

        this.setTileSource(new OfflineOsmTileSource("file:///" + MapLocation(), 3, 16));

        gameScene = new GameScene();
        gameScene.HTBParent = this;

        measureHelper = new MeasureHelper();

        CustomMouseListener mouseListener = new CustomMouseListener();
        CustomWheelListener wheelListener = new CustomWheelListener();
        this.addMouseListener(mouseListener);
        this.addMouseMotionListener(mouseListener);
        this.addMouseWheelListener(wheelListener);
        this.setFocusable(true);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int xSize = ((int) tk.getScreenSize().getWidth());
        int ySize = ((int) tk.getScreenSize().getHeight());
        screen_width = xSize;
        screen_height = ySize;
        myframe.add(this);
        myframe.add(showtext);
        myframe.add(showtextmouse);
        myframe.add(showtextvoice);
        myframe.add(showEquipmentInfoInLabel);
        myframe.add(mousePosition);
        this.add(mousePosition);
        float jpanelWidth = this.getWidth();
        int jpanelHeight = this.getHeight();

        // for showing mouse info position on cursor position   
        JLayeredPane layeredPane = myframe.getRootPane().getLayeredPane();
        layeredPane.add(alsXYMouseLabel, JLayeredPane.DRAG_LAYER);
        alsXYMouseLabel.setBounds(0, 0, myframe.getWidth(), myframe.getHeight());
        alsXYMouseLabel.setFont(new java.awt.Font("Tahoma", 1, 11));

        //------------------------- Map Layer ---------------------------------//
        MapLayer = new MapLayerClass();
        MapLayer.MAIN = this;
        ShapefileList = new ArrayList<>();
        //------------------------- Map Layer ---------------------------------//

    }

    @Override
    public void update(Graphics g) {
        System.out.println("Updating");
    }

    public int getCurrentZoom() {
        return currentZoom;
    }

    public void setCurrentZoom(int currentZoom) {
        OpenGIS_MAIN.currentZoom = currentZoom;
        zoom = currentZoom;
    }

    public int getPreviousZoom() {
        return PreviousZoom;
    }

    public void setPreviousZoom(int PreviousZoom) {
        OpenGIS_MAIN.PreviousZoom = PreviousZoom;
        zoom = PreviousZoom;
    }

    public void setZoomMap() {
        zoomMap = new HashMap<Integer, Integer>();
        int zoomLevel = 34; //max zoom level
        int visibleNauticalMileWidth = 16; //maximum zoom is 16 nm
        float zoomIncrease = (float) .25;
        for (; zoomLevel > 0; zoomLevel--) {
            zoomMap.put(zoomLevel, visibleNauticalMileWidth);
            visibleNauticalMileWidth = (int) (visibleNauticalMileWidth + visibleNauticalMileWidth * zoomIncrease);
        }
    }

    public static int getMapID() {
        return MAPID;
    }

    public static void setMapID(int MapID) {
        MAPID = MapID;
    }

    public static int getShowZoomScale() {
        return showZoomScale;
    }

    public static void setShowZoomScale(int aShowZoomScale) {
        showZoomScale = aShowZoomScale;
    }

    public String MapLocation() throws FileNotFoundException {
        String MapAddress = null;
        FileReader fileReader = new FileReader("C:/EWTSS/EWSSDirectory/MapAddress.txt");
        Scanner scanner = new Scanner(fileReader);
        while (scanner.hasNext()) {
            MapAddress = scanner.next();
        }
        System.out.println("Map Address: " + MapAddress);
        return MapAddress;
    }

    public void init_canvas(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (!this.isHideTerrinMap) {
            super.paint(g);
        }
        g.translate(translate_x, translate_y);
        g.scale(1, -1);
        g.setColor(Color.RED);
    }//end of init canvas

    public void start_program() throws FileNotFoundException, IOException, ClassNotFoundException {
        translate_x = (float) (this.getWidth() / 2.0);
        translate_y = -(float) (this.getHeight() / 2.0 - .5);
        fix_map_movement();
    }

    public static void paintIt() {
        gameScene.HTBParent.repaint();
    }
    public static int i = 0;

    // Positioning MGR for different Map viewer 
    public void drawMGR(Graphics g) {
        int mouseX = 0;
        int mouseY = 0;
        int zoomScaleX = 0;
        int zoomScaleY = 0;

        switch (getMapID()) {

            // Following code is for Mission Planner Map Window
            case 1:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Following code is for Radio Operator Map Window
            case 2:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            case 3:
                mouseX = 1050;
                mouseY = 480;
                zoomScaleX = 950;
                zoomScaleY = 490;
                break;

            case 4:
                mouseX = 750;
                mouseY = 550;
                break;

            // Following code is for Scenario Creation Map Window
            case 9:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Following code is for Instructor Map Window
            case 10:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Following code is for DF Map Window
            case 11:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Following code is for MS Map Window
            case 12:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Individual Training: Jammer Map Window
            case 13:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Individual Training: Analyzer Map Window
            case 14:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

//----------------------------------------------------------------------------
            // Group Training: Instructor Map Window
            case 20:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Group Training: DF Map Window
            case 21:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Group Training: MS Map Window
            case 22:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Group Training: Jammer Map Window
            case 23:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Group Training: Analyzer Map Window
            case 24:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

            // Group Training: EW Mission Planner Map Window
            case 25:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

//--------------------------------------------------------------------------
            // Test Window: Map Viewer
            case 30:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

//--------------------------------------------------------------------------
            // Test Window_Client: Map Viewer
            case 40:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                ;
                break;

            default:
                break;

//--------------------------------------------------------------------------
            // Message Sequence: Map Viewer
            case 50:
                mouseX = this.MapWidth - 160;
                mouseY = this.MapHeight - 90;
                zoomScaleX = this.MapWidth - 220;
                zoomScaleY = this.MapHeight - 80;
                break;

        }

        OpenGIS_MAIN.showLabelMouseInfo("MGR : " + OpenGIS_MAIN.MGRS, new Point((int) (mouseX), (int) (mouseY)));
        OpenGIS_MAIN.showLabel("Zoom Scale: " + OpenGIS_MAIN.zoomShow + ", Zoom Level: " + OpenGIS_MAIN.displayAreaWidth + " Km", new Point((int) (zoomScaleX), (int) (zoomScaleY)));
        //      HTB_MAIN.showLabelMouseInfo("Lat: " + HTB_MAIN.latm + ", Lon:" + HTB_MAIN.lonm, new Point((int) (mouseX), (int) (mouseY)));
    }

    @Override
    public void paint(final Graphics g) {
        this.g = g;
        g2d = (Graphics2D) g;
        init_canvas(g2d);
        Stroke s = g2d.getStroke();
        g2d.setStroke(s);

        drawline line = new drawline();
        line.DrawTemporaryline(g2d, g);
        line.DrawAllline(g2d, g);

        drawcircle circle = new drawcircle();
        circle.DrawTemporarycircle(g2d, g);
        circle.DrawAllcircle(g2d, g);

        drawrectangle rectangle = new drawrectangle();
        rectangle.DrawTemporaryrectangle(g2d, g);
        rectangle.DrawAllrectangle(g2d, g);

        DrawElevationProfile DEP = new DrawElevationProfile();
        DEP.DrawTemporarypolygon(g2d, g);
        DEP.DrawAllpolygon(g2d, g);

        DrawConnectionTest DCT = new DrawConnectionTest();
        DCT.DrawTemporaryline(g2d, g);
        DCT.DrawAllline(g2d, g);

        if (isShapefileLoadComplete()) {
            DrawShapeFile DSP = new DrawShapeFile();
            DSP.DrawTemporaryShape(g2d, g);
            DSP.DrawAllShape(g2d, g);
        }

        if (measureLineRemove == 1) {
            measureHelper.draw(g);
        }

        drawMGR(g);
        repaint();
    }

    public static void push_matrix(Graphics2D g) {
        affineTransform = g.getTransform();
    }

    public static void pop_matrix(Graphics2D g) {
        g.setTransform(affineTransform);
    }

    public void setDrawingToolColor(Color ToolColor) {
        this.DrawingToolColor = ToolColor;
    }

    public Color getDrawingToolColor() {
        return this.DrawingToolColor;
    }

    public void setDrawingToolThikcness(float Thikcness) {
        this.DrawingToolThickness = Thikcness;
    }

    public float getDrawingToolThikcness() {
        return this.DrawingToolThickness;
    }

    public static boolean isShapefileLoadComplete() {
        return ShapefileLoadComplete;
    }

    public static void setShapefileLoadComplete(boolean ShapefileLoadComplete) {
        OpenGIS_MAIN.ShapefileLoadComplete = ShapefileLoadComplete;
    }

    public void LoadShapeFile(List<String> FileAddress, List<String> ShapefileName, int ShapeNumber, String Type) throws FileNotFoundException, DataStreamEOFException {
//        gameScene.ShapeFileList.clear();
        for (int i = 0; i < ShapefileName.size(); i++) {
            ShapefileList.add(ShapefileName.get(i));
        }

        setPreviousZoom(currentZoom);
        setCurrentZoom(16);
        OpenGIS_MAIN.this.fix_map_movement();
//        ShapeFileRead SFP = new ShapeFileRead(gameScene, center, FileAddress, ShapefileName, ShapeNumber, Type);
        ShapeFileRead SFP = new ShapeFileRead(MapLayer, center, FileAddress, ShapefileName, ShapeNumber, Type);
        try {
            SFP.ReadShapeFile();
        } catch (InvalidShapeFileException ex) {
            Logger.getLogger(OpenGIS_MAIN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OpenGIS_MAIN.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCurrentZoom(getPreviousZoom());
        OpenGIS_MAIN.this.fix_map_movement();
//        ShapefileList_Form SFLF = new ShapefileList_Form();
        MapInterface MI = new MapInterface(1);
        MI.LoadShapefileList();
    }

    double Start_Lat;
    double Start_Lon;
    double End_Lat;
    double End_Lon;
    int totalTempPoints = 0;

    public void GenerateElevationData(GameScene gameScene, Point2D point, Point center) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        double Latitude = Double.parseDouble(df.format(latValue));
        double Longitude = Double.parseDouble(df.format(lonValue));

        if (totalTempPoints == 1) {
            Start_Lat = Latitude;
            Start_Lon = Longitude;
        } else if (totalTempPoints == 2) {
            End_Lat = Latitude;
            End_Lon = Longitude;

            if (isDrawElevationProfile()) {
                setDrawElevationProfile(false);
                this.lastPressedButton = PressedButtonEnum.NONE;
                CoordinateList CL = new CoordinateList(Start_Lat, Start_Lon, End_Lat, End_Lon);
            }
            totalTempPoints = 0;
            Start_Lat = 0;
            Start_Lon = 0;
            End_Lat = 0;
            End_Lon = 0;
        }
    }

    public double Distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        int r = 6371; // average radius of the earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = r * c;
        return d * 1000;
    }

    public void GetElevationFromLatLon(Point2D point) {

        double lonValue = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
        double latValue = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

        java.text.DecimalFormat df = new java.text.DecimalFormat();
        df.setMaximumFractionDigits(7);

        double Latitude = Double.parseDouble(df.format(latValue));
        double Longitude = Double.parseDouble(df.format(lonValue));
        System.out.println("Lat Lon: " + Latitude + " " + Longitude);
        ElevationReader HR = new ElevationReader();
        HR.getElevationFromHgt(Latitude, Longitude);
    }

    boolean DrawElevationProfile = false;

    public boolean isDrawElevationProfile() {
        return DrawElevationProfile;
    }

    public void setDrawElevationProfile(boolean DrawElevationProfile) {
        this.DrawElevationProfile = DrawElevationProfile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    class CustomMouseListener extends MouseMotionAdapter implements MouseListener {

        float first_clicked_x, first_clicked_y;
        boolean dragging = false;
        int indexOfReplaceableFigure = 0;
        double fx, fy, sx, sy, ex, ey;

        @Override
        public void mouseClicked(final MouseEvent e) {
            double x1 = ((e.getX() - translate_x));
            double y1 = (-(e.getY()) + translate_y);
            double x = center.x + e.getX() - getWidth() / 2;
            double y = center.y + e.getY() - getHeight() / 2;
            point = new Point2D.Double(x, y);
            clickedXforZoom = e.getX();
            clickedYforZoom = e.getY();
            currentZoom = zoom;
            totalTempPoints++;

            GenerateElevationData(gameScene, point, center);
            ElevationReader ER = new ElevationReader(point);

            switch (lastPressedButton) {

                case Draw_ElevationProfile:
                    setDrawElevationProfile(true);
                    break;

                case DisplayShapeFile:
                    setPreviousZoom(currentZoom);
                    setCurrentZoom(16);
                    OpenGIS_MAIN.this.fix_map_movement();
                    ShapeFileRead SFP = new ShapeFileRead(gameScene, center);
                    try {
                        SFP.ReadShapeFile();
                    } catch (InvalidShapeFileException ex) {
                        Logger.getLogger(OpenGIS_MAIN.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(OpenGIS_MAIN.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (DataStreamEOFException ex) {
                Logger.getLogger(OpenGIS_MAIN.class.getName()).log(Level.SEVERE, null, ex);
            }
                    setCurrentZoom(getPreviousZoom());
                    OpenGIS_MAIN.this.fix_map_movement();
                    break;

                case SELECT_OBJECT:
                    Select_Object SO = new Select_Object(MapLayer, point, center);
                    SO.SelectShapefile();
                    break;
                case DRAW_CONNECTION_TEST:
                    DrawConnectionTest DCT = new DrawConnectionTest();
                    DCT.adddrawline(gameScene, point, center);
                    break;

                case EDIT_SHAPFILE:
                    Select_Object SO_Edit = new Select_Object(MapLayer, point, center);
                    SO_Edit.EditShapefile();
                    break;

                case MEASURE_TOOL:
                    measureLineRemove = 1;
                    measureHelper.clickedPosition(point, center, getWidth(), getHeight());
                    break;

                case NONE:
                    break;

            }

            int modifiers = e.getModifiers();
            if ((modifiers & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
                g.setColor(Color.red);
                measureLineRemove = 0;
                measureHelper.clickedPosition(point, center, getWidth(), getHeight());
            }

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            translate_x += (e.getX() - first_clicked_x);
            translate_y -= (e.getY() - first_clicked_y);
            first_clicked_x = e.getX();
            first_clicked_y = e.getY();
            OpenGIS_MAIN.this.fix_map_movement();
            int modifiers = e.getModifiers();
            if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK && identity != 1) {
                double x = ((e.getX() - translate_x));
                double y = (-(e.getY()) + translate_y);
                Point2D point1;
                point1 = new Point2D.Double(x, y);
                dragging = true;
                repaint();
            }
            myframe.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            first_clicked_x = e.getX();
            first_clicked_y = e.getY();
            int modifiers = e.getModifiers();
            if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK && identity != 1) {
                double x = ((e.getX() - translate_x));
                double y = (-(e.getY()) + translate_y);
            }
        }

        public double cartesianToTrueNorth(double angleCartesian) {
            double temp = 360 - (angleCartesian - 90);
            if (temp >= 360) {
                double a = temp - 360;
                return (360 - a);
            }
            return (360 - temp);
        }

        @Override
        public void mouseMoved(MouseEvent me) {

            Point2D point2;
            int x1 = center.x + me.getX() - getWidth() / 2;
            int y1 = center.y + me.getY() - getHeight() / 2;
            point = new Point2D.Double(x1, y1);
            currentZoom = zoom;

            lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
            latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

            currentZoom = zoom;

            if (selectedRadarClicked == 1) {
                double x11 = center.x + me.getX() - getWidth() / 2;
                double y11 = center.y + me.getY() - getHeight() / 2;
                double fx = x11 - (center.x - getWidth() / 2);
                double fy = y11 - (center.x - getHeight() / 2);
                double tranX = (fx - translate_x);
                double tranY = (fy - translate_y);
                double xd = tranX * one_nm_equivalent_pixel;
                double yd = tranY * one_nm_equivalent_pixel;
                double fx2 = selectedRadarX - (center.x - getWidth() / 2);
                double fy2 = selectedRadarY - (center.x - getHeight() / 2);
                double tranX2 = (fx2 - translate_x);
                double tranY2 = (fy2 - translate_y);
                double xd2 = tranX2 * one_nm_equivalent_pixel;
                double yd2 = tranY2 * one_nm_equivalent_pixel;
                distance = Math.sqrt((xd2 - xd) * (xd2 - xd) + (yd2 - yd) * (yd2 - yd));
                double xdangle, ydangle;
                xdangle = xd2 - xd;
                ydangle = yd2 - yd;
                angle = (double) (Math.atan2(ydangle, xdangle) * 180 / 3.1416);
                angle = cartesianToTrueNorth(angle);
                DecimalFormat oneDForm = new DecimalFormat("###.##");
                distance = Double.valueOf(oneDForm.format(distance));
                angle = Double.valueOf(oneDForm.format(angle));
            }

            lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
            latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);

            GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();
//            String UTM = gridReferenceConversion.latLon2UTM(latm, lonm);
            MGRS = gridReferenceConversion.latLon2MGRUTM(latm, lonm);

            ElevationReader ER = new ElevationReader(latm, lonm);
            Elevation = ER.getElevation();

            java.text.DecimalFormat df = new java.text.DecimalFormat();
            df.setMaximumFractionDigits(2);

            lonm = GeneralUtil.getMinuteFromDecimal(lonm);
            latm = GeneralUtil.getMinuteFromDecimal(latm);

            lonm = Double.parseDouble(df.format(lonm));
            latm = Double.parseDouble(df.format(latm));

            // for showing mouse position oncursor position by bks
            alsXYMouseLabel.x = me.getX();
            alsXYMouseLabel.y = me.getY();
            alsXYMouseLabel.repaint();

            int x = (int) ((me.getX() - translate_x));
            int y = (int) (-(me.getY()) + translate_y);

            measureHelper.updateCursorPosition(x * one_nm_equivalent_pixel, y * one_nm_equivalent_pixel);

            repaint();
        }//end of mouse moved

        @Override
        public void mouseReleased(MouseEvent e) {
            first_clicked_x = e.getX();
            first_clicked_y = e.getY();
            int modifiers = e.getModifiers();
            if ((modifiers & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                double x = ((e.getX() - translate_x));
                double y = (-(e.getY()) + translate_y);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    //for showing lat lon on mouse cursor position
    class AlsXYMouseLabelComponent extends JComponent {

        public int x;
        public int y;
        int mouseX;
        int mouseY;

        public AlsXYMouseLabelComponent() {
            this.setBackground(Color.blue);
        }

        // use the xy coordinates to update the mouse cursor text/label
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            mouseX = 750;
            mouseY = 550;

            if (zoom == 16) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();
                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 5 ? MGR_Easting : MGR_Easting.substring(0, 5);
                String MGR_Northing_L7 = MGR_Northing.length() <= 5 ? MGR_Northing : MGR_Northing.substring(0, 5);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 15) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 5 ? MGR_Easting : MGR_Easting.substring(0, 5);
                String MGR_Northing_L7 = MGR_Northing.length() <= 5 ? MGR_Northing : MGR_Northing.substring(0, 5);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 14) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 4 ? MGR_Easting : MGR_Easting.substring(0, 4);
                String MGR_Northing_L7 = MGR_Northing.length() <= 4 ? MGR_Northing : MGR_Northing.substring(0, 4);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 13) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 4 ? MGR_Easting : MGR_Easting.substring(0, 4);
                String MGR_Northing_L7 = MGR_Northing.length() <= 4 ? MGR_Northing : MGR_Northing.substring(0, 4);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 12) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 3 ? MGR_Easting : MGR_Easting.substring(0, 3);
                String MGR_Northing_L7 = MGR_Northing.length() <= 3 ? MGR_Northing : MGR_Northing.substring(0, 3);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }
            if (zoom == 11) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 3 ? MGR_Easting : MGR_Easting.substring(0, 3);
                String MGR_Northing_L7 = MGR_Northing.length() <= 3 ? MGR_Northing : MGR_Northing.substring(0, 3);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 10) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 2 ? MGR_Easting : MGR_Easting.substring(0, 2);
                String MGR_Northing_L7 = MGR_Northing.length() <= 2 ? MGR_Northing : MGR_Northing.substring(0, 2);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 9) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 2 ? MGR_Easting : MGR_Easting.substring(0, 2);
                String MGR_Northing_L7 = MGR_Northing.length() <= 2 ? MGR_Northing : MGR_Northing.substring(0, 2);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 8) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 1 ? MGR_Easting : MGR_Easting.substring(0, 1);
                String MGR_Northing_L7 = MGR_Northing.length() <= 1 ? MGR_Northing : MGR_Northing.substring(0, 1);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }
            if (zoom == 7) {
                lonm = OsmMercator.XToLon((int) (point.getX()), OpenGIS_MAIN.currentZoom);
                latm = OsmMercator.YToLat((int) (point.getY()), OpenGIS_MAIN.currentZoom);
                GridReferenceConversion gridReferenceConversion = new GridReferenceConversion();

                MGR_Easting = gridReferenceConversion.latLon2MGRUTM_Easting(latm, lonm);
                MGR_Northing = gridReferenceConversion.latLon2MGRUTM_Northing(latm, lonm);
                String MGR_Easting_L7 = MGR_Easting.length() <= 1 ? MGR_Easting : MGR_Easting.substring(0, 1);
                String MGR_Northing_L7 = MGR_Northing.length() <= 1 ? MGR_Northing : MGR_Northing.substring(0, 1);
                String GridReference_E = "E: " + MGR_Easting_L7;
                String GridReference_N = "N: " + MGR_Northing_L7;
//                String GR = " " + GridReference_E + " " + GridReference_N;
                String ElevationData = "ELEV: " + Elevation;
                String GR = " " + GridReference_E + " " + GridReference_N + " " + ElevationData;
                g.setColor(Color.BLUE);
                g.drawString(GR, x, y - 2);
            }

            if (zoom == 6) {

            }

            g2d.draw(new Line2D.Double(x, y, x + 0, y - 30));
            g2d.draw(new Line2D.Double(x, y, x + 0, y + 30));
            g2d.draw(new Line2D.Double(x, y, x + 30, y + 0));
            g2d.draw(new Line2D.Double(x, y, x - 30, y + 0));

        }
    }

    public void drawMapClick() {
        flag = 1;
        repaint();
    }

    public void controlMeasureClick() {
        if (measureButtonClick == 0) {
            measureButtonClick = 1;
        } else if (measureButtonClick == 1) {
            measureButtonClick = 0;
        }
    }

    public void zoom_in() {
        if (current_zoom_level == max_zoom) {
            return;
        }
        current_zoom_level++;
        set_nm_equivalent_pixel();
    }

    public void zoom_out() {
        if (current_zoom_level == min_zoom) {
            return;
        }
        current_zoom_level--;
        set_nm_equivalent_pixel();
    }

    public void set_nm_equivalent_pixel() {
        int look_at_point_x = (int) (getWidth() / 2 - translate_x);
        int look_at_point_y = (int) (-(getHeight() / 2) - translate_y);
        float look_at_point_old_nm_system_x = look_at_point_x * one_nm_equivalent_pixel;
        float look_at_point_old_nm_system_y = look_at_point_y * one_nm_equivalent_pixel;

        one_nm_equivalent_pixel = (float) (Math.pow(2, current_zoom_level) / screen_width);
        one_px_equivalent_naut = (float) (screen_width / Math.pow(2, current_zoom_level));

        one_nm_equivalent_pixel = (float) ((float) zoomMap.get(current_zoom_level) / screen_width); //casting to float in fear of integer division
        one_px_equivalent_naut = (float) ((float) screen_width / zoomMap.get(current_zoom_level));

        int old_look_at_point_x_in_new_pixel_system = (int) (look_at_point_old_nm_system_x * one_px_equivalent_naut);
        int old_look_at_point_y_in_new_pixel_system = (int) (look_at_point_old_nm_system_y * one_px_equivalent_naut);

        translate_x += (look_at_point_x - old_look_at_point_x_in_new_pixel_system);
        translate_y += (look_at_point_y - old_look_at_point_y_in_new_pixel_system);
        repaint();
    }

    public static void showLabel(String str, Point point) {
        showtext.setLocation(point.x, point.y);
        showtext.setFont(new java.awt.Font("Tahoma", 1, 10));
        showtext.setForeground(new java.awt.Color(153, 0, 0));
        showtext.setText(str);
        showtext.setVisible(true);
    }

    public static void showLabelVoice(String str, Point point) {
        showtextvoice.setLocation(point.x, point.y);
        showtextvoice.setFont(new java.awt.Font("Tahoma", 1, 15));
        showtextvoice.setForeground(new java.awt.Color(153, 0, 0));
        showtextvoice.setText(str);
        showtextvoice.setVisible(true);
    }

    public static void showLabelMouseInfo(String str, Point point) {
        showtextmouse.setLocation(point.x, point.y);
        showtextmouse.setFont(new java.awt.Font("Tahoma", 1, 10));
        showtextmouse.setForeground(new java.awt.Color(153, 0, 0));
        showtextmouse.setText(str);
        showtextmouse.setVisible(true);
    }

    public static void showpowerstation(String str, Point point) {

        showtextmouse.setLocation(point.x, point.y);
        showtextmouse.setFont(new java.awt.Font("Tahoma", 1, 15));
        showtextmouse.setForeground(new java.awt.Color(153, 0, 0));
        showtextmouse.setText(str);
        showtextmouse.setVisible(true);

    }

    public static void showLabelMGRS(String str, Point point) {
        showtextmouse.setLocation(point.x, point.y);
        showtextmouse.setFont(new java.awt.Font("Tahoma", 1, 15));
        showtextmouse.setForeground(new java.awt.Color(153, 0, 0));
        showtextmouse.setText(str);
        showtextmouse.setVisible(true);
    }

    public static void showLabelGridLineTest(String str, Point point) {
        showtextmouse.setLocation(point.x, point.y);
        showtextmouse.setFont(new java.awt.Font("Tahoma", 1, 15));
        showtextmouse.setForeground(new java.awt.Color(153, 0, 0));
        showtextmouse.setText(str);
        showtextmouse.setVisible(true);
    }

    public static void showEquipmentInfo(String str, double x, double y) {
        int X = (int) (x * one_px_equivalent_naut);
        int Y = (int) (y * one_px_equivalent_naut);
        showEquipmentInfoInLabel.setLocation(X, Y);
        showEquipmentInfoInLabel.setFont(new java.awt.Font("Tahoma", 1, 11));
        showEquipmentInfoInLabel.setForeground(new java.awt.Color(153, 0, 0));
        showEquipmentInfoInLabel.setText(str);
        showEquipmentInfoInLabel.setVisible(true);
    }

    public static void customDrawString(Graphics g, String str, Point point) {
        Graphics2D graphics2d = (Graphics2D) g;
        push_matrix(graphics2d);
        graphics2d.scale(1, -1);
        graphics2d.drawString(str, (float) (point.getX() + 5), (float) -((point.getY() + 5)));
        pop_matrix(graphics2d);
    }

    public static void customDrawStringRotatingText(Graphics g, String str, Point point, double Angle) {
        Graphics2D graphics2d = (Graphics2D) g;
        push_matrix(graphics2d);
        graphics2d.scale(1, -1);
        AffineTransform at = new AffineTransform();
        at.setToRotation(Math.toRadians(45), 10, 10);
        graphics2d.setTransform(at);
        graphics2d.drawString(str, (float) (point.getX() + 5), (float) -((point.getY() + 5)));
        pop_matrix(graphics2d);
    }

    public static void customDrawStringAngleText(Graphics g, String str, int x, int y, double Angle) {
        Graphics2D graphics2d = (Graphics2D) g;
        graphics2d.scale(1, -1);
        graphics2d.setTransform(AffineTransform.getRotateInstance(Math.toRadians(Angle), x, y));
        graphics2d.drawString(str, x + 5, y + 5);
        pop_matrix(graphics2d);
    }

    public static void customDrawString2(Graphics g, String str, Point point) {
        Graphics2D graphics2d = (Graphics2D) g;
        push_matrix(graphics2d);
        graphics2d.scale(1, -1);
        graphics2d.drawString(str, (point.x + 5), -(point.y + 45));
        pop_matrix(graphics2d);
    }

    public static void customDrawString3(Graphics g, String str, Point point) {
        Graphics2D graphics2d = (Graphics2D) g;
        push_matrix(graphics2d);
        graphics2d.scale(1, -1);
        graphics2d.drawString(str, (point.x + 5), -(point.y + 25));
        pop_matrix(graphics2d);
    }

    public static void customDrawString4(Graphics g, String str, Point point) {
        Graphics2D graphics2d = (Graphics2D) g;
        push_matrix(graphics2d);
        graphics2d.scale(1, -1);
        Ellipse2D.Double theCircle = new Ellipse2D.Double();
        graphics2d.drawString(str, (point.x + 5), -(point.y + 25));
        pop_matrix(graphics2d);

    }

    public void fix_map_movement() {
        Coordinate coOrdinate = new Coordinate(0, 0);
        Point p = this.getMapPosition(coOrdinate, false);
        translate_x = p.x;
        translate_y = p.y;
        one_px_equivalent_naut = (float) this.getOnePixEquivalentNM();
        one_nm_equivalent_pixel = 1 / one_px_equivalent_naut;
    }

    int a = 0, b = 0;

    public class CustomWheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                if (currentZoom == 16) {
                } else {
                    currentZoom++;
                }
                setShowZoomScale(1);
                if (zoomShow > 3 && zoomShow <= 16 && b != 1) {
                    if (zoomShow == 4) {
                        a = 0;
                    }
                    if (zoomShow == 16) {
                        b = 1;
                    }
                    displayAreaWidth = displayAreaWidth / 2;
                    distanceOfTwoClicks /= 1.5;
                }
                OpenGIS_MAIN.paintIt();
            } else {
                if (currentZoom == 3) {

                } else {
                    currentZoom--;
                }
                setShowZoomScale(1);
                if (zoomShow >= 3 && zoomShow < 16 && a != 1) {
//                    selectedRadarX = selectedRadarX / 2;
                    if (zoomShow == 3) {
                        a = 1;
                    }
                    if (zoomShow == 16) {
                        b = 0;
                    }

                    displayAreaWidth = displayAreaWidth * 2;
                    distanceOfTwoClicks *= 1.5;
                }
                OpenGIS_MAIN.paintIt();
            }

            OpenGIS_MAIN.this.fix_map_movement();
            OpenGIS_MAIN.paintIt();
        }
    }

    public int getCenterX() {
        return this.center.x;
    }

    public int getCenterY() {
        return this.center.y;
    }

    public void DrawGR() {
        drawGR = true;
    }

    public void fixedRangeBearingOverZoomLevel(GameScene gs) {

    }

}
