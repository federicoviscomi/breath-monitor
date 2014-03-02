package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class WindowsTokenizerInputStream implements Iterable<Window> {

	// pensato perche' venga usata una sola istanza di un iteratore per ogni
	// oggetto di tipo this
	private static class WindowIterator implements Iterator<Window> {
		private double[] buffer;
		private final int windowSize;
		private int startIndex;
		private int endIndex;

		protected WindowIterator(final int windowSize) {
			this.windowSize = windowSize;
		}

		@Override
		public boolean hasNext() {
			return endIndex > startIndex;
		}

		public void init(final double[] buffer, final int offset,
				final int length) {
			this.buffer = buffer;
			startIndex = offset;
			endIndex = offset + length;
			if (buffer.length < windowSize) {
				throw new IllegalArgumentException();
			}
		}

		@Override
		public Window next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final int length = Math.min(windowSize, endIndex - startIndex);
			final Window nextWindow = new Window(buffer, startIndex, length);
			startIndex = startIndex + length;
			return nextWindow;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private WindowIterator iterator;

	public WindowsTokenizerInputStream(final int windowSize) {
		iterator = new WindowIterator(windowSize);
	}

	@Override
	public Iterator<Window> iterator() {
		return iterator;
	}

	public void set(final double[] buffer, final int offset, final int length) {
		iterator.init(buffer, offset, length);
	}

	public void setWindowSize(final int windowSize) {
		iterator = new WindowIterator(windowSize);
	}

}
