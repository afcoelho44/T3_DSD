package udesc.br.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

public class Server {
    private final ServerSocket server;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;

    public Server(ServerSocket server) throws IOException {
        this.server = server;

    }

    public void serve() throws IOException {
        this.server.setReuseAddress(true);
        String msg;
        while(true){
            try{
                connection = server.accept();

                setIn();
                setOut();

                msg= in.readLine();
                System.out.println(msg);
                connection.close();
                System.out.println("Socket closed.");
            }catch (ServerException e){
                String error = e.getMessage();
                System.out.println(error);
                out.println(error);

                if(connection != null){
                    connection.close();
                    System.out.println("Connection closed.");
                }
            }catch (Exception e){
                out.println("Server closed!");
                server.close();
                System.out.println("The server stopped abruptly due an error");
                break;
            }
        }
    }
    private void setIn() throws IOException {
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }
    private void setOut() throws IOException {
        out = new PrintWriter(connection.getOutputStream(), true);
    }
}