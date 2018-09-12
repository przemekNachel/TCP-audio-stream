package codecool;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Server {
    private int port;
    private byte audio[] = new byte[Format.BUFFER_SIZE];

    private Server(int port)  {
        this.port = port;
    }

    private void start() {
        try {
            SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(Format.format);
            sourceDataLine.open(Format.format);
            sourceDataLine.start();
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            InputStream in = new BufferedInputStream(socket.getInputStream());
            while (in.read(audio) != -1) {
                sourceDataLine.write(audio, 0, Format.BUFFER_SIZE);
            }
        } catch (IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }
}