package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MagnitudeInputStream extends FilterInputStream {

	private static void magnitude(final byte[] data, final int offset,
			final int length) {
		for (int i = offset; i < (offset + length); i++) {
			if (data[i] < 0) {
				if (data[i] == -128) {
					data[i] = 0;
				} else {
					data[i] = (byte) (data[i] + 127);
				}
			} else {
				data[i] = (byte) -(data[i] - 127);
			}
		}
	}

	public MagnitudeInputStream(final InputStream in) {
		super(in);
	}

	@Override
	public int read() throws IOException {
		return Math.abs(in.read());
	}

	@Override
	public int read(final byte[] data) throws IOException {
		return this.read(data, 0, data.length);
	}

	@Override
	public int read(final byte[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		magnitude(data, offset, read);
		return read;
	}

}
