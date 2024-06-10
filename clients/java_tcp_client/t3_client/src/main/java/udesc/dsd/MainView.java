package udesc.dsd;

import udesc.dsd.Socket.ConnectionService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainView extends JFrame {

    private ConnectionService service;

    public MainView(){
        setSize(500, 500);
        setLayout(null);
        setVisible(true);
        initLabels();
        initButtons();
    }

    private void initLabels() {
        serverConnectionLabel = new JLabel("Desconectado com o servidor.");
        clientConnectionLabel = new JLabel("Desconectado com o par.");

        serverConnectionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
        clientConnectionLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));

        serverConnectionLabel.setForeground(Color.WHITE);
        clientConnectionLabel.setForeground(Color.WHITE);

        switchServerConnection(false);
        switchClientConnection(false);

        serverConnectionLabel.setBounds(10, 10, 200, 50);
        clientConnectionLabel.setBounds(10, 50, 200, 50);

        serverConnectionLabel.setVisible(true);
        clientConnectionLabel.setVisible(true);

        add(serverConnectionLabel);
        add(clientConnectionLabel);
    }

    private void initButtons() {
        JButton buttonStart = new JButton("Iniciar");
        buttonStart.setBounds(100, 100, 100, 25 );
        add(buttonStart);
        buttonStart.setVisible(true);
        buttonStart.addActionListener(e -> startProgram());
    }

    private void switchServerConnection(boolean connected){
        if (connected){
            serverConnectionLabel = new JLabel("Conectado com o servidor.");
            serverConnectionLabel.setBackground(Color.GREEN);
        } else {
            serverConnectionLabel = new JLabel("Desconectado do servidor.");
            serverConnectionLabel.setBackground(Color.RED);
        }
    }

    private void switchClientConnection(boolean connected){
        if (connected){
            clientConnectionLabel = new JLabel("Conectado com o par.");
            clientConnectionLabel.setBackground(Color.GREEN);
        } else {
            clientConnectionLabel = new JLabel("Desconectado do par.");
            clientConnectionLabel.setBackground(Color.RED);
        }
    }

    public void startProgram(){
        try{
            int basePort = 50000;
            String[] fakeUserNames = new String[]{
                    "Volecir Torres",
                    "Jacimiro",
                    "Rubisclaiton",
                    "Arandelina Barbosa Correia",
                    "Perislau Penácio"
            };

            for (int i = 0; i < 5; i++) {
                new FakeClientThread(basePort + i, fakeUserNames[i]).start();
                Thread.sleep(3000);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private JLabel serverConnectionLabel;
    private JLabel clientConnectionLabel;
}
