/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Shahjahan
 */
public class ElevationGraph extends javax.swing.JFrame {

    private static int MAX_SCORE = 20;
    private static int PREF_W = 800;
    private static int PREF_H = 650;
    private static int BORDER_GAP = 60;
    private static final Color GRAPH_COLOR = Color.green;
    private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
    private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
    private static int GRAPH_POINT_WIDTH = 12;
    private static int Y_HATCH_CNT = 10;
    private List<Double> scores;

    /**
     * Creates new form ElevationGraph
     */
    public ElevationGraph() {
        initComponents();
    }

    public ElevationGraph(List<Double> Elevation, int MaxElevation, double UnitDistance, String FirstPoint, String SecondPoint) {
        this.scores = Elevation;
        initComponents();
        GraphPanel mainPanel = new GraphPanel();
        jPanel1.add(mainPanel);
        mainPanel.createAndShowGui_ElevationGraph(Elevation, MaxElevation, UnitDistance, FirstPoint, SecondPoint);
        pack();
        this.setVisible(true);
    }

    class GraphPanel extends JPanel {

        private int width = 800;
        private int heigth = 400;
        private int padding = 25;
        private int labelPadding = 25;
        private Color lineColor = new Color(44, 102, 230, 180);
        private Color pointColor = new Color(100, 100, 100, 180);
        private Color gridColor = new Color(200, 200, 200, 200);
        private final Stroke GRAPH_STROKE = new BasicStroke(2f);
        private int pointWidth = 4;
        private int numberYDivisions = 10;
        private List<Double> scores;
        private int UnitDivider = 10;
        private double UnitDistance = 0;

        public GraphPanel() {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(220, 40));
            panel.setBackground(Color.BLACK);
            add(panel);
        }

        public GraphPanel(List<Double> scores) {
            this.scores = scores;
//        if(this.scores.size() > ){
//            
//        }
        }

        public int getUuniDivider() {
            return UnitDivider;
        }

        public void setUuniDivider(int UuniDivider) {
            this.UnitDivider = UuniDivider;
        }

        public double getUnitDistance() {
            return UnitDistance;
        }

        public void setUnitDistance(double UnitDistance) {
            this.UnitDistance = UnitDistance;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
            double yScale = (((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore()));

            List<Point> graphPoints = new ArrayList<>();
            for (int i = 0; i < scores.size(); i++) {
                int x1 = (int) (i * xScale + padding + labelPadding);
                int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
                graphPoints.add(new Point(x1, y1));
            }

            // draw white background
            g2.setColor(Color.WHITE);
            g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
            g2.setColor(Color.BLACK);

            // create hatch marks and grid lines for y axis.
            for (int i = 0; i < numberYDivisions + 1; i++) {
                int x0 = padding + labelPadding;
                int x1 = pointWidth + padding + labelPadding;
                int y0 = (getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding));
                int y1 = y0;
                if (scores.size() > 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                    g2.setColor(Color.BLACK);
                    String yLabel = ((int) ((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + " m";
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(yLabel);
                    g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }

            // and for x axis
            for (int i = 0; i < scores.size(); i++) {
                if (scores.size() > 1) {
                    int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
                    int x1 = x0;
                    int y0 = getHeight() - padding - labelPadding;
                    int y1 = y0 - pointWidth;
                    if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                        g2.setColor(gridColor);
                        g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                        g2.setColor(Color.BLACK);
                        String xLabel = null;
                        if (getUnitDistance() >= 1000) {
                            java.text.DecimalFormat df = new java.text.DecimalFormat("####.##");
                            double IntervelDistance = Double.parseDouble(df.format(i * getUnitDistance() / 1000));
                            xLabel = IntervelDistance + " Km";
                        } else {
                            java.text.DecimalFormat df = new java.text.DecimalFormat("####.##");
                            double IntervelDistance = Double.parseDouble(df.format(i * getUnitDistance()));
                            xLabel = IntervelDistance + " m";
                        }

                        FontMetrics metrics = g2.getFontMetrics();
                        int labelWidth = metrics.stringWidth(xLabel);
                        g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                    }
                    g2.drawLine(x0, y0, x1, y1);
                }
            }

            // create x and y axes 
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
            g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

            Stroke oldStroke = g2.getStroke();
            g2.setColor(lineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints.size() - 1; i++) {
                int x1 = graphPoints.get(i).x;
                int y1 = graphPoints.get(i).y;
                int x2 = graphPoints.get(i + 1).x;
                int y2 = graphPoints.get(i + 1).y;
                g2.drawLine(x1, y1, x2, y2);
            }

            g2.setStroke(oldStroke);
            g2.setColor(pointColor);
            for (int i = 0; i < graphPoints.size(); i++) {
                int x = graphPoints.get(i).x - pointWidth / 2;
                int y = graphPoints.get(i).y - pointWidth / 2;
                int ovalW = pointWidth;
                int ovalH = pointWidth;
                g2.fillOval(x, y, ovalW, ovalH);
                g2.drawString(scores.get(i) + " m", x, y);
            }
        }

        private double getMinScore() {
            double minScore = Double.MAX_VALUE;
            for (Double score : scores) {
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }

        private double getMaxScore() {
            double maxScore = Double.MIN_VALUE;
            for (Double score : scores) {
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        }

        public void setScores(List<Double> scores) {
            this.scores = scores;
            invalidate();
            this.repaint();
        }

        public List<Double> getScores() {
            return scores;
        }

        public void createAndShowGui(List<Double> Elevation, int MaxElevation, double UnitDistance, String FirstPoint, String SecondPoint) {

            GraphPanel mainPanel = new GraphPanel(Elevation);
            mainPanel.setPreferredSize(new Dimension(800, 600));
            mainPanel.setNumberYDivisions(MaxElevation);
            mainPanel.setUnitDistance(UnitDistance);

            JFrame frame = new JFrame("DrawGraph");
            frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(mainPanel);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        }

        public void createAndShowGui_ElevationGraph(List<Double> Elevation, int MaxElevation, double UnitDistance, String FirstPoint, String SecondPoint) {
            GraphPanel mainPanel = new GraphPanel(Elevation);
            mainPanel.setPreferredSize(new Dimension(800, 600));
            mainPanel.setNumberYDivisions(MaxElevation);
            mainPanel.setUnitDistance(UnitDistance);
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeigth() {
            return heigth;
        }

        public void setHeigth(int heigth) {
            this.heigth = heigth;
        }

        public int getPadding() {
            return padding;
        }

        public void setPadding(int padding) {
            this.padding = padding;
        }

        public int getLabelPadding() {
            return labelPadding;
        }

        public void setLabelPadding(int labelPadding) {
            this.labelPadding = labelPadding;
        }

        public Color getLineColor() {
            return lineColor;
        }

        public void setLineColor(Color lineColor) {
            this.lineColor = lineColor;
        }

        public Color getPointColor() {
            return pointColor;
        }

        public void setPointColor(Color pointColor) {
            this.pointColor = pointColor;
        }

        public Color getGridColor() {
            return gridColor;
        }

        public void setGridColor(Color gridColor) {
            this.gridColor = gridColor;
        }

        public int getPointWidth() {
            return pointWidth;
        }

        public void setPointWidth(int pointWidth) {
            this.pointWidth = pointWidth;
        }

        public int getNumberYDivisions() {
            return numberYDivisions;
        }

        public void setNumberYDivisions(int numberYDivisions) {
            this.numberYDivisions = numberYDivisions;
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Elevation Graph");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1054, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 566, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ElevationGraph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ElevationGraph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ElevationGraph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ElevationGraph.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ElevationGraph().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
