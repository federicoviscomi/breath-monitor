package breathMonitor;

public class Cluster {

	private final boolean beat;

	private final int size;

	public static int totalSize = 0;

	public Cluster(final boolean beat, final int size) {
		this.beat = beat;
		this.size = size;
		totalSize = totalSize + size;
	}

	public boolean isABeat() {
		return beat;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(size);
		if (beat) {
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					sb.append("|");
					// sb.append("*");
				} else {
					sb.append("*");
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					sb.append("=");
					// sb.append("*");
				} else {
					sb.append("_");
					// sb.append("*");
				}
			}
		}
		return sb.toString();
	}

}
