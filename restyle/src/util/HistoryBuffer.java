package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HistoryBuffer implements Iterable<Double> {

	private static class HistoryBufferIterator implements Iterator<Double> {
		private final double[] items;
		private final int size;
		private int start;
		private final int end;
		private int s_msb;
		private final int e_msb;

		public HistoryBufferIterator(final double[] items, final int size,
				final int start, final int end, final int s_msb, final int e_msb) {
			this.items = items;
			this.size = size;
			this.start = start;
			this.end = end;
			this.s_msb = s_msb;
			this.e_msb = e_msb;
		}

		@Override
		public boolean hasNext() {
			return !((end == start) && (e_msb == s_msb));
		}

		@Override
		public Double next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final double item = items[start];
			start = start + 1;
			if (start == size) {
				s_msb ^= 1;
				start = 0;
			}
			return item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	public static void main(final String[] args) {
		final HistoryBuffer hb = new HistoryBuffer(20);
		for (int i = 0; i < 20; i++) {
			hb.write(i);
		}

		for (final double d : hb) {
			System.out.print(" " + d);
		}
		System.out.println();
		hb.write(2);
		for (final double d : hb) {
			System.out.print(" " + d);
		}
		hb.write(2);
		System.out.println();
		for (final double d : hb) {
			System.out.print(" " + d);
		}
	}

	/* vector of elements */
	private final double[] items;

	/* maximum number of elements */
	private final int size;

	/* index of oldest element */
	private int start;

	/* index at which to write new element */
	private int end;

	private int s_msb;
	private int e_msb;
	private int elemCount;

	public HistoryBuffer(final int size) {
		this.size = size;
		items = new double[size];
		init();
	}

	private void cbIncrE() {
		end++;
		if (end == size) {
			e_msb ^= 1;
			end = 0;
		}
	}

	private void cbIncrS() {
		start = start + 1;
		if (start == size) {
			s_msb ^= 1;
			start = 0;
		}
	}

	public void clear() {
		init();
	}

	public double getElementCount() {
		return elemCount;
	}

	private void init() {
		start = 0;
		end = 0;
		s_msb = 0;
		e_msb = 0;
		elemCount = 0;
	}

	public boolean isEmpty() {
		return (end == start) && (e_msb == s_msb);
	}

	// public double read() {
	// if (isEmpty()) {
	// throw new NoSuchElementException();
	// }
	// double item = items[start];
	// cbIncrS();
	// return item;
	// }

	public boolean isFull() {
		return (end == start) && (e_msb != s_msb);
	}

	@Override
	public Iterator<Double> iterator() {
		return new HistoryBuffer.HistoryBufferIterator(items, size, start, end,
				s_msb, e_msb);
	}

	public void write(final double elem) {
		items[end] = elem;

		if (elemCount < size) {
			elemCount++;
		}

		if (isFull()) { /* full, overwrite moves start pointer */
			cbIncrS();
		}
		cbIncrE();
	}
}