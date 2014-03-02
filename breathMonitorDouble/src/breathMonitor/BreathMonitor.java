package breathMonitor;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
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

public class BreathMonitor implements Runnable {

	private static final int ALARM_THRESHOLD_SECONDS = 30;

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

	public static void test1() throws UnsupportedAudioFileException, Exception {
		final String[] allTestFilesName = {
		// "/home/federico/tesi/Coarse crackles.wav",
		// "/home/federico/tesi/Inspiratory stridor.wav",
		// "/home/federico/tesi/MurmureVescicolare-Normal vesicular.wav",
		// "/home/federico/tesi/Pleural friction.wav",
		// "/home/federico/tesi/RantoliGrossolani-Coarse crackles.wav",
		// "/home/federico/tesi/Sibilo-Wheezing.wav",
		// "/home/federico/tesi/StridoreInspiratorio-Inspiratory stridor.wav",
		"/home/federico/tesi/normalwithalongapnea.wav",
		// "/home/federico/tesi/CoarseCracklesConcatenato5volte.wav"
		};
		for (final String nextTestFileName : allTestFilesName) {
			System.out.println("\n\n\n" + nextTestFileName);
			final boolean waveRader = true;
			ConsoleIO consoleIO = new ConsoleIO();
			
			boolean plot = false;
			PlotterInterface _plotter;
			if (plot) {
				_plotter = new DiscreteTimePlotter("");
			} else {
				_plotter = new DevNullPlotter();
			}
			if (waveRader) {
				final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
						WavFile.openWavFile(new File(nextTestFileName)));
				final BreathMonitor breathMonitor = new BreathMonitor(
						wavFileDoubleInputStream,
						AudioSystem.getAudioInputStream(
								new File(nextTestFileName)).getFormat(),
						nextTestFileName, true, _plotter, consoleIO, consoleIO);
				breathMonitor.run();
				wavFileDoubleInputStream.close();
			} else {
				final AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(new File(nextTestFileName));
				new BreathMonitor(
						new ByteToDoubleInputStream(audioInputStream),
						audioInputStream.getFormat(), nextTestFileName, true,
						_plotter, consoleIO, consoleIO).run();
				audioInputStream.close();
			}
		}
	}

	public static void main(final String[] args) throws Exception {
		if (true) {
			test1();
		} else {
			String fileName = parseArgs(args);
			final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
					WavFile.openWavFile(new File(fileName)));
			ConsoleIO consoleIO = new ConsoleIO();
			final BreathMonitor breathMonitor = new BreathMonitor(
					wavFileDoubleInputStream, AudioSystem.getAudioInputStream(
							new File(fileName)).getFormat(), fileName, true,
					new DiscreteTimePlotter("sdf"), consoleIO, consoleIO);
			breathMonitor.run();
			wavFileDoubleInputStream.close();
		}
	}

	private DoubleInputStream inputStream;

	private final double[] buffer;

	private final BreathAnalizer breathAnalizer;

	private final PlotterInterface plotter;

	private int oneSecondBufferSize;

	private final PersistencyManager persistency;

	private final int bufferTimeInSeconds;

	private final Writer _debugOnly_out1;

	private final Writer _debugOnly_out2;

	private final int breathCount = 0;

	private OutUserInterface oui;

	private InputUserInterface iui;

	private ThreadMXBean newBean;

	public BreathMonitor(final DoubleInputStream inputStream,
			final AudioFormat format, final String outFileName,
			boolean plotWave, PlotterInterface plotter, OutUserInterface oui,
			InputUserInterface iui) throws IOException, Exception {
		this.plotter = plotter;
		this.inputStream = inputStream;
		this.oui = oui;
		this.iui = iui;
		System.out.println("frame size " + format);
		oneSecondBufferSize = ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize()));
		bufferTimeInSeconds = 1;
		final int bufferSize = bufferTimeInSeconds * oneSecondBufferSize;
		buffer = new double[bufferSize];

		// this.inputStream = new MagnitudeFilterInputStream(this.inputStream);
		// this.inputStream = new WindowingFilterInputStream(this.inputStream,
		// bufferSize, windowSize);

		// this.inputStream = new DownSampleFilterInputStream(this.inputStream,
		// (int) format.getSampleRate(),
		// (int) (format.getSampleRate() / 10));

		// this.inputStream = new TruncateToBufferMultipleFilterInputStream(
		// this.inputStream, buffer.length);

		this.inputStream = new BandPassFilterJSTK(this.inputStream, 70, 2000,
				(int) format.getSampleRate(), bufferSize);
		this.inputStream = new AbsoluteValueFilterInputStream(this.inputStream);
		this.inputStream = new MedianFilterInputStream(this.inputStream,
				oneSecondBufferSize / 10);
		// this.inputStream = newLogFilterInputStream(this.inputStream);

		_debugOnly_out1 = new FileWriter(outFileName + ".beats");
		_debugOnly_out2 = new FileWriter(outFileName + ".clustered");

		// this.inputStream = new MaxFilterInputStream(this.inputStream);
		// this.inputStream = new AdaptiveSequenzialMagnitudeClusteringFilter(
		// this.inputStream, format.getSampleRate());
		// this.inputStream = new
		// VarianceFilterDoubleInputStream(this.inputStream);

		breathAnalizer = new BreathAnalizer(format.getFrameRate(),
				format.getFrameSize(), plotter, oneSecondBufferSize,
				_debugOnly_out1, _debugOnly_out2);
		persistency = new PersistencyManager(outFileName, outFileName + ".out");

		newBean = ManagementFactory.getThreadMXBean();
		try {
			if (newBean.isThreadCpuTimeSupported()) {
				newBean.setThreadCpuTimeEnabled(true);
			} else {
				throw new AccessControlException("");
			}
		} catch (AccessControlException e) {
			System.out.println("CPU Usage monitoring is not available!");
			System.exit(0);
		}

	}

	@Override
	public void run() {
		try {
			double elapsedTime = 0;
			int read = 0;

			long lastTime = System.nanoTime();
			long lastThreadTime = newBean.getCurrentThreadCpuTime();
			float smoothLoad = 0;

			while (!Thread.interrupted()
					&& ((read = inputStream.read(buffer, 0, buffer.length)) > 0)) {
				breathAnalizer.detect(buffer, 0, read);
				elapsedTime = elapsedTime + (read / oneSecondBufferSize);
				try {
					boolean breathing = breathAnalizer.isBreathing();
					oui.out_showBreathingState(elapsedTime, breathing);
					if (breathing) {
						elapsedTime = 0;
					} else if (elapsedTime > ALARM_THRESHOLD_SECONDS) {
						oui.out_startAlarm();
						iui.in_WaitForUserToStopAlarm();
						oui.out_stopAlarm();
					}
				} catch (final NoDataException e) {
					// System.err.println("NoDataException");
				}
				persistency.write(breathCount);

				// Calculate coarse CPU usage:
				long time = System.nanoTime();
				long threadTime = newBean.getCurrentThreadCpuTime();
				double load = (threadTime - lastThreadTime)
						/ (double) (time - lastTime);
				// Smooth it.
				smoothLoad += (load - smoothLoad) * 0.1;
				System.out.println("CPU time = " + smoothLoad
						+ " nanoseconds, signal time = "
						+ (read / oneSecondBufferSize));

				// For next iteration.
				lastTime = time;
				lastThreadTime = threadTime;
			}
			inputStream.close();
			breathAnalizer.close();
			persistency.close();
			_debugOnly_out1.close();
			_debugOnly_out2.close();
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
