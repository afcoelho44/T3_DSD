package udesc.br.view;

import javax.swing.*;
import java.awt.*;

public class RingOperationsView extends JFrame {

    private final Container contentPane = getContentPane();
    private HostPanelDraw[] hosts;

    public RingOperationsView() {
        contentPane.setBackground(Color.gray);
        setTitle("Ring Operations");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 900);
        initAll();
        this.setVisible(true);
    }

    private void initAll() {

        hosts = new HostPanelDraw[]{
                new HostPanelDraw(),
                new HostPanelDraw(),
                new HostPanelDraw(),
                new HostPanelDraw(),
                new HostPanelDraw(),
                new HostPanelDraw()
        };

        for (int i = 0; i < hosts.length; i++) {
            HostPanelDraw draw = getHostPanelDraw(i);
            add(draw);
        }
    }

    private HostPanelDraw getHostPanelDraw(int i) {
        HostPanelDraw draw = hosts[i];
        draw.setText(String.valueOf(i));
        switch(i) {
            case 0 -> draw.setBounds(400, 300, 50, 50);
            case 1 -> draw.setBounds(600, 150, 50, 50);
            case 2 -> draw.setBounds(800, 300, 50, 50);
            case 3 -> draw.setBounds(700, 550, 50, 50);
            case 4 -> draw.setBounds(500, 550, 50, 50);
            default -> {
                draw.setVisible(false);
            }
        }
        return draw;
    }
}
