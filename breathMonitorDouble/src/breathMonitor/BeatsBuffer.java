package breathMonitor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BeatsBuffer {
	private final Queue<Boolean> beats;

	private double blocklAverageDensity = -1;

	public int polled = 0;

	public BeatsBuffer() {
		beats = new ConcurrentLinkedQueue<Boolean>();
	}

	public void add(final boolean isABeat) {
		beats.add(isABeat);
		if (isABeat) {
			blocklAverageDensity = (blocklAverageDensity + 1) / 2;
		}
	}

	public boolean peek() {
		return beats.peek();
	}

	public boolean poll() {
		if (beats.size() == 1) {
			blocklAverageDensity = -1;
		}
		polled++;
		return beats.poll();
	}

	public int size() {
		return beats.size();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (final boolean b : beats) {
			if (b) {
				sb.append("*");
			} else {
				sb.append("_");
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
