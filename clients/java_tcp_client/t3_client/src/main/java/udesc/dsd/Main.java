package udesc.dsd;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        int basePort = 50000;
        String localhost = "localhost";

        String[] fakeUserNames = new String[]{
                "Volecir Torres",
                "Jacimiro",
                "Rubisclaiton",
                "Arandelina Barbosa Correia",
                "Perislau Penácio"
        };

        for (int i = 0; i < 5; i++) {
            new FakeClientThread(basePort + i, fakeUserNames[i]).start();
            Thread.sleep(3000);
        }
    }
}