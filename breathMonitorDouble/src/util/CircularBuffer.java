package util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CircularBuffer<T> implements Iterable<T> {

	private static class CircularBufferIterator<T> implements Iterator<T> {
		private final T[] items;
		private final int size;
		private int start;
		private final int end;
		private int s_msb;
		private final int e_msb;

		public CircularBufferIterator(final T[] items, final int size,
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
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			final T item = items[start];
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
		final CircularBuffer<Double> hb = new CircularBuffer<Double>(20);
		for (int i = 0; i < 20; i++) {
			hb.write(new Double(i));
		}

		for (final double d : hb) {
			System.out.print(" " + d);
		}
		System.out.println();
		hb.write(new Double(2));
		for (final double d : hb) {
			System.out.print(" " + d);
		}
		hb.write(new Double(2));
		System.out.println();
		for (final double d : hb) {
			System.out.print(" " + d);
		}
	}

	/* vector of elements */
	private final T[] items;

	/* maximum number of elements */
	private final int size;

	/* index of oldest element */
	private int start;

	/* index at which to write new element */
	private int end;

	private int s_msb;
	private int e_msb;
	private int elemCount;

	public CircularBuffer(final int size) {
		this.size = size;
		items = (T[]) new Object[size];
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
	public Iterator<T> iterator() {
		return new CircularBuffer.CircularBufferIterator(items, size, start, end,
				s_msb, e_msb);
	}

	public void write(final T elem) {
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