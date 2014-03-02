package filters;

import java.io.IOException;

public class WindowingFilterInputStream extends DoubleFilterInputStream {

	public WindowingFilterInputStream(final DoubleInputStream inputStream,
			final int bufferSize, final int windowSize) {
		super(inputStream);
		if (bufferSize > windowSize) {
			// TODO
		}
	}

	@Override
	public void close() throws IOException {
		// TODO
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		// TODO
		final int read = in.read(b, off, len);
		// System.err.println("windowing: b.length:" + b.length + ", off:" + off
		// + ", len:" + len + ": read " + read);
		return read;
	}

}
