/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import main.OpenGIS_MAIN;

/**
 *
 * @author Shahjahan
 */
public class ShapefileList_Form extends javax.swing.JInternalFrame {

    /**
     * Creates new form ShapefileList
     */
    OpenGIS_MAIN MAIN;

    public ShapefileList_Form(OpenGIS_MAIN MAIN) {
        super("Shapefile List", false, true, false, false);
        this.MAIN = MAIN;
        initComponents();
    }

    public ShapefileList_Form() {
        LoadShapefileList();
    }
    
    public void LoadShapefileList() {
        int Height = 10;
        int TotalShapefile = MAIN.ShapefileList.size();
        System.out.println("Total JCheckbox to add: " + TotalShapefile);

        for (int i = 0; i < TotalShapefile; i++) {
            final JCheckBox JC = new JCheckBox(MAIN.ShapefileList.get(i).toString(), true);
            JC.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (JC.isSelected()) {
                        for (int j = 0; j < MAIN.MapLayer.ShapeFileList.size(); j++) {
                            if (MAIN.MapLayer.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
                                MAIN.MapLayer.ShapeFileList.get(j).setDrawable(true);
                            }
                        }
                    } else {
                        for (int j = 0; j < MAIN.MapLayer.ShapeFileList.size(); j++) {
                            if (MAIN.MapLayer.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
                                MAIN.MapLayer.ShapeFileList.get(j).setDrawable(false);
                            }
                        }
                        System.out.println("Unselected Shapefile: " + JC.getText());
                    }
                }
            });
            JC.setBounds(5, Height, 180, 25);
            ShapefileListHolder.add(JC);

            final JButton CloseButton = new JButton("x");
            CloseButton.setName(MAIN.ShapefileList.get(i).toString());
            CloseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    for (int j = 0; j < htbParent.ShapefileList.size(); j++) {
//                        if (htbParent.ShapefileList.get(j).equals(CloseButton.getName())) {
//                            htbParent.ShapefileList.remove(j);
//                        }
//                    }
//                    htbParent.RemoveSpecificShapefileData(CloseButton.getName());
                    for (int m = 0; m < MAIN.MapLayer.ShapeFileList.size(); m++) {
                        if (MAIN.MapLayer.ShapeFileList.get(m).getShapefileName().equals(CloseButton.getName())) {
                            MAIN.MapLayer.ShapeFileList.remove(m);
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

//    public void LoadShapefileList() {
//        int Height = 10;
//        int TotalShapefile = htbParent.ShapefileList.size();
//        System.out.println("Total JCheckbox to add: " + TotalShapefile);
//
//        for (int i = 0; i < TotalShapefile; i++) {
//            final JCheckBox JC = new JCheckBox(htbParent.ShapefileList.get(i).toString(), true);
//            JC.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    if (JC.isSelected()) {
//                        for (int j = 0; j < htbParent.gameScene.ShapeFileList.size(); j++) {
//                            if (htbParent.gameScene.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
//                                htbParent.gameScene.ShapeFileList.get(j).setDrawable(true);
//                            }
//                        }
//                    } else {
//                        for (int j = 0; j < htbParent.gameScene.ShapeFileList.size(); j++) {
//                            if (htbParent.gameScene.ShapeFileList.get(j).getShapefileName().equals(JC.getText())) {
//                                htbParent.gameScene.ShapeFileList.get(j).setDrawable(false);
//                            }
//                        }
//                        System.out.println("Unselected Shapefile: " + JC.getText());
//                    }
//                }
//            });
//            JC.setBounds(5, Height, 180, 25);
//            ShapefileListHolder.add(JC);
//
//            final JButton CloseButton = new JButton("x");
//            CloseButton.setName(htbParent.ShapefileList.get(i).toString());
//            CloseButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
////                    for (int j = 0; j < htbParent.ShapefileList.size(); j++) {
////                        if (htbParent.ShapefileList.get(j).equals(CloseButton.getName())) {
////                            htbParent.ShapefileList.remove(j);
////                        }
////                    }
////                    htbParent.RemoveSpecificShapefileData(CloseButton.getName());
//                    for (int m = 0; m < htbParent.gameScene.ShapeFileList.size(); m++) {
//                        if (htbParent.gameScene.ShapeFileList.get(m).getShapefileName().equals(CloseButton.getName())) {
//                            htbParent.gameScene.ShapeFileList.remove(m);
//                        }
//                    }
//
//                    ShapefileListHolder.revalidate();
//                    ShapefileListHolder.repaint();
//                    System.out.println("Selected Map Layer to Close is: " + CloseButton.getName());
//                }
//            });
//            CloseButton.setBounds(200, Height, 35, 25);
//            ShapefileListHolder.add(CloseButton);
//            Height += 25;
//        }
//        ShapefileListHolder.revalidate();
//        ShapefileListHolder.repaint();
//    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ShapefileListHolder = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Map Layer List");

        javax.swing.GroupLayout ShapefileListHolderLayout = new javax.swing.GroupLayout(ShapefileListHolder);
        ShapefileListHolder.setLayout(ShapefileListHolderLayout);
        ShapefileListHolderLayout.setHorizontalGroup(
            ShapefileListHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
        );
        ShapefileListHolderLayout.setVerticalGroup(
            ShapefileListHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 258, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ShapefileListHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ShapefileListHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
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
            java.util.logging.Logger.getLogger(ShapefileList_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShapefileList_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShapefileList_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShapefileList_Form.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ShapefileList().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ShapefileList().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ShapefileList().setVisible(true);
//            }
//        });
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ShapefileList().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel ShapefileListHolder;
    // End of variables declaration//GEN-END:variables
}
