package udesc.br.model;

import udesc.br.commons.ClientLanguageMap;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection extends Thread {

    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;
    private String[] request;
    private LanguageData language;

    public ClientConnection(Socket socket, BufferedReader in, PrintWriter out, String[] request) {
        this.connection = socket;
        this.in = in;
        this.out = out;
        int laguageIndex = Integer.parseInt(request[0]);
        language = ClientLanguageMap.getLanguage(laguageIndex);
    }

    @Override
    public void run() {
        while (true) {
            try{
                out.println("Olá usuário de " + language.name() + "! Estou te ouvindo!");
                Thread.sleep(10_000);
            } catch (InterruptedException e){
                e.printStackTrace();
                break;
            }
        }
    }
}
