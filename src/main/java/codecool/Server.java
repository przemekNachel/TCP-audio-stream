package codecool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.*;

public class Server {
    private int port;

    private Server(int port)  {
        this.port = port;
    }

    private void start() {
        byte tempBuffer[] = new byte[Format.BUFFER_SIZE];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, Format.format);
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(Format.format);
            targetDataLine.start();
            while (true) {
                int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                out.write(tempBuffer);
                if (cnt > 0) byteArrayOutputStream.write(tempBuffer, 0, cnt);
            }
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }
}