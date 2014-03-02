package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AbsoluteValueInputStream extends FilterInputStream {

	private static void abs(final byte[] data, final int offset,
			final int length) {
		for (int i = offset; i < (offset + length); i++) {
			if (data[i] == Byte.MIN_VALUE) {
				data[i] = Byte.MAX_VALUE;
			} else if (data[i] < 0) {
				data[i] = (byte) -data[i];
			}
		}
	}

	public AbsoluteValueInputStream(final InputStream in) {
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
		abs(data, offset, read);
		return read;
	}

}
