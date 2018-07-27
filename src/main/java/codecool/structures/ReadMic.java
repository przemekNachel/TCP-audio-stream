package codecool.structures;


import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ReadMic {

    public static AudioFormat format = new AudioFormat(44100, 8, 1, true, false);
    public static int BUFFER_SIZE = 4096;


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


    public static void playSound (byte[] audio) throws Exception {

        ByteArrayInputStream bis = new ByteArrayInputStream(audio);
        AudioInputStream audioStream = new AudioInputStream(bis, ReadMic.format, audio.length);
        SourceDataLine sourceLine = AudioSystem.getSourceDataLine(format);

        sourceLine.open(format);
        sourceLine.start();

        int nBytesRead = 1;
        byte[] abData = new byte[BUFFER_SIZE];
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
