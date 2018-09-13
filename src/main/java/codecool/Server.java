package codecool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class Server {

    private int port;
    private static final List<BufferedOutputStream> clients = new ArrayList<>();
    private byte[] tempBuffer;
    private TargetDataLine targetDataLine;

    private Server(int port)  {
        this.port = port;
    }

    private void start() {
        tempBuffer = new byte[Format.BUFFER_SIZE];
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(new DataLine.Info(TargetDataLine.class, Format.format));
            ServerSocket serverSocket = new ServerSocket(port);
            targetDataLine.open(Format.format);
            targetDataLine.start();
            while (true) {
                new IncomingRequestHandler(serverSocket.accept(), this).start();
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

    private static class IncomingRequestHandler extends Thread {

        private final Socket socket;
        private final Server server;

        public IncomingRequestHandler(Socket socket, Server server) {
            this.server = server;
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

                synchronized (clients) {
                    clients.add(out);
                }

                while (true) {
                    server.targetDataLine.read(server.tempBuffer, 0, server.tempBuffer.length);
                    clients.forEach(bufferedOutputStream -> {
                        try {
                            bufferedOutputStream.write(server.tempBuffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}