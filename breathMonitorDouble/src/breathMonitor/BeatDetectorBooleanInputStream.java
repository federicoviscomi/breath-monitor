package breathMonitor;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import plot.PlotterInterface;
import util.HistoryBuffer;
import util.Window;
import util.WindowsTokenizerInputStream;
import filters.BooleanInputStream;
import filters.DoubleInputStream;
import filters.Util;

public class BeatDetectorBooleanInputStream implements BooleanInputStream {

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

	private final CopyOfBreathingPattern breathingPattern;

	private final PlotterInterface plotter;

	private final WindowsTokenizerInputStream wtis;

	private final Writer printWriter = null;

	public int elapsedTime;

	private DoubleInputStream inputStream;

	private double[] buffer;

	/**
	 * 
	 * @param frameRate
	 * @param frameSize
	 * @param plotter
	 * @param windowsSize
	 *            corrisponde ad un secondo
	 * @param inputStream
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public BeatDetectorBooleanInputStream(final double frameRate, final double frameSize,
			final PlotterInterface plotter, final int windowsSize,
			DoubleInputStream inputStream) throws FileNotFoundException {
		this.plotter = plotter;
		this.inputStream = inputStream;
		soundEnergyHistoryBuffer = new HistoryBuffer(50);
		breathingPattern = new CopyOfBreathingPattern(null);
		windowSize = windowsSize / 10;
		wtis = new WindowsTokenizerInputStream(windowSize);
		// System.err.println();

		// File file = new File(fileName);
		// printWriter = new Writer(new FileOutputStream(new File("sdfO."
		// + file.getName() + ".sdf")));
	}

	public void changeOnsetDetectionWindowsSize(final int idealWindowSize) {
		wtis.setWindowSize(idealWindowSize);
	}

	public void close() {
		try {
			breathingPattern.close();
		} catch (IOException e) {
			// do nothing
		}
	}


	@Override
	public int read(boolean[] b, int off, int len) throws IOException {
		inputStream.read(b, off, len)
		wtis.set(buffer, offset, length);
		soundEnergyHistoryBuffer.clear();
		for (final Window w : wtis) {
			// System.err.println("window bounds:" + w.getStartIndex() + "->"+
			// w.getEndIndex());
			final double instantSoundEnergy = Util.getInstantSoundEnergy(w);
			final double averageLocalEnergy = Util
					.getAverageLocalEnergy(soundEnergyHistoryBuffer);
			final double variance = Util.getVariance(soundEnergyHistoryBuffer,
					averageLocalEnergy);
			final double linearRegression = Util.getLinearRegression(variance);
			// final double linearRegression = 1;
			soundEnergyHistoryBuffer.write(instantSoundEnergy);

			final boolean isABeat = (instantSoundEnergy > (linearRegression * averageLocalEnergy));
			// final boolean isABeat = (instantSoundEnergy >
			// averageLocalEnergy);
			breathingPattern.add(isABeat);

			if (isABeat) {
				printWriter.write("*");
			} else {
				printWriter.write("_");
				// printWriter.write("*");
			}

			// System.err.format("istant sound energy:%20E, averageLocalEnergy:%20E, variance:%20E\n",instantSoundEnergy,
			// averageLocalEnergy, variance);
			// System.err.println("is a beat?" + isABeat);
			if (isABeat) {
				plotter.plotBeatFound(Color.GREEN);
			} else {
				plotter.plotBeatFound(Color.RED);
			}
			plotter.plotPoints(buffer, w.getStartIndex(), w.getSize(),
					Color.BLACK);
		}

		
		return 0;
	}

}
