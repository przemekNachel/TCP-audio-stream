package codecool;

import codecool.structures.ReadMic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private String address;
    private int port;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void start() {
        while (true) {
            try {
                Socket socket = new Socket(address, port);
                OutputStream os = socket.getOutputStream();
                os.write(ReadMic.getAudio());
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Client("192.168.1.2", 7575).start();
    }
}
