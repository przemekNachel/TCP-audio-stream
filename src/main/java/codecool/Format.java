package codecool;

import javax.sound.sampled.*;

public class Format {

    public static AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
    public static int BUFFER_SIZE = 128;
}
