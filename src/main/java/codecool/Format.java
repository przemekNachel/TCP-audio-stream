package codecool;

import javax.sound.sampled.*;

public class Format {

    public static AudioFormat format = new AudioFormat(44100, 8, 1, true, false);
    public static int BUFFER_SIZE = 4096;
}
