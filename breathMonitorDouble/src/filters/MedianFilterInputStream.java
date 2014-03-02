package filters;

import java.io.IOException;

public class MedianFilterInputStream extends DoubleFilterInputStream {

	private final int windowSize;

	private final double[] window;

	public MedianFilterInputStream(final DoubleInputStream in,
			final int windowSize) {
		super(in);
		this.windowSize = windowSize;
		window = new double[windowSize];
	}

	// sort a copy of the array, and return the median
	private double median(final double[] b, final int off, final int length) {
		// System.err.println("median. b.length:" + b.length + ", off:" + off
		// + ", window.length:" + window.length + ", length:" + length);
		System.arraycopy(b, off, window, 0, length);
		java.util.Arrays.sort(window, 0, length);
		if (((length % 2) == 1) || (length <= 1)) {
			return window[length / 2];
		}
		return ((window[length / 2] + window[(length / 2) - 1]) / 2);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		if ((b.length < (off + len)) || (off < 0) || (len < 0)) {
			throw new IllegalArgumentException();
		}
		final int read = in.read(b, off, len);
		// System.err
		// .println("read median: b.length:" + b.length + ", off:" + off
		// + ", len:" + len + ", windows:" + windowSize
		// + ": read " + read);
		windowedMedianFilter(b, off, read);
		return read;
	}

	public int read2(final double[] b, final int off, final int len)
			throws IOException {
		if ((b.length < (off + len)) || (off < 0) || (len < 0)) {
			throw new IllegalArgumentException();
		}
		// System.err.println("read median: b.length:" + b.length + ", off:" +
		// off
		// + ", len:" + len + ", windows:" + windowSize);
		int totalRead = 0, read = 1;
		for (int i = off; (i < (off + len)) && (read > 0); i = i + windowSize) {
			// System.err.println("******* " + (len - i));
			read = readAWindow(b, i, len - i);
			if (read > 0) {
				totalRead = totalRead + read;
			} else if (totalRead == 0) {
				// System.err.println("BandPassFilterJSTK.read(" + off + ", " +
				// len + "):"
				// + read + ", " + totalRead);
				return -1;
			}
		}
		// System.err.println("BandPassFilterJSTK.read(" + off + ", " + len +
		// "):"
		// + read + ", " + totalRead);
		return totalRead;
	}

	private int readAWindow(final double[] data, final int off, final int len)
			throws IOException {
		// System.err.println("read a window median: b.length:" + data.length +
		// ", off:" + off
		// + ", len:" + len + ", windows:" + windowSize);

		final int read = in.read(data, off, Math.min(windowSize, len));
		if (read <= 0) {
			return -1;
		}
		final double medianValue = median(data, off, read);
		for (int i = off; i < (off + read); i++) {
			if (data[i] > (4 * medianValue)) {
				data[i] = medianValue;
			} else if(data[i] < (0.3 * medianValue)){
				data[i] = medianValue;
			}
		}
		return read;
	}

	private void windowedMedianFilter(final double[] b, final int off,
			final int len) {
		for (int i = off; i < (off + len); i = i + windowSize) {
			// median
			final int rest = Math.min((off + len) - i, windowSize);
			final double median = median(b, i, rest);
			for (int j = i; j < rest; j++) {
				if (b[i] > (5 * median)) {
					b[i] = median;
				}
			}
		}
	}

}
