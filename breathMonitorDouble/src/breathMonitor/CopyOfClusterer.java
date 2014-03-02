package breathMonitor;

import java.io.IOException;
import java.io.Writer;

public class CopyOfClusterer {

	private final BeatsBuffer beats;

	private final Writer printWriter;

	public CopyOfClusterer(final Writer out2) {
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
		double blockDensity;
		System.err.println(" ");
		if (beats.poll()) {
			blockDensity = 1;
			System.err.print(" " + blockDensity);
		} else {
			blockDensity = 0;
			System.err.print(" " + blockDensity);
		}
		for (int i = 0; i < beats.size(); i++) {
			if (beats.poll()) {
				blockDensity = (blockDensity + 1) * 0.6;
				System.err.print(" " + blockDensity);
			} else {
				blockDensity = blockDensity * 0.6;
				System.err.print(" " + blockDensity);
			}
			if (blockDensity < 0.3) {
				break;
			}
		}
		System.err.println(" ");
		Cluster cluster;
		if (blockDensity == 0) {
			for (int i = 0; (i < beats.size()) && !beats.peek(); i++) {
				beats.poll();
			}
			cluster = new Cluster(false, beats.polled);
		} else {
			cluster = new Cluster(true, beats.polled);
		}

		printWriter.write(cluster.toString());
		return cluster;
	}

}
