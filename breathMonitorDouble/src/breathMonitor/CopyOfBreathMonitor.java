package breathMonitor;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;

import persistencyManager.PersistencyManager;
import plot.DevNullPlotter;
import plot.DiscreteTimePlotter;
import plot.PlotterInterface;
import wavFile.WavFile;
import wavFile.WavFileDoubleInputStream;
import wavFile.WavFileException;
import filters.AbsoluteValueFilterInputStream;
import filters.BandPassFilterJSTK;
import filters.DoubleInputStream;
import filters.MaxFilterInputStream;
import filters.MedianFilterInputStream;
import filters.WindowingFilterInputStream;

public class CopyOfBreathMonitor implements Runnable {

	private static final int ALARM_THRESHOLD_SECONDS = 30;

	// "/home/federico/tesi/SfregamentoPleurico-Pleural friction.wav",
	public static void main(final String[] args) throws Exception {
		final String[] allTestFilesName = { "/home/federico/tesi/Coarse crackles.wav",
		// "/home/federico/tesi/Inspiratory stridor.wav",
		// "/home/federico/tesi/MurmureVescicolare-Normal vesicular.wav",
		// "/home/federico/tesi/Pleural friction.wav",
		// "/home/federico/tesi/RantoliGrossolani-Coarse crackles.wav",
		// "/home/federico/tesi/Sibilo-Wheezing.wav",
		// "/home/federico/tesi/StridoreInspiratorio-Inspiratory stridor.wav"
		};
		for (final String nextTestFileName : allTestFilesName) {
			// System.out.println("\n\n\n" + nextTestFileName);
			final CopyOfBreathMonitor breathMonitor = new CopyOfBreathMonitor(
					new WavFileDoubleInputStream(WavFile.openWavFile(new File(
							nextTestFileName))), AudioSystem
							.getAudioInputStream(new File(nextTestFileName))
							.getFormat(), nextTestFileName);
			breathMonitor.run();
			// System.in.read();
			// AudioInputStream audioInputStream = AudioSystem
			// .getAudioInputStream(new File(nextTestFileName));
			// new BrathMonitor(new ByteToDoubleInputStream(audioInputStream),
			// audioInputStream.getFormat(), nextTestFileName).run();
			// audioInputStream.close();
		}
	}

	private final BeatDetectorBooleanInputStream breathAnalizer;

	private final PlotterInterface plotter;

	private final PersistencyManager persistency;

	//private final Writer out1;

	//private final Writer out2;

	// private final WavOutputStream outWavFile;

	public CopyOfBreathMonitor(DoubleInputStream inputStream,
			final AudioFormat format, final String fileName)
			throws IOException, WavFileException, Exception {
		int oneSecondBufferSize = ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize()));
		int bufferTime = 4;
		final int bufferSize = bufferTime * oneSecondBufferSize;
		final int windowSize = oneSecondBufferSize;
		// plotter = new DiscreteTimePlotter("");
		plotter = new DevNullPlotter();

		inputStream = new WindowingFilterInputStream(inputStream, bufferSize,
				windowSize);
		inputStream = new BandPassFilterJSTK(inputStream, 70, 2000,
				(int) format.getSampleRate(), bufferSize);
		inputStream = new AbsoluteValueFilterInputStream(inputStream);
		inputStream = new MedianFilterInputStream(inputStream,
				oneSecondBufferSize / 10);

		breathAnalizer = new BeatDetectorBooleanInputStream(format.getFrameRate(),
				format.getFrameSize(), plotter, oneSecondBufferSize,
				inputStream);
		persistency = new PersistencyManager(fileName, fileName + ".out");
		System.err.println();
	}

	/**
	 * tralascia parte dell'input per assicurare che vengano rispettati certi
	 * livelli di prestazioni real time
	 */
	private void ensureCertainRealTimePerformance() {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		try {
			try {
				while (!Thread.interrupted()) {
					if (breathAnalizer.isBreathing()) {
						System.out.println("breathing");
					} else {
						System.out.println("! not breathing");
						if (breathAnalizer.elapsedTime > ALARM_THRESHOLD_SECONDS) {
							System.err.println("alarm!");
						}
					}
					persistency.write(breathAnalizer.getBreathFrequency());
				}
			} catch (NoDataException e) {
				// do nothig
			}
			breathAnalizer.close();
			persistency.close();

			String fileName = "sdfs";
			final File file = new File(fileName);

			final PrintWriter pw = new PrintWriter(new FileOutputStream(
					new File(file.getName() + ".sdf")));

			pw.println("Original:");
			//pw.println(out1);
			pw.println("\nClustered:");
			//pw.println(out2);
			pw.close();
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
