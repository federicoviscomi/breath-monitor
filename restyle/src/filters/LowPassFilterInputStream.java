package filters;

import java.io.IOException;

import ddf.minim.effects.LowPassFS;

public class LowPassFilterInputStream extends DoubleFilterInputStream {

	private final LowPassFS lowPassFilter;

	public LowPassFilterInputStream(final DoubleInputStream floatInputStream,
			final int cutoffFrequency, final float sampleRate) {
		super(floatInputStream);
		lowPassFilter = new LowPassFS(cutoffFrequency, sampleRate);
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		final float[] f = new float[len];
		lowPassFilter.process(f);
		for (int i = 0; i < len; i++) {
			f[i] = (float) b[i + off];
		}
		return read;
	}

}
