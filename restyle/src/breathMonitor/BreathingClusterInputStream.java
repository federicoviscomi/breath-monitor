package breathMonitor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.LinkedBlockingQueue;

import javax.sound.sampled.AudioFormat;

import filters.DoubleInputStream;

import plot.PlotterInterface;

public class BreathingClusterInputStream {

	private final BeatDetectorInputStream beatDetector;
	private final boolean[] beats;
	private final LinkedBlockingQueue<Cluster> clusterBuffer;
	private final Clusterer clusterer;

	public BreathingClusterInputStream(DoubleInputStream inputStream,
			float frameRate, int frameSize, PlotterInterface plotter,
			int bufferSize, Writer _debugOnly_out1, Writer _debugOnly_out2,
			AudioFormat format, int windowSize, int clusterSize)
			throws FileNotFoundException {
		this.clusterer = new Clusterer(_debugOnly_out2, clusterSize);
		this.beatDetector = new BeatDetectorInputStream(frameRate, frameSize,
				plotter, windowSize, _debugOnly_out1, format, inputStream);
		this.clusterBuffer = new LinkedBlockingQueue<Cluster>();
		this.beats = new boolean[100 * ((int) Math.floor(bufferSize
				/ windowSize))];
	}

	public void close() {
		this.beatDetector.close();
		this.clusterer.close();
	}

	public Cluster readNextCluster() throws IOException {
		Cluster nextCluster = clusterBuffer.poll();
		if (nextCluster != null) {
			return nextCluster;
		}
		int read = beatDetector.readBeats(beats, 0, beats.length);
		if (read <= 0) {
			return null;
		}
		clusterBuffer.addAll(clusterer.getClusters(beats, 0, read));
		return clusterBuffer.poll();
	}

}
