package codecool;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
			SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(Format.format);
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

	public static void main(String[] args) {
		new Client("192.168.1.5", 7575).start();
	}
}
