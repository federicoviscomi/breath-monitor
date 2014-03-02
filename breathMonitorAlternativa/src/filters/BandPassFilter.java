package filters;

import java.io.FilterInputStream;
import java.io.InputStream;

public class BandPassFilter extends FilterInputStream {

	final float srate = 44100.f;

	public BandPassFilter(final InputStream in, final float f1, final float f2) {
		super(in);
		// this.bandPassFilter = new
		// de.fau.cs.jstk.sampled.filters.BandPassFilter(in, f1, f1, 2 << 4);
		// bandPass = new BandPass(frequency, bandWidth, sampleRate);

	}

	/*
	 * public int read(final byte[] data, final int offset, final int length)
	 * throws IOException { final int read = in.read(data, offset, length);
	 * float[] filtered = new float[read]; bandPassFilter.filter(filtered,
	 * byteArrayToFloatArray(data, offset, read), read, offset);
	 * floatArrayToByteArrayCopy(filtered, offset, data, offset, read); return
	 * read; }
	 */
}
