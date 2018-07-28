package codecool;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;
    byte[] audio = new byte[Format.BUFFER_SIZE];

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        new ServerThread(this, "read").start();
        new ServerThread(this, "play").start();
    }

    public synchronized void getSound() {
        while (true) {
            InputStream is;
            try {
                Socket socket = serverSocket.accept();
                notify();
                is = socket.getInputStream();
                audio = new byte[Format.BUFFER_SIZE];
                is.read(audio, 0,audio.length);
                wait();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playSound () {
        AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(audio), Format.format, audio.length);
        SourceDataLine sourceLine = null;
        notify();
        try {
            sourceLine = AudioSystem.getSourceDataLine(Format.format);
            sourceLine.open(Format.format);
            sourceLine.start();
            wait();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int nBytesRead = 1;
        byte[] abData = new byte[Format.BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) sourceLine.write(abData, 0, nBytesRead);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

}
