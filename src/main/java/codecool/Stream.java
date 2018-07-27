package codecool;

import codecool.structures.MyQueue;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Stream {

    private int bitDepth = 16;
    private int sampleRate = 44100;


    MyQueue<Integer> sound = new MyQueue<>();

    DataOutputStream dataOutputStream = new DataOutputStream(new Socket("localhost", 959).getOutputStream());

    public Stream() throws IOException {
        while(true) {
            dataOutputStream.writeInt(sound.dequeue());
        }
    }
}
