package codecool;

public class ClientThread extends Thread{

    private Client client;
    private String mode;

    public ClientThread(Client client, String mode) {
        this.mode = mode;
        this.client = client;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (mode.equals("get")) client.getSound();
            else  client.sendSound();
        }
    }
}
