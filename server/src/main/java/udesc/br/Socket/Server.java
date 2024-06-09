package udesc.br.Socket;

import udesc.br.model.Host;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;

import static udesc.br.commons.Colors.*;
import static udesc.br.commons.Constants.DELIMITER;
import static udesc.br.commons.Constants.TOKEN;

public class Server {
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
                System.out.println(YELLOW + connectionIp + " solicitou a entrada no anel");

                setIn();
                setOut();

                int port = Integer.parseInt(in.readLine());

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

        startRing();
    }

    public void startRing() throws IOException, InterruptedException {
        System.out.println(YELLOW + "Enviando token para o primeiro membro do anel!");
        Thread.sleep(5000);
        System.out.println(GREEN + "Token enviado!");
        sendTokenTo(hosts[0]);
        observeRing();
    }

    private void observeRing(){
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
        host.getOut().println(0 + DELIMITER + host.getPeerIp() + DELIMITER + host.getPeerPort());
    }

    private void sendTokenTo(Host host) {
        host.getOut().println(2 + DELIMITER + " " + DELIMITER + TOKEN);
    }
}