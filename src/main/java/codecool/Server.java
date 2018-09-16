package codecool;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class Server {

    private final int port;
    private static volatile byte[] tempBuffer;
    private static ArrayList<BufferedOutputStream> clients = new ArrayList<>();
    private static TargetDataLine targetDataLine;

    private Server(int port)  {
        this.port = port;
        tempBuffer = new byte[Format.BUFFER_SIZE];
    }

    private void start() {
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, Format.format));
            targetDataLine.open(Format.format);
            targetDataLine.start();
            ServerSocket serverSocket = new ServerSocket(port);
            new Sender().start();
            while (true) {
                clients.add(new BufferedOutputStream(serverSocket.accept().getOutputStream()));
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

    private static class Sender extends Thread {

        @Override
        public void run() {

            while (true) {
                targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                clients.forEach(out -> {
                    try {
                        out.write(tempBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}