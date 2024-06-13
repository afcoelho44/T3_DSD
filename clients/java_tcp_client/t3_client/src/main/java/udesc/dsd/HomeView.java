package udesc.dsd;

import udesc.dsd.Socket.ConnectionService;

import javax.swing.*;

public class HomeView extends JFrame {

    private ConnectionService service;

    public HomeView(){
        setLocationRelativeTo(null);
        setSize(550, 500);
        setVisible(true);
        setLayout(null);
        setTitle("Token ring");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents() {
        lbUsername = new JLabel("Nome de usuário");
        lbPorta = new JLabel("Porta: ");
        lbServerRequest = new JLabel("Entrando em contato com o servidor, aguarde a resposta");
        lbServerRequest.setVisible(false);

        add(lbUsername);
        add(lbPorta);
        add(lbServerRequest);
        add(new JLabel());

        lbUsername.setBounds(20, 30, 200, 25);
        lbPorta.setBounds(20, 100, 200, 25);
        lbServerRequest.setBounds(105, 250, 400, 25);

        tfUserName = new JTextField();
        add(tfUserName);
        tfUserName.setBounds(20, 55, 200, 25);

        tfPort = new JTextField();

        add(tfPort);
        tfPort.setBounds(20, 120, 200, 25);

        btStart = new JButton("Iniciar");
        btStart.addActionListener(a -> sendRequestToEnterRing());
        add(btStart);
        btStart.setBounds(20, 170, 100, 25);

        btRequest = new JButton("Solicitar");
        btRequest.addActionListener(a -> sendRequest());
        add(btRequest);
        btRequest.setBounds(105, 280, 200, 25);
    }

    private void sendRequestToEnterRing(){
        try {
            String userName = tfUserName.getText();
            int port = Integer.parseInt(tfPort.getText());

            service = new ConnectionService(port, userName);
            lbServerRequest.setVisible(true);

            new Thread( () -> {
                boolean connected = service.requestServerToEnterRing();

                if(connected){
                    lbServerRequest.setText("Conectado com sucesso.");
                    disableInputFields();
                } else {
                    lbServerRequest.setText("Erro ao conectar.");
                }
            }).start();

        } catch (Exception e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void sendRequest(){

    }

    private void disableInputFields() {
        tfUserName.setEnabled(false);
        tfPort.setEnabled(false);
        btStart.setEnabled(false);
    }

    private JButton btStart;
    private JButton btRequest;
    private JTextField tfUserName;
    private JTextField tfPort;
    private JLabel lbUsername;
    private JLabel lbPorta;
    private JLabel lbServerRequest;
}
