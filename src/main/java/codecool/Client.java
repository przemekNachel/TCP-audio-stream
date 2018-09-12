package codecool;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Client {

	private String host;
	private int port;

	private Client(String host, int port) {
		this.host = host;
		this.port = port;
	}

	private void start() {
		byte tempBuffer[] = new byte[Format.BUFFER_SIZE];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			Socket socket = new Socket(host, port);
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, Format.format);
			TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
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

	public static void main(String[] args) {
		new Client("192.168.1.2", 7575).start();
	}
}
