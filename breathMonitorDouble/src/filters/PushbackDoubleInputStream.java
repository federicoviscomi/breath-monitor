package filters;

import java.io.IOException;

/**
 * A <code>PushbackDoubleInputStream</code> adds functionality to another input
 * stream, namely the ability to "push back" or "unread" one double. This is
 * useful in situations where it is convenient for a fragment of code to read an
 * indefinite number of data doubles that are delimited by a particular double
 * value; after reading the terminating double, the code fragment can "unread"
 * it, so that the next read operation on the input stream will reread the
 * double that was pushed back. For example, doubles representing the characters
 * constituting an identifier might be terminated by a double representing an
 * operator character; a method whose job is to read just an identifier can read
 * until it sees the operator and then push the operator back to be re-read.
 * 
 * @author David Connelly
 * @author Jonathan Payne
 * @since JDK1.0
 */
public class PushbackDoubleInputStream implements DoubleInputStream {

	/**
	 * The pushback buffer.
	 * 
	 * @since JDK1.1
	 */
	protected double[] buf;

	/**
	 * The position within the pushback buffer from which the next double will
	 * be read. When the buffer is empty, <code>pos</code> is equal to
	 * <code>buf.length</code>; when the buffer is full, <code>pos</code> is
	 * equal to zero.
	 * 
	 * @since JDK1.1
	 */
	protected int pos;

	private DoubleInputStream in;

	/**
	 * Creates a <code>PushbackDoubleInputStream</code> and saves its argument,
	 * the input stream <code>in</code>, for later use. Initially, there is no
	 * pushed-back double (the field <code>pushBack</code> is initialized to
	 * <code>-1</code>).
	 * 
	 * @param in
	 *            the input stream from which doubles will be read.
	 */
	public PushbackDoubleInputStream(final DoubleInputStream in) {
		this(in, 1);
	}

	/**
	 * Creates a <code>PushbackDoubleInputStream</code> with a pushback buffer
	 * of the specified <code>size</code>, and saves its argument, the input
	 * stream <code>in</code>, for later use. Initially, there is no pushed-back
	 * double (the field <code>pushBack</code> is initialized to <code>-1</code>
	 * ).
	 * 
	 * @param in
	 *            the input stream from which doubles will be read.
	 * @param size
	 *            the size of the pushback buffer.
	 * @exception IllegalArgumentException
	 *                if size is <= 0
	 * @since JDK1.1
	 */
	public PushbackDoubleInputStream(final DoubleInputStream in, final int size) {
		this.in = in;
		if (size <= 0) {
			throw new IllegalArgumentException("size <= 0");
		}
		buf = new double[size];
		pos = size;
	}

	/**
	 * Closes this input stream and releases any system resources associated
	 * with the stream. Once the stream has been closed, further read(),
	 * unread(), available(), reset(), or skip() invocations will throw an
	 * IOException. Closing a previously closed stream has no effect.
	 * 
	 * @exception IOException
	 *                if an I/O error occurs.
	 */
	@Override
	public synchronized void close() throws IOException {
		if (in == null) {
			return;
		}
		in.close();
		in = null;
		buf = null;
	}

	/**
	 * Check to make sure that this stream has not been closed
	 */
	private void ensureOpen() throws IOException {
		if (in == null) {
			throw new IOException("Stream closed");
		}
	}

	/**
	 * Reads up to <code>len</code> doubles of data from this input stream into
	 * an array of doubles. This method first reads any pushed-back doubles;
	 * after that, if fewer than <code>len</code> doubles have been read then it
	 * reads from the underlying input stream. If <code>len</code> is not zero,
	 * the method blocks until at least 1 double of input is available;
	 * otherwise, no doubles are read and <code>0</code> is returned.
	 * 
	 * @param b
	 *            the buffer into which the data is read.
	 * @param off
	 *            the start offset in the destination array <code>b</code>
	 * @param len
	 *            the maximum number of doubles read.
	 * @return the total number of doubles read into the buffer, or
	 *         <code>-1</code> if there is no more data because the end of the
	 *         stream has been reached.
	 * @exception NullPointerException
	 *                If <code>b</code> is <code>null</code>.
	 * @exception IndexOutOfBoundsException
	 *                If <code>off</code> is negative, <code>len</code> is
	 *                negative, or <code>len</code> is greater than
	 *                <code>b.length - off</code>
	 * @exception IOException
	 *                if this input stream has been closed by invoking its
	 *                {@link #close()} method, or an I/O error occurs.
	 * @see java.io.DoubleInputStream#read(double[], int, int)
	 */
	@Override
	public int read(final double[] b, int off, int len) throws IOException {
		ensureOpen();
		if (b == null) {
			throw new NullPointerException();
		} else if ((off < 0) || (len < 0) || (len > (b.length - off))) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int avail = buf.length - pos;
		if (avail > 0) {
			if (len < avail) {
				avail = len;
			}
			System.arraycopy(buf, pos, b, off, avail);
			pos += avail;
			off += avail;
			len -= avail;
		}
		if (len > 0) {
			len = in.read(b, off, len);
			if (len == -1) {
				return avail == 0 ? -1 : avail;
			}
			return avail + len;
		}
		return avail;
	}

	/**
	 * Pushes back a portion of an array of doubles by copying it to the front
	 * of the pushback buffer. After this method returns, the next double to be
	 * read will have the value <code>b[off]</code>, the double after that will
	 * have the value <code>b[off+1]</code>, and so forth.
	 * 
	 * @param b
	 *            the double array to push back.
	 * @param off
	 *            the start offset of the data.
	 * @param len
	 *            the number of doubles to push back.
	 * @exception IOException
	 *                If there is not enough room in the pushback buffer for the
	 *                specified number of doubles, or this input stream has been
	 *                closed by invoking its {@link #close()} method.
	 * @since JDK1.1
	 */
	public void unread(final double[] b, final int off, final int len)
			throws IOException {
		ensureOpen();
		if (len > pos) {
			throw new IOException("Push back buffer is full");
		}
		pos -= len;
		System.arraycopy(b, off, buf, pos, len);
	}
}
