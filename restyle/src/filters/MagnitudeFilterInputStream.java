package filters;

import java.io.IOException;

public class MagnitudeFilterInputStream extends DoubleFilterInputStream {

	private static void abs(final double[] data, final int offset,
			final int length) {
		for (int i = offset; i < (offset + length); i++) {
			if (data[i] < 0) {
				if (data[i] == -128) {
					data[i] = -127;
				}
				data[i] = data[i] + Byte.MAX_VALUE;
			} else {
				data[i] = data[i] - Byte.MAX_VALUE;
			}
		}
	}

	public MagnitudeFilterInputStream(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] data, final int offset, final int length)
			throws IOException {
		final int read = in.read(data, offset, length);
		abs(data, offset, read);
		return read;
	}

}
