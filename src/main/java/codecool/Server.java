package codecool;

import codecool.structures.ReadMic;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

    private void start() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                byte[] buf = new byte[128000];
                is.read(buf, 0, buf.length);
                ReadMic.playSound(buf);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
