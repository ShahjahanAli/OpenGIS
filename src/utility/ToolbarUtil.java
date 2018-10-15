/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;

/**
 *
 * @author ASUS
 */
public class ToolbarUtil {
    
    private static JComponent lastClickedComponent;
    
    public static JComponent getLastClickedComponent() {
        return lastClickedComponent;
    }
    
    public static void setLastClickedComponent(JComponent current) {
        lastClickedComponent = current;
    }
    
    public static Border getSelectedBorder() {
        Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
        return border;
    }
            

}
