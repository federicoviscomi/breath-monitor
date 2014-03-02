package stethoscopeInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import filters.ByteToDoubleInputStream;

public class InputRecordedOnFileStethoscopeInterface implements
		StethoscopeInterface {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub

	}

	private ByteToDoubleInputStream doubleInputStream;

	private final String fileName;

	public InputRecordedOnFileStethoscopeInterface(final String fileName) {
		this.fileName = fileName;
		doubleInputStream = null;
	}

	@Override
	public void close() {
		if (doubleInputStream != null) {
			try {
				doubleInputStream.close();
			} catch (final IOException e) {
				// do nothing
			}
		}
	}

	@Override
	public void connect() throws IOException {
		if (doubleInputStream != null) {
			throw new IOException("already connected");
		}
		doubleInputStream = new ByteToDoubleInputStream(new FileInputStream(
				new File(fileName)));
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		return doubleInputStream.read(b, off, len);
	}

	@Override
	public int skip(final int toSkip) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
