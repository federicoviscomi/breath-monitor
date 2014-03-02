package filters;

import java.io.IOException;

public class SimpleGainFilterInputStream extends DoubleFilterInputStream {

	private final int gain;

	public SimpleGainFilterInputStream(final DoubleInputStream in,
			final int gain) {
		super(in);
		this.gain = gain;
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		for (int i = off; i < (off + len); i++) {
			b[i] = gain * b[i];
		}
		return read;
	}

}
