package codecool;

import java.io.*;
import java.net.ServerSocket;
import javax.sound.sampled.*;

public class Server {

    private final int port;
    private static byte[] tempBuffer;
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
            while (true) {
                new IncomingRequestHandler(new BufferedOutputStream(serverSocket.accept().getOutputStream())).start();
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

    private static class IncomingRequestHandler extends Thread {

        private final BufferedOutputStream out;

        public IncomingRequestHandler(BufferedOutputStream out) {
            this.out = out;
        }

        @Override
        public void run() {

            while (true) {
                targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                try {
                    out.write(tempBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}