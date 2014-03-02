package breathMonitor;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.security.AccessControlException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import persistencyManager.PersistencyManager;
import plot.DevNullPlotter;
import plot.DiscreteTimePlotter;
import plot.PlotterInterface;
import userInterface.ConsoleIO;
import userInterface.ConsoleOutInterface;
import userInterface.InputUserInterface;
import userInterface.OperationMonitor;
import userInterface.OutUserInterface;
import wavFile.WavFile;
import wavFile.WavFileDoubleInputStream;
import wavFile.WavFileException;
import filters.AbsoluteValueFilterInputStream;
import filters.BandPassFilterJSTK;
import filters.ByteToDoubleInputStream;
import filters.DensityLikeDistanceFilter;
import filters.DoubleInputStream;
import filters.DownSampleFilterInputStream;
import filters.MedianFilterInputStream;
import filters.SubtractMeanFilterInputStream;

public class BreathMonitor implements Runnable {

	// TODO scegliere un valore piu' significativo
	private static final int MAX_CLUSTER_SIZE = 20;

	private static final int WINDOW_SIZE = 800;

	private static final int MIN_SAMPLE_RATE = 200;

	private static final int DOWN_SAMPLING_RATE = 10;

	public static void main(final String[] args) throws Exception {
		if (true) {
			test1(true);
			// test2();
			// test3(false);
		} else {
			String fileName = parseArgs(args);
			final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
					WavFile.openWavFile(new File(fileName)));
			ConsoleIO consoleIO = new ConsoleIO();
			final BreathMonitor breathMonitor = new BreathMonitor(
					wavFileDoubleInputStream, AudioSystem.getAudioInputStream(
							new File(fileName)).getFormat(), fileName, true,
					new DiscreteTimePlotter(), consoleIO, consoleIO);
			breathMonitor.run();
			wavFileDoubleInputStream.close();
		}
	}

	public static String parseArgs(String[] args) {
		if (args.length <= 3) {
			System.err.println("usage: monitor -f filename");
			System.exit(-1);
		}
		if (!args[0].startsWith("monitor") && !args[1].startsWith("-f")) {
			System.err.println("usage: monitor -f filename");
			System.exit(-1);
		}
		String fileName = args[3];
		for (int i = 3; i < args.length; i++) {
			fileName = fileName + " " + args[i];
		}
		return fileName;
	}

	public static void test1(boolean enablePlotter)
			throws UnsupportedAudioFileException, Exception {
		final String[] allTestFilesName = { "/home/federico/tesi/wav/RantoliGrossolani-Coarse crackles.wav",
		// "/home/federico/tesi/Inspiratory stridor.wav",
		// "/home/federico/tesi/MurmureVescicolare-Normal vesicular.wav",
		// "/home/federico/tesi/Pleural friction.wav",
		// "/home/federico/tesi/RantoliGrossolani-Coarse crackles.wav",
		// "/home/federico/tesi/Sibilo-Wheezing.wav",
		// "/home/federico/tesi/StridoreInspiratorio-Inspiratory stridor.wav"
		// "/home/federico/tesi/normalwithalongapnea.wav"
		};
		PlotterInterface plotter;
		if (enablePlotter) {
			plotter = new DiscreteTimePlotter();
		} else {
			plotter = new DevNullPlotter();
		}

		for (final String nextTestFileName : allTestFilesName) {
			System.out.println("\n\n\n" + nextTestFileName);
			final boolean waveRader = true;
			ConsoleIO consoleIO = new ConsoleIO();
			if (waveRader) {
				final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
						WavFile.openWavFile(new File(nextTestFileName)));
				final BreathMonitor breathMonitor = new BreathMonitor(
						wavFileDoubleInputStream,
						AudioSystem.getAudioInputStream(
								new File(nextTestFileName)).getFormat(),
						nextTestFileName, true, plotter, consoleIO, consoleIO);
				breathMonitor.run();
				wavFileDoubleInputStream.close();
			} else {
				final AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(new File(nextTestFileName));
				new BreathMonitor(
						new ByteToDoubleInputStream(audioInputStream),
						audioInputStream.getFormat(), nextTestFileName, true,
						plotter, consoleIO, consoleIO).run();
				audioInputStream.close();
			}
		}
	}

	public static void test2() throws UnsupportedAudioFileException, Exception {
		final String[] allTestFilesName = {
				"/home/federico/tesi/test normal white noise/normal00.wav",
				"/home/federico/tesi/test normal white noise/normal02.wav",
				"/home/federico/tesi/test normal white noise/normal04.wav",
				"/home/federico/tesi/test normal white noise/normal06.wav",
				"/home/federico/tesi/test normal white noise/normal08.wav",
				"/home/federico/tesi/test normal white noise/normal10.wav",
				"/home/federico/tesi/test normal white noise/normal12.wav",
				"/home/federico/tesi/test normal white noise/normal14.wav",
				"/home/federico/tesi/test normal white noise/normal16.wav",
				"/home/federico/tesi/test normal white noise/normal18.wav",
				"/home/federico/tesi/test normal white noise/normal20.wav" };
		for (final String nextTestFileName : allTestFilesName) {
			// System.out.println("\n\n\n" + nextTestFileName);
			final boolean waveRader = true;
			ConsoleIO consoleIO = new ConsoleIO();
			final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
					WavFile.openWavFile(new File(nextTestFileName)));
			final BreathMonitor breathMonitor = new BreathMonitor(
					wavFileDoubleInputStream, AudioSystem.getAudioInputStream(
							new File(nextTestFileName)).getFormat(),
					nextTestFileName, true, new DevNullPlotter(), consoleIO,
					consoleIO);
			breathMonitor.run();
			wavFileDoubleInputStream.close();
		}
	}

	public static void test3(boolean enablePlotter)
			throws UnsupportedAudioFileException, Exception {
		File wavDir = new File("/home/federico/tesi/wav/");
		File[] wavTestFiles = wavDir.listFiles();

		PlotterInterface plotter;
		if (enablePlotter) {
			plotter = new DiscreteTimePlotter();
		} else {
			plotter = new DevNullPlotter();
		}

		for (final File nextTestFile : wavTestFiles) {
			if (!nextTestFile.isDirectory()
					&& nextTestFile.getName().endsWith("wav")) {
				System.out.println("\n\n\n" + nextTestFile);
				final boolean waveRader = true;
				ConsoleIO consoleIO = new ConsoleIO();
				if (waveRader) {
					final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
							WavFile.openWavFile(nextTestFile));
					final BreathMonitor breathMonitor = new BreathMonitor(
							wavFileDoubleInputStream, AudioSystem
									.getAudioInputStream(nextTestFile)
									.getFormat(),
							nextTestFile.getAbsolutePath(), true, plotter,
							consoleIO, consoleIO);
					breathMonitor.run();
					wavFileDoubleInputStream.close();
				} else {
					final AudioInputStream audioInputStream = AudioSystem
							.getAudioInputStream(nextTestFile);
					new BreathMonitor(new ByteToDoubleInputStream(
							audioInputStream), audioInputStream.getFormat(),
							nextTestFile.getAbsolutePath(), true, plotter,
							consoleIO, consoleIO).run();
					audioInputStream.close();
				}
			}
		}
	}

	private int alarmThreasholdWindows;

	private DoubleInputStream inputStream;

	private final double[] buffer;

	private final BreathingClusterInputStream breathingClusterInputStream;

	private final PlotterInterface plotter;

	private int oneSecondBufferSize;

	private final PersistencyManager persistency;

	private final int bufferTimeInSeconds;

	private final PrintWriter _debugOnly_out1;

	private final PrintWriter _debugOnly_out2;

	private final int breathCount = 0;

	private final OutUserInterface oui;

	private final InputUserInterface iui;

	private static int varCount = 0;

	private static double test2 = 1;

	public BreathMonitor(final DoubleInputStream inputStream,
			final AudioFormat format, final String outFileName,
			boolean plotWave, PlotterInterface plotter, OutUserInterface oui,
			InputUserInterface iui) throws IOException, WavFileException,
			Exception {
		this.plotter = plotter;
		this.inputStream = inputStream;
		this.oui = oui;
		this.iui = iui;
		// System.out.println("format " + format);
		oneSecondBufferSize = ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize()));
		bufferTimeInSeconds = 1;
		final int bufferSize = bufferTimeInSeconds * oneSecondBufferSize;
		buffer = new double[bufferSize];

		// this.inputStream = new MagnitudeFilterInputStream(this.inputStream);
		// this.inputStream = new WindowingFilterInputStream(this.inputStream,
		// bufferSize, windowSize);

		if ((int) format.getSampleRate() > DOWN_SAMPLING_RATE * MIN_SAMPLE_RATE) {
			// this.inputStream = new
			// DownSampleFilterInputStream(this.inputStream,
			// (int) format.getSampleRate(),
			// (int) (format.getSampleRate() / DOWN_SAMPLING_RATE));
		}

		// this.inputStream = new TruncateToBufferMultipleFilterInputStream(
		// this.inputStream, buffer.length);

		this.inputStream = new BandPassFilterJSTK(this.inputStream, 70, 2000,
				(int) format.getSampleRate(), bufferSize);
		this.inputStream = new AbsoluteValueFilterInputStream(this.inputStream);
		this.inputStream = new MedianFilterInputStream(this.inputStream,
				oneSecondBufferSize / 10);
		// this.inputStream = new
		// SubtractMeanFilterInputStream(this.inputStream);
		// this.inputStream = newLogFilterInputStream(this.inputStream);

		_debugOnly_out1 = new PrintWriter(
				new FileWriter(outFileName + ".beats"));
		_debugOnly_out2 = new PrintWriter(new FileWriter(outFileName
				+ ".clustered"));

		// this.inputStream = new MaxFilterInputStream(this.inputStream);
		// this.inputStream = new AdaptiveSequenzialMagnitudeClusteringFilter(
		// this.inputStream, format.getSampleRate());
		// this.inputStream = new
		// VarianceFilterDoubleInputStream(this.inputStream);

		breathingClusterInputStream = new BreathingClusterInputStream(
				this.inputStream, format.getFrameRate(), format.getFrameSize(),
				plotter, bufferSize, _debugOnly_out1, _debugOnly_out2, format,
				WINDOW_SIZE, MAX_CLUSTER_SIZE);

		alarmThreasholdWindows = (int) Math
				.floor(15 / (((double) WINDOW_SIZE) / oneSecondBufferSize));

		persistency = new PersistencyManager(outFileName, outFileName + ".out");
		System.err.println();
	}

	@Override
	public void run() {
		try {

			ThreadMXBean newBean = ManagementFactory.getThreadMXBean();

			if (newBean.isThreadCpuTimeSupported()) {
				newBean.setThreadCpuTimeEnabled(true);
			} else {
				throw new AccessControlException("");
			}

			long pauseSize = 0;
			double totalElapsedTime = 0;
			Cluster breathing;
			// System.out.print("\n[");
			// double[] x = new double[100];
			// double[] y = new double[100];
			// int i = 0;
			long lastTime = System.nanoTime();
			long lastThreadTime = newBean.getCurrentThreadCpuTime();
			float smoothLoad = 0;

			while (!Thread.interrupted()
					&& (breathing = breathingClusterInputStream
							.readNextCluster()) != null) {

				// System.out.println(breathing);
				totalElapsedTime = totalElapsedTime + breathing.size
						* (((double) WINDOW_SIZE) / oneSecondBufferSize);

				if (breathing.isABreath) {
					pauseSize = 0;
				} else {
					pauseSize = pauseSize + breathing.size;
					if (pauseSize > alarmThreasholdWindows) {
						oui.out_startAlarm();
						iui.in_WaitForUserToStopAlarm();
						oui.out_stopAlarm();
						pauseSize = 0;
					}
				}
				System.out.println("pause size " + pauseSize);
				oui.out_showBreathingState(totalElapsedTime,
						breathing.isABreath);
			}

			// Calculate coarse CPU usage:
			long time = System.nanoTime();
			long threadTime = newBean.getCurrentThreadCpuTime();
			double load = (threadTime - lastThreadTime)
					/ (double) (time - lastTime);
			// Smooth it.
			// damping factor, lower means less responsive, 1 means no
			// smoothing.
			smoothLoad += (load - smoothLoad) * 0.1;
			System.out.format("Total CPU time %d ms\n",
					((threadTime - lastThreadTime) / 1000000));
			System.out.format("Total elapsed time %d ms \n",
					((time - lastTime) / 1000000));

			// For next iteration.
			// lastTime = time;
			// lastThreadTime = threadTime;

			// test2 = test2 + 1;
			// varCount++;
			_debugOnly_out1.flush();
			_debugOnly_out2.flush();
			_debugOnly_out1.close();
			_debugOnly_out2.close();
			inputStream.close();
			breathingClusterInputStream.close();
			persistency.close();

			// System.out.print("\nA" + varCount + "=[");
			// for (int j = 0; j < i; j++) {
			// System.out.print(x[j]);
			// if (j != i - 1) {
			// System.out.print(", ");
			// }
			// }
			// System.out.print("];\n");

			// System.out.print("\nB" + varCount + "=[");
			// for (int j = 0; j < i; j++) {
			// System.out.print(y[j]);
			// if (j != i - 1) {
			// System.out.print(", ");
			// }
			// }
			// System.out.print("];\n");

		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
