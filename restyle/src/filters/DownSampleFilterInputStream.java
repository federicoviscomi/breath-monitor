package filters;

import java.io.IOException;

public class DownSampleFilterInputStream extends DoubleFilterInputStream {

	private final int currentSampleRate;
	private final int desiredSampleRate;

	public DownSampleFilterInputStream(final DoubleInputStream in,
			final int currentSampleRate, final int desiredSampleRate) {
		super(in);
		this.currentSampleRate = currentSampleRate;
		this.desiredSampleRate = desiredSampleRate;
	}

	@Override
	public int read(final double[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		// System.err.println(" csr " + currentSampleRate + ", dsr "
		// + desiredSampleRate + " csr/dsr "
		// + (currentSampleRate / desiredSampleRate));
		int j = 0;
		for (int i = 1; i < read; i++) {
			if ((i % (currentSampleRate / desiredSampleRate)) == 0) {
				// if ((i % read) != 0) {
				// data[j++] = data[i];
				data[i] = 0;
			}
		}
		return read;
	}

}
