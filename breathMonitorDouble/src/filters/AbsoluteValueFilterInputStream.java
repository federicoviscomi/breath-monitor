package filters;

import java.io.IOException;

public class AbsoluteValueFilterInputStream extends DoubleFilterInputStream {

	private static void abs(final double[] data, final int offset,
			final int length) {
		for (int i = offset; i < (offset + length); i++) {
			data[i] = Math.abs(data[i]);
		}
	}

	public AbsoluteValueFilterInputStream(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] data, final int off, final int len)
			throws IOException {
		final int read = in.read(data, off, len);
		// System.err.println("abs: b.length:" + data.length + ", off:" + off
		// + ", len:" + len + ": read " + read);
		if (read <= 0) {
			return -1;
		}
		abs(data, off, read);
		return read;
	}

}