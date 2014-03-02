package breathMonitor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPInputStream extends InputStream {

	private static final int PACKET_BUFFER_SIZE = 2 << 12;

	DatagramSocket datagramSocket;
	byte[] buffer;
	ByteArrayInputStream byteArrayInputStream;

	public UDPInputStream(final InetAddress address, final int port)
			throws SocketException {
		buffer = new byte[PACKET_BUFFER_SIZE];
		datagramSocket = new DatagramSocket(port, address);
		byteArrayInputStream = new ByteArrayInputStream(buffer);
	}

	@Override
	public int available() throws IOException {
		return byteArrayInputStream.available();
	}

	@Override
	public void close() throws IOException {
		datagramSocket.close();
		byteArrayInputStream.close();

		datagramSocket = null;
		buffer = null;
		byteArrayInputStream = null;
	}

	@Override
	public int read() throws IOException {
		if (byteArrayInputStream.available() > 0) {
			return byteArrayInputStream.read();
		}
		datagramSocket.receive(new DatagramPacket(buffer, PACKET_BUFFER_SIZE));
		return byteArrayInputStream.read();
	}

	@Override
	public long skip(final long len) throws IOException {
		/*
		 * long skipped = 0; do { skipped = skipped +
		 * byteArrayInputStream.skip(len - skipped); if (skipped < len) {
		 * receive(); skipped = skipped + byteArrayInputStream.skip(len -
		 * skipped); } } while (skipped < len); return len - skipped;
		 */
		return byteArrayInputStream.skip(len);
	}

}
