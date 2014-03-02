package filters;

import java.io.FileNotFoundException;
import java.io.IOException;

import plot.FileSequencePlotter;

public class AdaptiveSequenzialMagnitudeClusteringFilter extends
		DoubleFilterInputStream {

	private static final int MAX_ALLOWED_CLUSTERS = 20;

	private final static double abs(final double a) {
		return (a <= 0.0D) ? 0.0D - a : a;
	}

	private FileSequencePlotter plotter;

	public AdaptiveSequenzialMagnitudeClusteringFilter(
			final DoubleInputStream inputStream, final double samplingRate) {
		super(inputStream);
		try {
			plotter = new FileSequencePlotter("asssssdfdfdf");
		} catch (final FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// plotter = new DiscreteTimePlotter("");
	}

	private final int BSAS(final double threshold, final double[] b,
			final int off, final int len, final boolean plot) {

		int clusterCount = 0;
		int clusterStart = off;

		// la distanza dalla media

		DistanceCluster currentcluster = new MinxMaxyDistanceCluster(off,
				b[off]);
		// DistanceCluster currentcluster = new
		// AverageEuclideanDistanceCluster(off, b[off]);
		for (int i = off; (i < (off + len))
				&& (clusterCount < (MAX_ALLOWED_CLUSTERS - 1)); i++) {
			// double vectorDistance = vectorDistance(i,
			// b[i],averageClusterValueX, averageClusterValueY);
			// double vectorDistance = vectorDistance(i, b[i], currentcluster);
			final double vectorDistance = currentcluster.getDistance(i, b[i]);
			// System.err.println(" distance " + vectorDistance);
			if (vectorDistance < threshold) {
				currentcluster.add(i, b[i]);
			} else {
				clusterCount++;
				if ((clusterStart < (off + len)) && plot) {
					plotter.plotPoints(b, clusterStart, i - clusterStart);
				}
				clusterStart = i;
				currentcluster = new MinxMaxyDistanceCluster(i, b[i]);
			}
		}
		if ((clusterStart < (off + len)) && plot) {
			plotter.plotPoints(b, clusterStart, (off + len) - clusterStart);
		}
		return clusterCount;
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {

		// read data
		final int read = in.read(b, off, len);
		if (read == -1) {
			plotter.shutDown();
			return -1;
		}
		// find maximum and minimum of absolute values of the data
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;
		for (int i = off; i < (off + read); i++) {
			b[i] = abs(b[i]) * read;
			if (b[i] < min) {
				min = b[i];
			} else if (b[i] > max) {
				max = b[i];
			}
		}

		// for (int i = off; i < off + read; i++) {
		// b[i] = (b[i] - min) * read;
		// }
		// max = max - min;
		// min = 0;

		// for (int i = off; i < off + read; i++) {
		// b[i] = b[i] * read;
		// }

		// estimate clusters number by trying a fixed amount of values for the
		// threshold from min to max
		// double vv = vectorDistance(read / sampleRate, max, 0, min);
		// HashMap<Integer, Integer> clustersNumber = new HashMap<Integer,
		// Integer>(SIZE);
		final int[] clustersNumber = new int[MAX_ALLOWED_CLUSTERS];
		int maxValue = 0;
		java.util.Arrays.fill(clustersNumber, 0);
		double maxArgThreshold = 0;

		for (double threshold = min; threshold < max; threshold = threshold
				+ ((max - min) / MAX_ALLOWED_CLUSTERS)) {
			final int bsas = BSAS(threshold, b, off, read, false);
			// System.err.println("min=" + min + ", max=" + max + ", threshold="
			// + threshold + ", bsas=" + bsas);
			if (bsas != (clustersNumber.length - 1)) {
				if ((++clustersNumber[bsas] > maxValue)
						&& (bsas != (clustersNumber.length - 1))) {
					maxValue = clustersNumber[bsas];
					maxArgThreshold = threshold;
				}
			}
		}
		System.err.println(java.util.Arrays.toString(clustersNumber));
		// for (int i = 0; i < clustersNumber.length; i++) {
		// if (clustersNumber[i] > maxValue) {
		// maxValue = clustersNumber[i];
		// maxArg = i;
		// }
		// }
		// System.err.println(maxArg);
		BSAS(maxArgThreshold, b, off, read, true);
		return read;
	}
}
