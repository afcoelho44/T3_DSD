package udesc.dsd.Socket;

import udesc.dsd.Util.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static udesc.dsd.Commons.Constants.DELIMITER;
import static udesc.dsd.Commons.Constants.TOKEN_MESSAGE;

public class ConnectionService implements Loggable {

    private final int port;
    private BufferedReader in;
    private PrintWriter out;
    private Socket peerConnection;
    private Socket listenerConnection;
    private final ServerSocket listener;
    private final String userName;

    private String token;

    public ConnectionService(int port, String userName) throws IOException{
        this.port = port;
        this.userName = userName;
        listener = new ServerSocket(port);
        requestServerToEnterRing();
    }

    private void sendTokenToPeer(){
        out.println(1 + DELIMITER + TOKEN_MESSAGE + token);
        token = null;
    }

    public void doAction(String token){
        try {
            if(token.isBlank()) return;
            yellowLog(userName + " is doing something... (token=" + token + ")");
            Thread.sleep(new Random().nextLong(1000, 3000));
            greenLog(userName + " finished the action! Sending token to pair");
            sendTokenToPeer();
        } catch (InterruptedException e) {
            redLog(e.getMessage());
        }
    }

    public void firstTokenHandleAction(String token){
        if (token.contains(TOKEN_MESSAGE)){
            token = token.replace(TOKEN_MESSAGE, "");
            doAction(token);
        }
    }

    private void listen(){
        try {
            while (true) {
                listenerConnection = listener.accept();
                greenLog("Connection accepted.");

                setIn();

                String message = in.readLine();
                String[] request = message.split(DELIMITER);

                int font = Integer.parseInt(request[0]);

                new Runnable[]{
                        () -> setPeerConnection(request[1], Integer.parseInt(request[2])), //0 for server messages;
                        () -> doAction(request[1]),                                             //1 for client messages;
                        () -> firstTokenHandleAction(request[2])                           //2 for token handle;
                }[font].run();

                listenerConnection.close();
            }
        } catch (IOException e) {
            redLog(e.getMessage());
        }
    }

    private void requestServerToEnterRing(){
        try {

            Socket serverConnection = new Socket("localhost", 65000);
            PrintWriter out = new PrintWriter(serverConnection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));

            out.println(port);
            yellowLog("Request sent to server. Await for response...");

            String response = in.readLine();
            boolean serverAccepted = response.equals("ok");

            if (serverAccepted) greenLog("Server accepted request. You are in the ring!");
             else {
                redLog("Server refused request.");
                return;
            }

            response = in.readLine();
            String[] request = response.split(DELIMITER);

            setPeerConnection(request[1], Integer.parseInt(request[2]));

            serverConnection.close();

            new Thread(this::listen).start();
            purpleLog("Comecei a ouvir.");
            if(port == 50000)
                new Thread(() -> {
                    token = "testeTeste";
                    doAction(token);
                }).start();
        } catch (IOException e) {
            redLog(e.getMessage());
        }
    }

    public void setPeerConnection(String ip, int port){
        try {
            peerConnection = new Socket(ip, port);
            setOut();
        } catch (IOException e) {
            redLog(e.getMessage());
        }
    }

    public void setIn() throws IOException{
        this.in = new BufferedReader(new InputStreamReader(listenerConnection.getInputStream()));
    }
    private void setOut() throws IOException{
        this.out = new PrintWriter(peerConnection.getOutputStream(), true);
    }
}