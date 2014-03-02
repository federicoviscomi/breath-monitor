package filters;

import java.io.IOException;

public class DensityLikeDistanceFilter extends DoubleFilterInputStream {

	public DensityLikeDistanceFilter(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		for (int i = off; i < (off + read); i++) {
			// b[i] = distance(i, b[i]);
		}
		return read;
	}

}
