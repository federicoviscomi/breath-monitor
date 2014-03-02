package filters;

import java.io.IOException;
import java.io.InputStream;

public class ByteToDoubleInputStream implements DoubleInputStream {

	private final InputStream in;

	public ByteToDoubleInputStream(final InputStream in) {
		this.in = in;

	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public int read(final double b[], final int off, final int len)
			throws IOException {
		final byte[] buffer = new byte[len];
		final int read = in.read(buffer);
		for (int i = 0; i < read; i++) {
			b[i + off] = buffer[i];
		}
		return read;
	}

}
