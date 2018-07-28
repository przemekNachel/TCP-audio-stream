package codecool;

public class ServerThread extends Thread{

    private Server server;
    private String mode;

    public ServerThread(Server server, String mode) {
        this.mode = mode;
        this.server = server;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (mode.equals("play")) server.playSound();
            else  server.getSound();
        }
    }
}
