package codecool.kto;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Mic
{
    public byte[] buffer;
    private int port;
    static AudioInputStream ais;

    public static void main(String[] args)
    {
        TargetDataLine line;
        DatagramPacket dgp;

        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int channels = 2;
        int bitDepth = 16;
        boolean bigEndian = true;
        InetAddress addr;


        AudioFormat format = new AudioFormat(encoding, rate, bitDepth, channels, (bitDepth / 8) * channels, rate, bigEndian);

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return;
        }

        try
        {
            line = (TargetDataLine) AudioSystem.getLine(info);

            int buffsize = line.getBufferSize()/5;
            buffsize += 512;

            line.open(format);

            line.start();

            int numBytesRead;
            byte[] data = new byte[buffsize];

            addr = InetAddress.getByName("127.0.0.1");
            DatagramSocket socket = new DatagramSocket();
            while (true) {
                // Read the next chunk of data from the TargetDataLine.
                numBytesRead =  line.read(data, 0, data.length);
                // Save this chunk of data.
                dgp = new DatagramPacket (data,data.length,addr,50005);

                socket.send(dgp);
            }

        }catch (LineUnavailableException e) {
            e.printStackTrace();
        }catch (UnknownHostException e) {
            // TODO: handle exception
        } catch (SocketException e) {
            // TODO: handle exception
        } catch (IOException e2) {
            // TODO: handle exception
        }
    }
}