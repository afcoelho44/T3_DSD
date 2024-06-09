package udesc.dsd;

import udesc.dsd.Socket.ConnectionService;

import java.io.IOException;

public class FakeClientThread extends Thread{

    private final int port;
    private final String userName;

    public FakeClientThread(int port, String username){
        this.port = port;
        this.userName = username;
    }

    @Override
    public void run() {
        try {
            new ConnectionService(port, userName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
