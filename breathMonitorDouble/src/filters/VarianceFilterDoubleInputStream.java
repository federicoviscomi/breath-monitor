package filters;

import java.io.IOException;

public class VarianceFilterDoubleInputStream extends DoubleFilterInputStream {

	private static final int windowSize = 400;

	private static double variance(final double[] b, final int off,
			final int length) {
		double mean = 0;
		for (int i = off; i < (off + length); i++) {
			b[i] = b[i] + 1;
			mean = mean + b[i];
		}
		mean = mean / length;

		double variance = 0;
		for (int i = off; i < (off + length); i++) {
			variance = variance + ((b[i] - mean) * (b[i] - mean));
		}
		return variance / length;
	}

	private static void windowedVariance(final double[] b, final int off,
			final int len) {
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		final double[] variances = new double[len / windowSize];
		int next = 0;
		for (int i = off; i < (off + len); i = i + windowSize) {
			// median
			final int rest = Math.min((off + len) - i, windowSize);
			// final double median = median(b, i, rest);
			final double v = variance(b, i, rest) + 1;
			if (max < v) {
				max = v;
			}
			if (min > v) {
				min = v;
			}
			variances[next++] = v;
		}
		for (int i = 0; i < next; i++) {
			variances[i] = 10000 * ((variances[i] - min) / max);
			System.err.println(" variance " + variances[i]);
		}

	}

	public VarianceFilterDoubleInputStream(final DoubleInputStream in) {
		super(in);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		windowedVariance(b, off, len);
		return read;
	}
}
