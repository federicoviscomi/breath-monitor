package filters;

import java.io.IOException;

import de.fau.cs.jstk.sampled.AudioSource;

public class FISAudioSource implements AudioSource {

	private final DoubleInputStream in;
	private final int sampleRate;

	public FISAudioSource(final DoubleInputStream in, final int sampleRate) {
		this.in = in;
		this.sampleRate = sampleRate;
	}

	@Override
	public boolean getPreEmphasis() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getSampleRate() {
		return sampleRate;
	}

	@Override
	public int read(final double[] buf) throws IOException {
		return in.read(buf, 0, buf.length);
	}

	@Override
	public int read(final double[] buf, final int length) throws IOException {
		return in.read(buf, 0, length);
	}

	@Override
	public void setPreEmphasis(final boolean applyPreEmphasis, final double a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void tearDown() throws IOException {
		in.close();
	}

}
