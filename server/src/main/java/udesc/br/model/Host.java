package udesc.br.model;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Host {
    private final String ip;
    private final int port;
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket connection;

    private String peerIp;
    private int peerPort;

    public Host(Socket connection, BufferedReader in, String ip, PrintWriter out, int port) {
        this.connection = connection;
        this.in = in;
        this.ip = ip;
        this.out = out;
        this.port = port;
    }

    public String getPeerIp() {
        return peerIp;
    }

    public void setPeerIp(String peerIp) {
        this.peerIp = peerIp;
    }

    public int getPeerPort() {
        return peerPort;
    }

    public void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
    }

    public Socket getConnection() {
        return connection;
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getIp() {
        return ip;
    }

    public PrintWriter getOut() {
        return out;
    }

    public int getPort() {
        return port;
    }
}
