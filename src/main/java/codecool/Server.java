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
    byte[] buf = new byte[Format.BUFFER_SIZE];

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start() {
        new Thread(() -> getSound()).start();
        new Thread(() -> playSound()).start();
    }

    public synchronized void getSound() {
        while (true) {
            InputStream is;
            try {
                Socket socket = serverSocket.accept();
                is = socket.getInputStream();
                System.arraycopy(audio, 0, buf, 0, audio.length);
                notify();
                wait();
                audio = new byte[Format.BUFFER_SIZE];
                is.read(audio, 0,audio.length);
                is.close();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void playSound () {
        while (true) {
            notify();
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(buf), Format.format, buf.length);
            SourceDataLine sourceLine = null;
            try {
                sourceLine = AudioSystem.getSourceDataLine(Format.format);
                sourceLine.open(Format.format);
                sourceLine.start();
            } catch (LineUnavailableException e) {
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
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }

}
