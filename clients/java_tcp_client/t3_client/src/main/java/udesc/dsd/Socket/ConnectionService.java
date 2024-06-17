package udesc.dsd.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import udesc.dsd.Util.Loggable;
import udesc.dsd.Util.Loggable;

import static udesc.dsd.Commons.Constants.DELIMITER;

public class ConnectionService implements Loggable {

    private String peerIp;
    private int peerPort;
    private final int port;
    private BufferedReader in;
    private PrintWriter out;
    private Socket peerConnection;
    private Socket listenerConnection;
    private final ServerSocket listener;
    private final String userName;
    private String[] request;
    private String token;
    private volatile boolean hasActivity = false;
    private final String serverIp;

    public ConnectionService(String serverIp, int port, String userName) throws IOException {
        this.serverIp = serverIp;
        this.port = port;
        this.userName = userName;
        listener = new ServerSocket(port);
        listener.setReuseAddress(true);
    }

    private void sendTokenToPeer() {
        try {
            out.println(token);
            token = null;
            yellowLog("Sending token to peer at " + peerIp + ":" + peerPort);
        } catch (Exception e) {
            redLog("Failed to send token to peer: " + e.getMessage());
        }
    }

    public void doAction() {
        try {
            if (hasActivity) {
                doRequestedActivity();
            } else {
                blueLog(userName + " has the token");
                Thread.sleep(2000);
                while (hasActivity) Thread.onSpinWait();
            }

            sendTokenToPeer();
        } catch (InterruptedException e) {
            redLog("InterruptedException: " + e.getMessage());
        } catch (Exception e) {
            redLog("Exception in doAction: " + e.getMessage());
        }
    }

    private void doRequestedActivity() throws IOException, InterruptedException {
        greenLog(userName + " is doing something... (token=" + token + ")");
        Thread.sleep(5000);
        hasActivity = false;
        greenLog(userName + " finished the action! Sending token to peer");
    }

    public void receiveTokenHandler() {
        try {
            token = request[0];
            doAction();
        } catch (Exception e) {
            redLog("Exception in receiveTokenHandler: " + e.getMessage());
        }
    }

    private void listen() {
        try {
            listenerConnection = listener.accept();
            setIn();
            greenLog("Connection accepted from " + peerIp + ":" + peerPort);

            while (true) {
                String message = in.readLine();
                request = message.split(DELIMITER);
                receiveTokenHandler();
            }

        } catch (IOException e) {
            redLog("IOException in listen: " + e.getMessage());
        }
    }

    public boolean requestServerToEnterRing() {
        try {
            Socket serverConnection = new Socket(serverIp, 65000);
            PrintWriter out = new PrintWriter(serverConnection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));

            out.println(userName + DELIMITER + port);
            yellowLog("Request sent to server. Await for response...");

            String response = in.readLine();
            boolean serverAccepted = response.equals("ok");

            if (serverAccepted)
                greenLog("Server accepted request. You are in the ring!");
            else {
                redLog("Server refused request.");
                return false;
            }

            response = in.readLine();

            request = response.split(DELIMITER);

            this.peerIp = request[0];
            this.peerPort = Integer.parseInt(request[1]);
            createPeerConnection();

            serverConnection.close();

            new Thread(this::listen).start();
            purpleLog("Started listening.");

            if (request.length > 2) {
                token = request[2];
                log(userName + " received token: " + token + ". Awaiting any instants to start the ring.");
                Thread.sleep(500);
                doAction();
            }

        } catch (IOException | InterruptedException e) {
            redLog("Exception during requesting server to enter ring: " + e.getMessage());
            return false;
        }

        return true;
    }

    public void createPeerConnection() {
        try {
            Thread.sleep(1000);
            peerConnection = new Socket(peerIp, peerPort);
            setOut();
            greenLog("Peer connection established with " + peerIp + ":" + peerPort);
        } catch (IOException | InterruptedException e) {
            redLog("IOException in createPeerConnection: " + e.getMessage());
        }
    }

    public void hasActivity() {
        hasActivity = true;
        if(token != null) {
            try {
                doRequestedActivity();
            } catch (IOException e) {
                redLog("IOException in hasActivity: " + e.getMessage());
            } catch (InterruptedException e) {
                redLog("InterruptedException: " + e.getMessage());
            }
        }
    }

    public void setIn() throws IOException {
        this.in = new BufferedReader(new InputStreamReader(listenerConnection.getInputStream()));
    }

    private void setOut() throws IOException {
        this.out = new PrintWriter(peerConnection.getOutputStream(), true);
    }
}
