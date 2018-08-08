package codecool;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.io.BufferedInputStream;
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
        new Thread(() -> getSound()).start();
    }

    public void getSound() {
        while (true) {
            InputStream ins;
            try {
                Socket socket = serverSocket.accept();
                ins = socket.getInputStream();
                BufferedInputStream is = new BufferedInputStream(ins);

                audio = new byte[Format.BUFFER_SIZE];
                is.read(audio, 0,audio.length);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            AudioInputStream audioStream = new AudioInputStream(new ByteArrayInputStream(audio), Format.format, audio.length);
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
