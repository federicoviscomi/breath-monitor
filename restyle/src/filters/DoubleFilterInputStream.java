package filters;

import java.io.IOException;

/**
 * A <code>FilterInputStream</code> contains some other input stream, which it
 * uses as its basic source of data, possibly transforming the data along the
 * way or providing additional functionality.
 * 
 */
public abstract class DoubleFilterInputStream implements DoubleInputStream {

	/** The input stream to be filtered. */
	protected DoubleInputStream in;
	private final LengthPreservingFilterFunction lengthPreservingFilterFunction;

	/**
	 * Creates a <code>FilterInputStream</code> by assigning the argument
	 * <code>in</code> to the field <code>this.in</code> so as to remember it
	 * for later use.
	 * 
	 * @param in
	 *            the underlying input stream, or <code>null</code> if this
	 *            instance is to be created without an underlying stream.
	 */
	public DoubleFilterInputStream(final DoubleInputStream in) {
		this.in = in;
		lengthPreservingFilterFunction = null;
	}

	protected DoubleFilterInputStream(final DoubleInputStream in,
			final LengthPreservingFilterFunction lengthPreservingFilterFunction) {
		this.in = in;
		this.lengthPreservingFilterFunction = lengthPreservingFilterFunction;
	}

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream. This method simply performs <code>in.close()</code>.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public int read(final double b[], final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);
		lengthPreservingFilterFunction.lengthPreservingFilter(b, off, read);
		return read;
	}
}
