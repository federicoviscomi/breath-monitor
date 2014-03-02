package breathMonitor;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;

public class Clusterer {

	private static double distance(Cluster cluster, boolean[] beats, int index) {
		return Math.abs((beats[index] ? 1 : 0)
				- (cluster.beatCount / cluster.size));
	}

	private final Writer _debugOnly_printWriter;

	private final int maxClusterSize;

	public Clusterer(final Writer _debugOnly_printWriter, int maxClusterSize) {
		this._debugOnly_printWriter = _debugOnly_printWriter;
		this.maxClusterSize = maxClusterSize;
	}

	public void close() {
		try {
			_debugOnly_printWriter.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	public ArrayList<Cluster> getClusters(boolean[] beats, int offset,
			int length) {
		// System.err.print("\n" + java.util.Arrays.toString(beats) + " from "+
		// offset + " to " + length);
		ArrayList<Cluster> clusters = new ArrayList<Cluster>();
		if (length == 0) {
			throw new IllegalArgumentException();
		}

		Cluster cluster = new Cluster(beats[offset], 1);
		try {
			_debugOnly_printWriter.write(cluster.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int index = offset; index < length + offset; index++) {
			if ((distance(cluster, beats, index) > 0.7)
					|| (index == length + offset - 1)
					|| (cluster.size > maxClusterSize)) {
				clusters.add(cluster);
				cluster = new Cluster(beats[index], 1);
				try {
					_debugOnly_printWriter.write(cluster.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				cluster.size++;
				if (beats[index]) {
					cluster.beatCount++;
				}
			}
		}

		return clusters;
	}

}
