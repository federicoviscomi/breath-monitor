package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FiniteImpulseResponseBandapassFilterInputStream extends
		FilterInputStream {

	private static int filter(final int read) {
		return read;
	}

	public FiniteImpulseResponseBandapassFilterInputStream(
			final InputStream inputStream, final int lowerFrequency,
			final int higherFrequency) {
		super(inputStream);
	}

	private void filter(final byte[] data, final int offset, final int read) {
		// TODO Auto-generated method stub

	}

	@Override
	public int read() throws IOException {
		return filter(in.read());
	}

	@Override
	public int read(final byte[] data) throws IOException {
		return this.read(data, 0, data.length);
	}

	@Override
	public int read(final byte[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		filter(data, offset, read);
		return read;
	}

}
