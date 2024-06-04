package udesc.dsd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ConnectionService {

    private final String ip;
    private final int port;
    private String peerIp;
    private int peerPort;
    private BufferedReader in;
    private PrintWriter out;
    private Socket peerConnection;
    private Socket entranceConnection;
    private ServerSocket listener;
    private Thread listenerThread;

    private String token;

    private static final String TOKEN_MESSAGE = "token::";
    private static final String DELIMITER = ";; ";

    public ConnectionService(String ip, int port) throws IOException{
        this.ip = ip;
        this.port = port;
        listener = new ServerSocket(port);
        listenerThread = new Thread(this::listen);
        requestServerToEnterRing();
        listenerThread.start();
    }

    private void sendTokenToPeer(){
        out.println(TOKEN_MESSAGE + token);
    }

    private void listen(){
        try {
            while (true) {
                entranceConnection = listener.accept();
                System.out.println("Connection accepted.");
                
                setIn();

                String message = in.readLine();
                String[] request = message.split(DELIMITER);

                int font = Integer.parseInt(request[0]);

                if (request[2].contains(TOKEN_MESSAGE))
                    token = request[2].replace(TOKEN_MESSAGE, "");

                new Runnable[]{
                    () -> setPeerConnection(request[1], Integer.parseInt(request[2])), //0 for server messages;
                    () -> doAction(token)                                              //1 for client messages;
                }[font].run();

                entranceConnection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPeerConnection(String ip, int port){
        try {
            
            this.peerIp=ip;
            this.peerPort=port;
            peerConnection = new Socket(peerIp, peerPort);
            setOut();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doAction(String token){
        try {

            Thread.sleep(new Random().nextLong(1000, 3000));
            sendTokenToPeer();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Action done.");
    }

    public void setIn() throws IOException{
        this.in = new BufferedReader(new InputStreamReader(entranceConnection.getInputStream()));
    }
    private void setOut() throws IOException{
        this.out = new PrintWriter(peerConnection.getOutputStream(), true); // true para autoflush
    }

    private void requestServerToEnterRing(){
        try {

            Socket serverConnection = new Socket("localhost", 65000);
            PrintWriter out = new PrintWriter(serverConnection.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));

            out.println("enter" + DELIMITER + ip + DELIMITER + port);
            System.out.println("Request sent to server. Await for response...");

            String response = in.readLine();
            boolean serverAccepted = response.equals("ok");
            System.out.println(serverAccepted? "Server accepted request. You are in the ring!" : "Server refused request.");

            if(!serverAccepted) return;

            response = in.readLine();
            String[] request = response.split(DELIMITER);

            setPeerConnection(request[1], Integer.parseInt(request[2]));

            serverConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}