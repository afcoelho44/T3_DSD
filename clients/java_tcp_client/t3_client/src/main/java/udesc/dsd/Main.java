package udesc.dsd;

import java.io.IOException;
import main.java.udesc.dsd.ConnectionService;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        ConnectionService connectionService = new ConnectionService();
        connectionService.setIP("localhost");
        connectionService.SendAndReceiveMessage("0; estou programando com um cafézinho.");
    }
}