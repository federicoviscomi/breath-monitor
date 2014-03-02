package filters;

import java.io.IOException;

public class MaxFilterInputStream extends DoubleFilterInputStream {

	public MaxFilterInputStream(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		final int size = 100;
		for (int i = off; i < (off + read); i = i + size) {
			// double max = getMax(b, i, Math.min(size, read - i));
			final double max = Util.getInstantSoundEnergy(b, i,
					Math.min(size, read - i));
			for (int j = i; j < (i + Math.min(size, read - i)); j++) {
				b[j] = max;
			}

		}
		return read;
	}
}
