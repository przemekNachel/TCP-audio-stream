package codecool.structures;


import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ReadMic {

    public static AudioFormat format = new AudioFormat(44100, 8, 1, true, false);
    public static int BUFFER_SIZE = 12800;


    public static void main(String[] args) {

    }

    public static byte[] getAudio() {
        byte[] data = new byte[BUFFER_SIZE];
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        try {
            TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();
            line.read(data, 0, data.length);
            line.close();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        return data;
    }



}
