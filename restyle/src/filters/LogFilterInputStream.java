package filters;

import java.io.IOException;

public class LogFilterInputStream extends DoubleFilterInputStream {

	public LogFilterInputStream(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		for (int i = off; i < (off + len); i++) {
			if (b[i] < 0) {
				throw new IllegalArgumentException();
			}
			if (b[i] < 1) {
				b[i] = b[i] + 1;
			}
			b[i] = Math.log(b[i]);
		}
		return read;
	}

}
