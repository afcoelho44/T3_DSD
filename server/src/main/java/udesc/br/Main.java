package udesc.br;

import udesc.br.Socket.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
        Server server = new Server(65000, 5);
        server.waitHosts();
        server.mountRing();
        server.startRing();
    }
}