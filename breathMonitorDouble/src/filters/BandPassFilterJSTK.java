package filters;

import java.io.IOException;

import breathMonitor.BreathMonitor;
import de.fau.cs.jstk.sampled.filters.BandPassFilter;

public class BandPassFilterJSTK implements DoubleInputStream {

	final double srate = 44100.f;
	private final BandPassFilter bandPassFilter;

	public BandPassFilterJSTK(final DoubleInputStream in, final double f1,
			final double f2, final int sampleRate, final int fftsize) {
		bandPassFilter = new de.fau.cs.jstk.sampled.filters.BandPassFilter(
				new FISAudioSource(in, sampleRate), f1, f2, fftsize);
		// this.bandPassFilter = new
		// de.fau.cs.jstk.sampled.filters.Butterworth(new FISAudioSource(in,
		// sampleRate), 100, f1, f2, true);
	}

	@Override
	public void close() throws IOException {
		bandPassFilter.tearDown();
	}

	@Override
	public int read(final double[] data, final int off, final int len)
			throws IOException {
		// System.err.println("1- band: b.length:" + data.length + ", off:" +
		// off
		// + ", len:" + len);
		final int read = bandPassFilter.read(data, off, len);
		// System.err.println("2- band: b.length:" + data.length + ", off:" +
		// off
		// + ", len:" + len + ": read " + read);
		if (read <= 0) {
			return -1;
		}
		return read;
	}
}