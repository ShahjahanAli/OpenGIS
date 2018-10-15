/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import map.MapLayer_Form;
import map.ShapefileList_Form;
import tools.DisplayIconTools;
import static tools.DisplayIconTools.measureButtonSelected;
import tools.EditShapefile;
import utility.ToolbarUtil;

/**
 *
 * @author shahjahan
 */
public class MapInterface extends javax.swing.JFrame {

    /**
     * Creates new form ScenarioCreation
     */
    OpenGIS_MAIN board;
    JDesktopPane desk;
    JInternalFrame iframe;

    static int screen_height;
    static int screen_width;

    static int MS_ID = 30;

    public static GameScene gameScene;
    public static GameScene gameScene_temp = new GameScene();
    public static boolean gameFileSelected = true;  //Initial Game File selected by Load Game

    public String IPAddress;
    public boolean isNetworkConnected = false;

    public boolean isGameStarter = false;

    public MapInterface() throws UnknownHostException, SQLException {
        initComponents();
        MapView(MS_ID);
        ClosingOption();
        getScreenSize();
//        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public void getScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(0, 0, screenSize.width, screenSize.height - 40);
    }

    public void MapView(int identity) throws SQLException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        MapViewer.setBounds(0, 0, screenSize.width, screenSize.height - 40);
        MapViewer.setPreferredSize(new Dimension((int) this.MapViewer.getBounds().getWidth(), (int) this.MapViewer.getBounds().getHeight() - 40));
        int Width = (int) this.MapViewer.getBounds().getWidth();
        int Height = (int) this.MapViewer.getBounds().getHeight();

        desk = new JDesktopPane();
        desk.setBounds(0, 0, screenSize.width, screenSize.height - 40);
        iframe = new JInternalFrame("Simulator", true, false, false, false);
        iframe.setBounds(0, 0, screenSize.width, screenSize.height - 40);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) iframe.getUI()).setNorthPane(null);
        iframe.setVisible(true);
        iframe.setBorder(new LineBorder(Color.BLACK, 2));
        iframe.putClientProperty("JDesktopPane.dragMode", "outline");
        try {
            board = new OpenGIS_MAIN(iframe, identity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        iframe.add(board);
        desk.add(iframe);
        MapViewer.add(desk);
//        LoadShapefileList();
//        Toolbars();

        try {
            board.start_program();
        } catch (Exception ex) {
        }
    }

    public void LoadShapefileList() {
        int Height = 10;
        int TotalShapefile = board.ShapefileList.size();
        System.out.println("Total JCheckbox to add: " + TotalShapefile);

        for (int i = 0; i < TotalShapefile; i++) {
            final JCheckBox JC = new JCheckBox(board.ShapefileList.get(i).toString(), true);
            JC.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JC.isSelected()) {
                        for (int j = 0; j < board.MapLayer.ShapeFileList.size(); j++) {
                            if (board.MapLayer.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
                                board.MapLayer.ShapeFileList.get(j).setDrawable(true);
                            }
                        }
                    } else {
                        for (int j = 0; j < board.MapLayer.ShapeFileList.size(); j++) {
                            if (board.MapLayer.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
                                board.MapLayer.ShapeFileList.get(j).setDrawable(false);
                            }
                        }
                        System.out.println("Unselected Shapefile: " + JC.getText());
                    }
                }
            });
            JC.setBounds(5, Height, 180, 25);
            ShapefileListHolder.add(JC);

            final JButton CloseButton = new JButton("x");
            CloseButton.setName(board.ShapefileList.get(i).toString());
            CloseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    for (int m = 0; m < board.MapLayer.ShapeFileList.size(); m++) {
                        if (board.MapLayer.ShapeFileList.get(m).getShapefileName().equals(CloseButton.getName())) {
                            board.MapLayer.ShapeFileList.remove(m);
                        }
                    }
                    ShapefileListHolder.revalidate();
                    ShapefileListHolder.repaint();
                    System.out.println("Selected Map Layer to Close is: " + CloseButton.getName());
                }
            });
            CloseButton.setBounds(200, Height, 35, 25);
            ShapefileListHolder.add(CloseButton);
            Height += 25;
        }
        ShapefileListHolder.revalidate();
        ShapefileListHolder.repaint();
    }

    public MapInterface(String S) throws UnknownHostException {
        IPAddress = InetAddress.getLocalHost().getHostAddress();
    }

    public MapInterface(int I) {

    }

    public void ClosingOption() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                JFrame frame = new JFrame();
                int answer = JOptionPane.showConfirmDialog(frame, "Are you sure you want to Logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else if (answer == JOptionPane.NO_OPTION) {
                    frame.dispose();
                }
            }
        });
    }

    public void Toolbars() throws SQLException {

        EditShapefile ES = new EditShapefile(board, desk);
        ES.setBounds(1280 / 2, 480, 136, 69);
        desk.add(ES, new Integer(10));
        ES.setVisible(true);

        DisplayIconTools displayIcon = new DisplayIconTools(board);
        displayIcon.setBounds(235, 480, 622, 69);
        desk.add(displayIcon, new Integer(10));
        displayIcon.setVisible(true);

        ShapefileList_Form SFL = new ShapefileList_Form(board);
        SFL.setBounds(950, 100, 271, 247);
        desk.add(SFL, new Integer(10));
        SFL.setVisible(true);

    }
    static JLabel showtextmouse;

    public static void showLabelMGRS() {
        showtextmouse.setLocation(100, 100);
        showtextmouse.setFont(new java.awt.Font("Tahoma", 1, 15));
        showtextmouse.setForeground(new java.awt.Color(153, 0, 0));
        showtextmouse.setText(OpenGIS_MAIN.MGRS);
        showtextmouse.setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        SplitPanelMain = new javax.swing.JSplitPane();
        ShapefileListHolder = new javax.swing.JPanel();
        MapViewer = new javax.swing.JPanel();
        ElevationProfileDraw = new javax.swing.JButton();
        rulerToggleButton = new javax.swing.JToggleButton();
        EditObject = new javax.swing.JButton();
        DrawConnectionTest = new javax.swing.JButton();
        ObjectSelection = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        AddMapLayer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("OpenGIS");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1300, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1300, 720));

        SplitPanelMain.setDividerLocation(200);
        SplitPanelMain.setDividerSize(20);

        javax.swing.GroupLayout ShapefileListHolderLayout = new javax.swing.GroupLayout(ShapefileListHolder);
        ShapefileListHolder.setLayout(ShapefileListHolderLayout);
        ShapefileListHolderLayout.setHorizontalGroup(
            ShapefileListHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        ShapefileListHolderLayout.setVerticalGroup(
            ShapefileListHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
        );

        SplitPanelMain.setLeftComponent(ShapefileListHolder);

        MapViewer.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout MapViewerLayout = new javax.swing.GroupLayout(MapViewer);
        MapViewer.setLayout(MapViewerLayout);
        MapViewerLayout.setHorizontalGroup(
            MapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1047, Short.MAX_VALUE)
        );
        MapViewerLayout.setVerticalGroup(
            MapViewerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        SplitPanelMain.setRightComponent(MapViewer);

        ElevationProfileDraw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ElevationProfileIcon.png"))); // NOI18N
        ElevationProfileDraw.setToolTipText("Elevation Profile");
        ElevationProfileDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ElevationProfileDrawActionPerformed(evt);
            }
        });

        rulerToggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/ruler.jpg"))); // NOI18N
        rulerToggleButton.setToolTipText("Ruler");
        rulerToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rulerToggleButtonActionPerformed(evt);
            }
        });

        EditObject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/EditIcon.png"))); // NOI18N
        EditObject.setToolTipText("Edit Object");
        EditObject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditObjectActionPerformed(evt);
            }
        });

        DrawConnectionTest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/DrawConnection.png"))); // NOI18N
        DrawConnectionTest.setToolTipText("Draw LOS Connection");
        DrawConnectionTest.setActionCommand("jButtonRadar");
        DrawConnectionTest.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DrawConnectionTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DrawConnectionTestActionPerformed(evt);
            }
        });

        ObjectSelection.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/select.png"))); // NOI18N
        ObjectSelection.setToolTipText("Object Select");
        ObjectSelection.setActionCommand("jButtonRadar");
        ObjectSelection.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ObjectSelection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ObjectSelectionActionPerformed(evt);
            }
        });

        jMenuBar1.setPreferredSize(new java.awt.Dimension(56, 25));

        jMenu1.setText(" File");

        jMenuItem2.setText("Exit");
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu7.setText("Map Layer");

        AddMapLayer.setText("Add Layer");
        AddMapLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMapLayerActionPerformed(evt);
            }
        });
        jMenu7.add(AddMapLayer);

        jMenuBar1.add(jMenu7);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(SplitPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, 1272, Short.MAX_VALUE)
                .addContainerGap(18, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(229, 229, 229)
                .addComponent(EditObject, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ObjectSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(DrawConnectionTest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rulerToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(ElevationProfileDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(483, 483, 483))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rulerToggleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ElevationProfileDraw, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditObject, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ObjectSelection, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DrawConnectionTest, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SplitPanelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public int ScenarioNumber = 0;


    private void AddMapLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMapLayerActionPerformed
        MapLayer_Form MLF = new MapLayer_Form(board);
        MLF.setVisible(true);
    }//GEN-LAST:event_AddMapLayerActionPerformed

    private void rulerToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rulerToggleButtonActionPerformed
        if (ToolbarUtil.getLastClickedComponent() != null) {
            ToolbarUtil.getLastClickedComponent().setBorder(null);
        }
        if (measureButtonSelected) {
            measureButtonSelected = false;
            board.lastPressedButton = PressedButtonEnum.NONE;
        } else {
            measureButtonSelected = true;
            board.lastPressedButton = PressedButtonEnum.MEASURE_TOOL;
        }
        ToolbarUtil.setLastClickedComponent(this.rulerToggleButton);
    }//GEN-LAST:event_rulerToggleButtonActionPerformed

    private void ElevationProfileDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ElevationProfileDrawActionPerformed
        if (ToolbarUtil.getLastClickedComponent() != null) {
            ToolbarUtil.getLastClickedComponent().setBorder(null);
        }
        this.ElevationProfileDraw.setBorder(ToolbarUtil.getSelectedBorder());
        ToolbarUtil.setLastClickedComponent(this.ElevationProfileDraw);
        board.lastPressedButton = PressedButtonEnum.Draw_ElevationProfile;
    }//GEN-LAST:event_ElevationProfileDrawActionPerformed

    private void EditObjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditObjectActionPerformed
        if (ToolbarUtil.getLastClickedComponent() != null) {
            ToolbarUtil.getLastClickedComponent().setBorder(null);
        }
        this.EditObject.setBorder(ToolbarUtil.getSelectedBorder());
        ToolbarUtil.setLastClickedComponent(this.EditObject);
        board.lastPressedButton = PressedButtonEnum.EDIT_SHAPFILE;
    }//GEN-LAST:event_EditObjectActionPerformed

    private void ObjectSelectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ObjectSelectionActionPerformed
        if (ToolbarUtil.getLastClickedComponent() != null) {
            ToolbarUtil.getLastClickedComponent().setBorder(null);
        }
        this.ObjectSelection.setBorder(ToolbarUtil.getSelectedBorder());
        ToolbarUtil.setLastClickedComponent(this.ObjectSelection);
        board.lastPressedButton = PressedButtonEnum.SELECT_OBJECT;
    }//GEN-LAST:event_ObjectSelectionActionPerformed

    private void DrawConnectionTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DrawConnectionTestActionPerformed
        if (ToolbarUtil.getLastClickedComponent() != null) {
            ToolbarUtil.getLastClickedComponent().setBorder(null);
        }
        this.DrawConnectionTest.setBorder(ToolbarUtil.getSelectedBorder());
        ToolbarUtil.setLastClickedComponent(this.DrawConnectionTest);
        board.lastPressedButton = PressedButtonEnum.DRAW_CONNECTION_TEST;
    }//GEN-LAST:event_DrawConnectionTestActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MapInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MapInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MapInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MapInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MapInterface sc = new MapInterface();
                    sc.setVisible(true);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(MapInterface.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(MapInterface.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddMapLayer;
    private javax.swing.JButton DrawConnectionTest;
    private javax.swing.JButton EditObject;
    private javax.swing.JButton ElevationProfileDraw;
    private javax.swing.JPanel MapViewer;
    private javax.swing.JButton ObjectSelection;
    public static javax.swing.JPanel ShapefileListHolder;
    private javax.swing.JSplitPane SplitPanelMain;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JToggleButton rulerToggleButton;
    // End of variables declaration//GEN-END:variables
}
