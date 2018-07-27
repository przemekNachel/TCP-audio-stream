package codecool;

import codecool.structures.ReadMic;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.ByteArrayInputStream;
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
                byte[] buf = new byte[ReadMic.BUFFER_SIZE];
                is.read(buf, 0, buf.length);
                playSound(buf);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void playSound (byte[] audio) throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(audio);
        AudioInputStream audioStream = new AudioInputStream(bis, ReadMic.format, audio.length);
        SourceDataLine sourceLine = AudioSystem.getSourceDataLine(ReadMic.format);

        sourceLine.open(ReadMic.format);
        sourceLine.start();

        int nBytesRead = 1;
        byte[] abData = new byte[ReadMic.BUFFER_SIZE];
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
