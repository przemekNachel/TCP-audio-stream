package codecool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
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
            TargetDataLine targetDataLine = getTargetDataLine();
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
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

    private TargetDataLine getTargetDataLine() throws LineUnavailableException {
        int positon = 1;
        for (Mixer.Info info : AudioSystem.getMixerInfo()) {
            Line.Info[] linesInfo = AudioSystem.getMixer(info).getTargetLineInfo();
            for (Line.Info lineInfo : linesInfo) {
                Line line = AudioSystem.getLine(lineInfo);
                if (line instanceof TargetDataLine) System.out.println(positon++ + ". " + info.getName());
            }
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int choice = 0;
        if (input.matches("\\d+") && Integer.parseInt(input) < positon) choice = Integer.parseInt(input) - 1;
        return (TargetDataLine) AudioSystem.getMixer(AudioSystem.getMixerInfo()[choice]).getLine(new DataLine.Info(TargetDataLine.class, Format.format));
    }

    public static void main(String[] args) {
        new Server(7575).start();
    }
}