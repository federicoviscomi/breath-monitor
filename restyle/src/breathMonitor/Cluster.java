package breathMonitor;

public class Cluster {

	public boolean isABreath;

	public int size;

	public int beatCount;

	public Cluster() {
		this.isABreath = false;
		this.size = 0;
		this.beatCount = 0;
	}

	public Cluster(final boolean beat, final int size) {
		this.isABreath = beat;
		this.size = size;
	}

	public boolean isABeat() {
		return isABreath;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(size);
		if (isABreath) {
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					// sb.append("|");
					sb.append("*");
				} else {
					sb.append("*");
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				if (i == 0) {
					// sb.append("=");
					sb.append("_");
				} else {
					sb.append("_");
					// sb.append("*");
				}
			}
		}
		return sb.toString();
	}

}
