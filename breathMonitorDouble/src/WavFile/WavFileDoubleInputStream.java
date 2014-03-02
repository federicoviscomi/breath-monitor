package wavFile;

import java.io.IOException;

import filters.DoubleInputStream;

public class WavFileDoubleInputStream implements DoubleInputStream {

	private final WavFile wavFile;

	public WavFileDoubleInputStream(final WavFile wavFile) {
		this.wavFile = wavFile;
	}

	@Override
	public void close() throws IOException {
		wavFile.close();
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		try {
			final int read = wavFile.readFrames(b, off, len);
			// System.err.println("WavFileDoubleInputStream.read(" + off + ", "
			// + len + "):" + read);
			return read;
		} catch (final WavFileException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return 0;
	}

}
