/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpmcsimulator;

import javax.swing.UIManager;

/**
 *
 * @author hui
 */
public class JPMCSimulator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (Exception e) {
            System.out.println("Theme not found");
        }


        JFrameMain main = new JFrameMain();
        main.setVisible(true);
    }
}
