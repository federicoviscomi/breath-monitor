package breathMonitor;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.sound.sampled.AudioFormat;

import plot.PlotterInterface;
import util.HistoryBuffer;
import util.Window;
import util.WindowsTokenizerInputStream;
import filters.DoubleInputStream;
import filters.Util;

public class BeatDetectorInputStream {

	private final HistoryBuffer soundEnergyHistoryBuffer;

	private final PlotterInterface plotter;

	private final WindowsTokenizerInputStream wtis;

	private final Writer _debugOnly_writer;

	private final DoubleInputStream inputStream;

	private final double[] buffer;

	public BeatDetectorInputStream(final double frameRate,
			final double frameSize, final PlotterInterface plotter,
			final int windowSize, final Writer _debugOnly_writer,
			AudioFormat format, DoubleInputStream inputStream) {
		this.plotter = plotter;
		this._debugOnly_writer = _debugOnly_writer;
		this.inputStream = inputStream;
		this.soundEnergyHistoryBuffer = new HistoryBuffer(50);
		this.wtis = new WindowsTokenizerInputStream(windowSize);

		int oneSecondBufferSize = ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize()));
		int bufferTimeInSeconds = 1;
		final int bufferSize = bufferTimeInSeconds * oneSecondBufferSize;
		this.buffer = new double[bufferSize];
	}

	public void close() {
		try {
			inputStream.close();
			_debugOnly_writer.close();
		} catch (IOException e) {
			// do nothing
		}
	}

	public int readBeats(final boolean[] beats, final int offset,
			final int length) throws IOException {
		int read = inputStream.read(buffer, 0, buffer.length);
		// System.out.println("read " + read);
		wtis.set(buffer, 0, read);
		soundEnergyHistoryBuffer.clear();
		int beatsCount = offset;
		for (final Window w : wtis) {
			double instantSoundEnergy = Util.getInstantSoundEnergy(w);
			double averageLocalEnergy = Util
					.getAverageLocalEnergy(soundEnergyHistoryBuffer);
			double variance = Util.getVariance(soundEnergyHistoryBuffer,
					averageLocalEnergy);
			double linearRegression = Util.getLinearRegression(variance);

			soundEnergyHistoryBuffer.write(instantSoundEnergy);

			// final boolean isABeat = (instantSoundEnergy > (linearRegression *
			// averageLocalEnergy));
			final boolean isABeat = (instantSoundEnergy > averageLocalEnergy);

			beats[beatsCount++] = isABeat;

			if (isABeat) {
				_debugOnly_writer.write("*");
			} else {
				_debugOnly_writer.write("_");
			}

			// System.err.println("is a beat?" + isABeat);
			if (isABeat) {
				plotter.plotBeatFound(Color.GREEN);
			} else {
				plotter.plotBeatFound(Color.RED);
			}
			plotter.plotPoints(buffer, w.getStartIndex(), w.getSize());
		}
		return beatsCount;
	}

}
