package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownsampleFilterInputStream extends FilterInputStream {

	private static int downSample(final byte[] data, final int offset,
			final int length, final int sampleRate) {
		int read = offset;
		for (int i = offset; i < (offset + length); i++) {
			if (((i - offset) % sampleRate) == 0) {
				data[read] = data[i];
				read++;
			}
		}
		return read - offset;
	}

	private final int sampleFrequency;

	public DownsampleFilterInputStream(final InputStream in,
			final int sampleFrequency) {
		super(in);
		this.sampleFrequency = sampleFrequency;
	}

	@Override
	public int read() throws IOException {
		return in.read();
	}

	@Override
	public int read(final byte[] data) throws IOException {
		return this.read(data, 0, data.length);
	}

	@Override
	public int read(final byte[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		downSample(data, offset, read, sampleFrequency);
		return read;
	}

}
