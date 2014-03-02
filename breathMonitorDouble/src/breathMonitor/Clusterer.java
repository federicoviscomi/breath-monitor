package breathMonitor;

import java.io.IOException;
import java.io.Writer;

public class Clusterer {

	private final BeatsBuffer beats;

	private final Writer printWriter;

	public Clusterer(final Writer out2) {
		printWriter = out2;
		beats = new BeatsBuffer();
	}

	public void add(final boolean isABeat) {
		beats.add(isABeat);
	}

	public void close() throws IOException {
		printWriter.close();

	}

	public Cluster getNext() throws NoDataException, IOException {

		// System.err.println(beats);

		if (beats.size() == 0) {
			throw new NoDataException();
		}

		beats.polled = 0;
		int beatCount = 0;
		System.err.print("\n" + beats.size() + "inizio ");
		boolean clusterType;
		if (beats.poll()) {
			beatCount = 1;
			System.err.print(" " + (beatCount / beats.polled));
			clusterType = true;
			for (int i = 0; i < beats.size(); i++) {
				if (beats.poll()) {
					beatCount++;
				}
				System.err.print(" " + (beatCount / beats.polled));
				if ((beatCount / beats.polled) < 0.8) {
					break;
				}
			}
		} else {
			beatCount = 0;
			System.err.print(" " + (beatCount / beats.polled));
			clusterType = false;
			for (int i = 0; i < beats.size(); i++) {
				if (beats.poll()) {
					beatCount++;
				}
				System.err.print(" " + (beatCount / beats.polled));
				if (beatCount / beats.polled > 0.2) {
					break;
				}
			}
		}

		System.err.println(" fine");
		Cluster cluster = new Cluster(clusterType, beats.polled);
		printWriter.write(cluster.toString());
		return cluster;
	}

}
