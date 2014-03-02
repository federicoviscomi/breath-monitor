package userInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import plot.PlotterInterface;

import wavFile.WavFile;
import wavFile.WavFileDoubleInputStream;
import wavFile.WavFileException;
import breathMonitor.BreathMonitor;

public class OperationMonitor implements Operation {

	private PlotterInterface plotter;

	public OperationMonitor(PlotterInterface plotter) {
		this.plotter = plotter;
	}

	private static String getFileName(final StringTokenizer commandTokenizer,
			final String line) {
		if (!commandTokenizer.hasMoreTokens()) {
			System.out.println("\nusage: monitor -f filname");
			return null;
		}
		if (!commandTokenizer.nextToken().startsWith("-f")) {
			System.out.println("\nusage: monitor -f filname");
			return null;
		}
		if (!commandTokenizer.hasMoreTokens()) {
			System.out.println("\nusage: monitor -f filname");
			return null;
		}
		final String fileName = line.substring(line.indexOf("-f") + 2);
		return fileName;
	}

	@Override
	public void callOp(final StringTokenizer commandTokenizer, final String line) {
		try {
			String fileName = getFileName(commandTokenizer, line);
			if (fileName == null) {
				return;
			}
			fileName = fileName.trim();
			if (!fileName.startsWith("/") && !fileName.startsWith("\\")
					&& !fileName.startsWith(".")) {
				fileName = "./" + fileName.trim();
			}
			final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
					WavFile.openWavFile(new File(fileName)));
			ConsoleIO consoleIO = new ConsoleIO();
			final BreathMonitor breathMonitor = new BreathMonitor(
					wavFileDoubleInputStream, AudioSystem.getAudioInputStream(
							new File(fileName)).getFormat(), fileName, false,
					plotter, consoleIO, consoleIO);
			ConsoleUserInterface.breathMonitorThread = new Thread(breathMonitor);
			ConsoleUserInterface.breathMonitorThread.run();
			wavFileDoubleInputStream.close();
		} catch (final FileNotFoundException e) {
			System.out.println(e.getLocalizedMessage());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
