package udesc.br.Socket;

import udesc.br.model.ClientConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import static udesc.br.commons.Colors.BLUE;

public class Server {
    private final ServerSocket server;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private List<ClientConnection> clients = new ArrayList<>();

    public Server(ServerSocket server) throws IOException {
        this.server = server;
    }

    public void serve() throws IOException {
        this.server.setReuseAddress(true);
        String msg;
        while(true){
            try{
                System.out.println(BLUE + "Esperando requisição <-");
                connection = server.accept();
                System.out.println(BLUE + "Alguém solicitou");
                setIn();
                setOut();

                msg = in.readLine();
                String[] request = msg.split("; ");
                System.out.println(request[1]);

                ClientConnection client = new ClientConnection(connection, in, out, request);
                client.start();
                clients.add(client);

                System.out.println("Requisição rodando na " + client.getName());
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