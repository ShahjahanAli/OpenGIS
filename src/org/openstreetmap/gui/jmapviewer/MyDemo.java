/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//License: GPL. Copyright 2008 by Jan Peter Stotz

import org.openstreetmap.gui.jmapviewer.JMapViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * Demonstrates the usage of {@link JMapViewer}
 *
 * @author Jan Peter Stotz
 *
 */
public class MyDemo extends JFrame {

    public MyDemo() {
        super("JMapViewer Demo");
        setSize(400, 400);
        final JMapViewer map = new JMapViewer();
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        final JCheckBox showMapMarker = new JCheckBox("Map markers visible");
        showMapMarker.setSelected(map.getMapMarkersVisible());
        showMapMarker.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setMapMarkerVisible(showMapMarker.isSelected());
            }
        });
        panel.add(showMapMarker);
        final JCheckBox showTileGrid = new JCheckBox("Tile grid visible");
        showTileGrid.setSelected(map.isTileGridVisible());
        showTileGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setTileGridVisible(showTileGrid.isSelected());
            }
        });
        panel.add(showTileGrid);
        final JCheckBox showZoomControls = new JCheckBox("Show zoom controls");
        showZoomControls.setSelected(map.getZoomContolsVisible());
        showZoomControls.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                map.setZoomContolsVisible(showZoomControls.isSelected());
            }
        });
        panel.add(showZoomControls);
        add(map, BorderLayout.CENTER);

        //
// map.addMapMarker(new MapMarkerDot(49.814284999, 8.642065999));
// map.addMapMarker(new MapMarkerDot(49.91, 8.24));
// map.addMapMarker(new MapMarkerDot(49.71, 8.64));
// map.addMapMarker(new MapMarkerDot(48.71, -1));
// map.addMapMarker(new MapMarkerDot(49.807, 8.644));
        map.setDisplayPositionByLatLon(-0.223056, -78.5126, 11);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        new MyDemo().setVisible(true);
    }

}
