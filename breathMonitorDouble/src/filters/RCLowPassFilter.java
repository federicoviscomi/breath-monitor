package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RCLowPassFilter extends FilterInputStream {

	private static final double ALPHA = 0.2f;
	private static final int WINDOWS_SIZE = 30;

	public RCLowPassFilter(final InputStream in) {
		super(in);
	}

	public void filter(final byte[] data, final int offset, final int length) {
		// Return RC low-pass filter output samples, given input samples,
		// time interval dt, and time constant RC
		for (int i = 1; i < data.length; i++) {
			if ((i % WINDOWS_SIZE) != 0) {
				data[i] = (byte) ((ALPHA * data[i]) + ((1 - ALPHA) * data[i - 1]));
			}
		}
	}

	@Override
	public int read() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int read(final byte[] data) throws IOException {
		return this.read(data, 0, data.length);
	}

	@Override
	public int read(final byte[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		filter(data, offset, length);
		return read;
	}

}