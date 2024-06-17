package udesc.br.Socket;

import udesc.br.model.Host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

import static udesc.br.commons.Colors.*;
import static udesc.br.commons.Constants.DELIMITER;
import static udesc.br.commons.Constants.TOKEN;

public class Server {
    private final String token = "oPeloNoPeitoDoPeDoPedroEhPretoHeHeHe";
    private final ServerSocket server;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private final int maxHosts;
    private Host[] hosts;
    private boolean ringCompleted = false;

    public Server(int port, int maxHosts) throws IOException {
        this.server = new ServerSocket(port);
        this.maxHosts = maxHosts;
        this.hosts = new Host[maxHosts];
    }

    public void waitHosts() throws IOException {
        if (ringCompleted) return;
        this.server.setReuseAddress(true);
        int hostCount = 0;

        while(hostCount < maxHosts){
            try{
                System.out.println(BLUE + "Esperando requisição <-");
                connection = server.accept();
                String connectionIp = connection.getInetAddress().getHostAddress();

                if(connectionIp.equals("127.0.0.1")){
                    connectionIp = InetAddress.getLocalHost().getHostAddress();
                }

                setIn();
                setOut();

                String[] message = in.readLine().split(DELIMITER);

                String username = message[0];
                int port = Integer.parseInt(message[1]);
                System.out.println(YELLOW + username + "("+connectionIp+":"+ port + ")" + " solicitou a entrada no anel");

                Host host = new Host(connection, in, connectionIp, out, port);
                hosts[hostCount++] = host;

                out.println("ok");
                System.out.println(GREEN + "Entrada no anel aceita!");
            } catch (ServerException e){
                String error = e.getMessage();
                System.out.println(error);
                out.println(error);

                if(connection != null){
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (Exception e){
                out.println("Server closed!");
                server.close();
                System.out.println("The server stopped abruptly due an error");
                break;
            }
        }
        ringCompleted = true;
    }

    public void mountRing() throws IOException, InterruptedException {
        if (!ringCompleted) return;
        for (int i = 0; i < maxHosts; i++) {
            Host host = hosts[i];
            Host nextHost = hosts[(i + 1) % maxHosts];

            host.setPeerIp(nextHost.getIp());
            host.setPeerPort(nextHost.getPort());

            sendMessageTo(host);
        }
    }

    public void observeRing(){
        System.out.println("Obiservanu");
        while (true){

        }
    }

    private void setIn() throws IOException {
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void setOut() throws IOException {
        out = new PrintWriter(connection.getOutputStream(), true);
    }

    private void sendMessageTo(Host host) {
        StringBuilder builder = new StringBuilder();

        builder.append(host.getPeerIp())
                .append(DELIMITER)
                .append(host.getPeerPort());

        if (host.equals(hosts[0]))
            builder.append(DELIMITER)
                    .append(token);

        host.getOut().println(builder);
    }
}