package codecool;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import javax.sound.sampled.*;

public class Client {

	private byte audio[] = new byte[Format.BUFFER_SIZE];
	private String host;
	private int port;

	private Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private void start() {
		try {
			SourceDataLine sourceDataLine = getSourceDataLine();
			sourceDataLine.open(Format.format);
			sourceDataLine.start();
			Socket socket = new Socket(host, port);
			InputStream in = new BufferedInputStream(socket.getInputStream());
			while (in.read(audio) != -1) {
				sourceDataLine.write(audio, 0, Format.BUFFER_SIZE);
			}
		} catch (IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private SourceDataLine getSourceDataLine() throws LineUnavailableException {
		int positon = 1;
		for (Mixer.Info info : AudioSystem.getMixerInfo()) {
			Line.Info[] linesInfo = AudioSystem.getMixer(info).getSourceLineInfo();
			for (Line.Info lineInfo : linesInfo) {
				Line line = AudioSystem.getLine(lineInfo);
				if (line instanceof SourceDataLine) System.out.println(positon++ + ". " + info.getName());
			}
		}
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		int choice = 0;
		if (input.matches("\\d+") && Integer.parseInt(input) < positon) choice = Integer.parseInt(input) - 1;
		return (SourceDataLine) AudioSystem.getMixer(AudioSystem.getMixerInfo()[choice]).getLine(new DataLine.Info(SourceDataLine.class, Format.format));
	}

	public static void main(String[] args) {
		new Client("192.168.1.5", 7575).start();
	}
}
