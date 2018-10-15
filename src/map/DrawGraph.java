/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author Shahjahan
 */
@SuppressWarnings("serial")
public class DrawGraph extends JPanel {

    private static int Extra = 2;
    private static int MAX_SCORE = 20;
    private static int PREF_W = 800;
    private static int PREF_H = 650;
    private static int BORDER_GAP = 60;
    private static final Color GRAPH_COLOR = Color.green;
    private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static int GRAPH_POINT_WIDTH = 12;
    private static int Y_HATCH_CNT = 10;
    private List<Integer> scores;
    public String FirstPoint = " First";
    public String SecondPoint = " Second";

    public DrawGraph(List<Integer> scores) {
        this.scores = scores;
    }

    public DrawGraph() {

    }

    public String getFirstPoint() {
        return FirstPoint;
    }

    public void setFirstPoint(String FirstPoint) {
        this.FirstPoint = FirstPoint;
    }

    public String getSecondPoint() {
        return SecondPoint;
    }

    public void setSecondPoint(String SecondPoint) {
        this.SecondPoint = SecondPoint;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * BORDER_GAP)) / (scores.size() - 1);
        double yScale = ((double) getHeight() - (2 * BORDER_GAP)) / (MAX_SCORE - 1);

        List<Point> graphPoints = new ArrayList<Point>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + BORDER_GAP);
            int y1 = (int) ((MAX_SCORE - scores.get(i)) * yScale + BORDER_GAP);
//            System.out.println("X value: " + x1 + " Y value: " + y1);
            graphPoints.add(new Point(x1, y1));
        }

        // create x and y axes 
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
        g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

        // create hatch marks for y axis. 
        for (int i = 0; i < MAX_SCORE + Extra; i++) {
            int x0 = BORDER_GAP;
            int x1 = getWidth() - BORDER_GAP + Extra;
            int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / MAX_SCORE + BORDER_GAP);
            int y1 = y0;
            g2.drawLine(x0, y0, x1, y1);
            g2.drawString(i + 1 + " Meter", x0 - 50, y0 + 5);
        }

        // and for x axis
//        for (int i = 0; i < scores.size() - 1; i++) {
////            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
//            int x0 = (i + 1) * (getWidth() - BORDER_GAP) / (scores.size() - 1) + BORDER_GAP;
//            int x1 = x0;
//            int y0 = getHeight() - BORDER_GAP;
//            int y1 = BORDER_GAP;
//            g2.drawLine(x0, y0, x1, y1);
//            g2.drawString("" + i, x0, y0 + 20);
////            if (i == 0) {
////                g2.drawString(getFirstPoint(), x0 - 20, y0 + 20);
////            } else if (i == scores.size() - 2) {
////                g2.drawString(getSecondPoint(), x0 - 20, y0 + 20);
////            } else {
////                g2.drawString("" + (i++), x0, y0 + 20);
////            }
//
//        }
        for (int i = 0; i < scores.size(); i++) {
//            int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / (scores.size() - 1) + BORDER_GAP;
            int x0 = (i) * (getWidth() - BORDER_GAP) / (scores.size()) + BORDER_GAP;
            int x1 = x0;
            int y0 = getHeight() - BORDER_GAP;
            int y1 = BORDER_GAP - Extra;
            g2.drawLine(x0, y0, x1, y1);
            g2.drawString("" + i, x0, y0 + 20);
//            if (i == 0) {
//                g2.drawString(getFirstPoint(), x0 - 20, y0 + 20);
//            } else if (i == scores.size() - 2) {
//                g2.drawString(getSecondPoint(), x0 - 20, y0 + 20);
//            } else {
//                g2.drawString("" + (i++), x0, y0 + 20);
//            }

        }

        Stroke oldStroke = g2.getStroke();
        g2.setColor(GRAPH_COLOR);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }

        g2.setStroke(oldStroke);
        g2.setColor(GRAPH_POINT_COLOR);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
            int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;;
            int ovalW = GRAPH_POINT_WIDTH;
            int ovalH = GRAPH_POINT_WIDTH;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_W, PREF_H);
    }

    private static void createAndShowGui() {
        List<Integer> scores = new ArrayList<Integer>();
        Random random = new Random();
        int maxDataPoints = 16;
        int maxScore = 20;
        for (int i = 0; i < maxDataPoints; i++) {
            scores.add(random.nextInt(maxScore));
        }
        DrawGraph mainPanel = new DrawGraph(scores);

        JFrame frame = new JFrame("Elevation Graph");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public void createAndShowGui(List<Integer> Elevation, int MaxElevation, String FirstPoint, String SecondPoint) {

        DrawGraph mainPanel = new DrawGraph(Elevation);
        mainPanel.setFirstPoint(FirstPoint);
        mainPanel.setSecondPoint(SecondPoint);
        mainPanel.setMAX_SCORE(MaxElevation);

        JFrame frame = new JFrame("Elevation Graph");
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }

    public static int getMAX_SCORE() {
        return MAX_SCORE;
    }

    public static void setMAX_SCORE(int MAX_SCORE) {
        DrawGraph.MAX_SCORE = MAX_SCORE;
    }

    public static int getPREF_W() {
        return PREF_W;
    }

    public static void setPREF_W(int PREF_W) {
        DrawGraph.PREF_W = PREF_W;
    }

    public static int getPREF_H() {
        return PREF_H;
    }

    public static void setPREF_H(int PREF_H) {
        DrawGraph.PREF_H = PREF_H;
    }

    public static int getBORDER_GAP() {
        return BORDER_GAP;
    }

    public static void setBORDER_GAP(int BORDER_GAP) {
        DrawGraph.BORDER_GAP = BORDER_GAP;
    }

    public static int getGRAPH_POINT_WIDTH() {
        return GRAPH_POINT_WIDTH;
    }

    public static void setGRAPH_POINT_WIDTH(int GRAPH_POINT_WIDTH) {
        DrawGraph.GRAPH_POINT_WIDTH = GRAPH_POINT_WIDTH;
    }

    public static int getY_HATCH_CNT() {
        return Y_HATCH_CNT;
    }

    public static void setY_HATCH_CNT(int Y_HATCH_CNT) {
        DrawGraph.Y_HATCH_CNT = Y_HATCH_CNT;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }
}
