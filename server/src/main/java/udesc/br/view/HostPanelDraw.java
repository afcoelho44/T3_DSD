package udesc.br.view;

import javax.swing.*;
import java.awt.*;

public class HostPanelDraw extends JLabel {
    public void paintComponent(Graphics g) {
        setSize(100, 100);
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillOval(0, 0, 100, 100);
    }
}
