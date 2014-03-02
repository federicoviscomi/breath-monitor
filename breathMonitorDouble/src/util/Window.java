package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Window implements Iterable<Double> {
	public class WindowIterator implements Iterator<Double> {

		private final double[] buffer;
		private int startIndex;
		private final int endIndex;

		public WindowIterator(final double[] buffer, final int off,
				final int len) {
			this.buffer = buffer;
			startIndex = off;
			endIndex = off + len;
		}

		@Override
		public boolean hasNext() {
			return endIndex > startIndex;
		}

		@Override
		public Double next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			return buffer[startIndex++];
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	private final double[] buffer;
	private final int off;
	private final int len;

	public Window(final double[] buffer, final int off, final int len) {
		this.buffer = buffer;
		this.off = off;
		this.len = len;
	}

	public int getEndIndex() {
		return off + len;
	}

	public double getMax() {
		double max = Double.MIN_VALUE;
		for (final double m : this) {
			if (m > max) {
				max = m;
			}
		}
		return max;
	}

	public double getMin() {
		double min = Double.MAX_VALUE;
		for (final double m : this) {
			if (m < min) {
				min = m;
			}
		}
		return min;
	}

	public int getSize() {
		return len;
	}

	public int getStartIndex() {
		return off;
	}

	@Override
	public Iterator<Double> iterator() {
		return new Window.WindowIterator(buffer, off, len);
	}

}
