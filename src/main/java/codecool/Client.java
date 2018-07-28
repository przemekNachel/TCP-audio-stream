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
    byte[] audio;


    public Client(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public synchronized void sendSound() {
        while (true) {
            try {
                Socket socket = new Socket(address, port);
                OutputStream os = socket.getOutputStream();
                wait();
                os.write(audio);
                os.close();
                notify();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void getSound() {
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
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            audio = data;
            notify();
        }
    }

    public static void main(String[] args) {
        new Client("192.168.1.2", 7575).start();
    }

    private void start() {
        new ClientThread(this, "get").start();
        new ClientThread(this, "send").start();
    }
}
