package beatFinder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.sound.sampled.AudioFormat;

import ddf.minim.analysis.BeatDetect;

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

	private int maxWindowSize;

	private static float[] byteToFloat(byte[] byteBuffer, int start, int length) {
		final float[] floatBuffer = new float[length];
		for (int i = 0; i < length; i++) {
			floatBuffer[i] = byteBuffer[i + start];
		}
		return floatBuffer;
	}

	private boolean hasNext;
	private byte[] token;
	private final BeatDetect bd;
	private int charCount;
	private int onset = 1;

	public BeatTokenizerStream(AudioFormat audioFormat, int bufferSize) {
		super(2 * bufferSize);
		hasNext = false;
		// bufferSize, audioFormat.getSampleRate()
		bd = new BeatDetect();
		maxWindowSize = (int) (audioFormat.getFrameRate() * audioFormat
				.getFrameSize()) * 2;
	}

	public BeatTokenizerStream(AudioFormat format, int timeSize,
			float sampleRate) {
		super(timeSize);
		hasNext = false;
		// bufferSize, audioFormat.getSampleRate()
		bd = new BeatDetect(timeSize, sampleRate);
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
	public synchronized void write(byte buffer[], int offset, int length) {
		super.write(buffer, offset, length);
		bd.detect(byteToFloat(buffer, offset, length));
		if (bd.isOnset()) {
			hasNext = true;
			token = super.toByteArray();
			super.reset();
			System.out.print(onset);
			onset++;
		} else if (super.size() >= maxWindowSize) {
			hasNext = true;
			token = super.toByteArray();
			super.reset();
		} else {
			System.out.print(".");
		}
		charCount++;
		if (charCount % 60 == 0) {
			// System.out.println();
		}
	}

	@Override
	public synchronized void write(int b) {
		throw new UnsupportedOperationException();
	}

}
