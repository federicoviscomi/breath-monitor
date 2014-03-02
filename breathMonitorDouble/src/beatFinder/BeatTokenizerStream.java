package beatFinder;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.sound.sampled.AudioFormat;

/**
 * This is:
 * <ul>
 * <li>a byte array output stream</li>
 * <li>a byte array beat tokenizer</li>
 * </ul>
 * 
 * @author federico
 * 
 */
public class BeatTokenizerStream extends ByteArrayOutputStream implements
		Iterator<byte[]> {

	private boolean hasNext;
	private byte[] token;

	public BeatTokenizerStream(final AudioFormat audioFormat,
			final int bufferSize) {
		super(bufferSize);
		hasNext = false;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public byte[] next() {
		if (!hasNext) {
			throw new NoSuchElementException();
		}
		hasNext = false;
		return token;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public synchronized void write(final byte buffer[], final int offset,
			final int length) {
		super.write(buffer, offset, length);

	}

	@Override
	public synchronized void write(final int b) {
		throw new UnsupportedOperationException();
	}

}
