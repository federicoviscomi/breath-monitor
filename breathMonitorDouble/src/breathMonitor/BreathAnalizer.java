package breathMonitor;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;

import plot.PlotterInterface;
import util.HistoryBuffer;
import util.Window;
import util.WindowsTokenizerInputStream;
import filters.Util;

public class BreathAnalizer {

	public static double average(final byte[] arr) {
		double avg = 0;
		for (int i = 0; i < arr.length; i++) {
			avg += arr[i];
		}
		avg /= arr.length;
		return avg;
	}

	private final int windowSize;

	private final HistoryBuffer soundEnergyHistoryBuffer;

	private final BreathingPattern breathingPattern;

	private final PlotterInterface plotter;

	private final WindowsTokenizerInputStream wtis;

	private final Writer _debugOnly_out1;

	/**
	 * 
	 * @param frameRate
	 * @param frameSize
	 * @param plotter
	 * @param windowSize
	 *            corrisponde ad un secondo
	 * @param _debugOnly_out2
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public BreathAnalizer(final double frameRate, final double frameSize,
			final PlotterInterface plotter, final int windowSize,
			final Writer _debugOnly_out1, final Writer _debugOnly_out2)
			throws FileNotFoundException {
		this.plotter = plotter;
		this._debugOnly_out1 = _debugOnly_out1;
		this.soundEnergyHistoryBuffer = new HistoryBuffer(50);
		this.breathingPattern = new BreathingPattern(_debugOnly_out2);
		this.windowSize = windowSize;
		this.wtis = new WindowsTokenizerInputStream(windowSize);
	}

	public void close() throws IOException {
		breathingPattern.close();
	}

	public void detect(final double[] buffer, final int offset, final int length)
			throws IOException {
		wtis.set(buffer, offset, length);
		soundEnergyHistoryBuffer.clear();
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
			breathingPattern.add(isABeat);

			if (isABeat) {
				_debugOnly_out1.write("*");
			} else {
				_debugOnly_out1.write("_");
			}

			System.err.println("is a beat?" + isABeat);
			if (isABeat) {
				plotter.plotBeatFound(Color.GREEN);
			} else {
				plotter.plotBeatFound(Color.RED);
			}
			plotter.plotPoints(buffer, w.getStartIndex(), w.getSize());
		}
	}

	public boolean isBreathing() throws NoDataException, IOException {
		return breathingPattern.isABreath();
	}
}
