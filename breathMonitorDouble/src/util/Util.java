package util;

public class Util {
	public static double[] linspace(final int length, final double start,
			final double increment) {
		final double[] data = new double[length];
		double value = start;
		for (int i = 0; i < data.length; i++) {
			data[i] = value;
			value = value + increment;
		}
		// System.err.println(data[0]);
		return data;
	}

	public static double[] trim(final double[] data, final int offset,
			final int read) {
		if ((offset == 0) && (read == data.length)) {
			return data;
		}
		final double[] trimmedData = new double[read];
		System.arraycopy(data, offset, trimmedData, 0, read);
		return trimmedData;
	}

}
