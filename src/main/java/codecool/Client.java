package codecool;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private String address;
    private int port;
    byte[] audio = new byte[Format.BUFFER_SIZE];


    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void sendSound() {
        while (true) try {
            Socket socket = new Socket(address, port);
            OutputStream os = socket.getOutputStream();
            os.write(audio);
            os.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getSound() {
        while (true) {

            byte[] data = new byte[Format.BUFFER_SIZE];
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, Format.format);
            try {
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(Format.format);
                line.start();
                line.read(data, 0, data.length);
                line.close();
            } catch (LineUnavailableException ex) {
                ex.printStackTrace();
            }



            System.arraycopy(data, 0, audio, 0, audio.length);
        }
    }

    public static void main(String[] args) {
        new Client("192.168.1.2", 7575).start();
    }

    private void start() {
        new Thread(() -> getSound()).start();
        new Thread(() -> sendSound()).start();
    }
}
