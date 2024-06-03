package udesc.br;

import udesc.br.Socket.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException{
        run();
    }

    private static void run() throws IOException {
        System.out.println( "Server starting...");
        while (isRunning){
            Server server = new Server(new ServerSocket(65000));
            server.serve();
            continueRunning();
        }
    }

    private static void continueRunning(){
        String answer = "";
        while (!answer.equals("y")){
            System.out.println( "Want to restart server? [y/n]");
            Scanner s = new Scanner(System.in);
            answer = s.next();
            if (answer.equals("n")) {
                isRunning = false;
                break;
            }
            System.out.println( "Server restarting...");
        }
        System.out.println("Shutting down");
    }


    private static boolean isRunning = true;
}