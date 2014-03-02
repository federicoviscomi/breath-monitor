package breathMonitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class BreathingPattern {

	private final Clusterer clusterer;
	private final ArrayList<Cluster> clusterBuffer;
	private int breathCount;

	public BreathingPattern(final Writer out2) throws FileNotFoundException {
		clusterer = new Clusterer(out2);
		clusterBuffer = new ArrayList<Cluster>();
	}

	public void add(final boolean isABeat) {
		clusterer.add(isABeat);
	}

	public void close() throws IOException {
		try {
			while (true) {
				clusterBuffer.add(clusterer.getNext());
			}
		} catch (final NoDataException e) {
			// do nothing
		}
		clusterer.close();
	}

	public int getBreathCount() {
		return breathCount;
	}

	public double getBreathFrequency() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isABreath() throws NoDataException, IOException {
		// a breath is a non beat cluster followed by a beat cluster, followed
		// by a non beat cluster
		while (clusterBuffer.size() < 3) {
			clusterBuffer.add(clusterer.getNext());
		}
		final boolean isABreath = !clusterBuffer.get(0).isABeat()
				&& clusterBuffer.get(1).isABeat()
				&& !clusterBuffer.get(2).isABeat();
		clusterBuffer.clear();
		if (isABreath) {
			breathCount++;
		}
		return isABreath;
	}

}
