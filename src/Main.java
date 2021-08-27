package src;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        Visualizer panel = new Visualizer();
        
        frame.setTitle("Sorting Algorithms Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(900, 600));
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.add(panel);
        frame.validate();
        frame.pack();
        frame.setVisible(true);
    }
}
