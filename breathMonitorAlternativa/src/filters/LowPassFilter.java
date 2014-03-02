package filters;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import ddf.minim.effects.LowPassFS;

public class LowPassFilter extends FilterInputStream {

	private LowPassFS lowPassFilter;

	public LowPassFilter(final InputStream inputStream,
			final int cutoffFrequency, float sampleRate) {
		super(inputStream);
		lowPassFilter = new LowPassFS(cutoffFrequency, sampleRate);
	}

	public int read(byte b[], int off, int len) throws IOException {
		int read = in.read(b, off, len);
		
		return read;
	}

	public int read(byte b[]) throws IOException {
		return in.read(b, 0, b.length);
	}
	
	public int read() throws IOException {
		throw new UnsupportedOperationException();
	}
}
