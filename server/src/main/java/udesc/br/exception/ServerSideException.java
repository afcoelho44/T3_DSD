package udesc.br.exception;

public class ServerSideException extends RuntimeException{
    public ServerSideException(){
        super("Server error!");
    }
}
