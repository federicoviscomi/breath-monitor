package filters;

import java.io.IOException;

public class TruncateToBufferMultipleFilterInputStream extends
		DoubleFilterInputStream {

	private final int length;

	public TruncateToBufferMultipleFilterInputStream(
			final DoubleInputStream inputStream, final int length) {
		super(inputStream);
		this.length = length;
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		if (read != length) {
			return -1;
		}
		return read;
	}

}
