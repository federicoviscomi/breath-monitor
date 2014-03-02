package breathMonitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import plot.Plotter;
import wavFile.WavFileException;
import filters.AbsoluteValueInputStream;
import filters.DownsampleFilterInputStream;
import filters.FiniteImpulseResponseBandapassFilterInputStream;
import filters.LowPassFilter;
import filters.MagnitudeInputStream;

public class BreathMonitor implements Runnable {

	private static double[] byteToDoubleArray(final byte[] byteArray,
			final int offset, final int length) {
		final double[] doubleArray = new double[length];
		for (int i = 0; i < length; i++) {
			doubleArray[i] = byteArray[i + offset];
		}
		return doubleArray;
	}

	public static double[] linspace(final int length, final double start,
			final double increment) {
		final double[] data = new double[length];
		double value = start;
		for (int i = 0; i < data.length; i++) {
			data[i] = value;
			value = value + increment;
		}
		return data;
	}

	public static void main(final String[] args)
			throws UnsupportedAudioFileException, IOException, WavFileException {
		final String[] allTestFilesName = {
		// "/home/federico/tesi/Coarse crackles.wav",
		// "/home/federico/tesi/Inspiratory stridor.wav",
		// "/home/federico/tesi/MurmureVescicolare-Normal vesicular.wav",
		// "/home/federico/tesi/Normal Split S1.wav",
		// "/home/federico/tesi/Normal Split Second Sound.wav",
		// "/home/federico/tesi/Normal vesicular.wav",
		"/home/federico/tesi/Normal.wav"// ,
		// "/home/federico/tesi/Pleural friction.wav",
		// "/home/federico/tesi/RantoliGrossolani-Coarse crackles.wav",
		// "/home/federico/tesi/SfregamentoPleurico-Pleural friction.wav",
		// "/home/federico/tesi/Sibilo-Wheezing.wav",
		// "/home/federico/tesi/StridoreInspiratorio-Inspiratory stridor.wav",
		// "/home/federico/tesi/Track 1.wav"
		};
		for (final String nextTestFileName : allTestFilesName) {
			System.out.println("\n\n\n" + nextTestFileName);
			new BreathMonitor(AudioSystem.getAudioInputStream(new File(
					nextTestFileName))).run();
		}
	}

	public int bufferSize;
	private InputStream inputStream;

	private final AudioFormat format;

	private byte[] buffer;

	private final BreathAnalizer breathAnalizer;

	private final Plotter plotter;

	public BreathMonitor(final AudioInputStream audioInputStream) {
		if (audioInputStream.getFormat().getFrameSize() != 1) {
			throw new Error();
		}
		// aggiungere degli smoothing filters ad esempio gaussain smoothing
		inputStream = audioInputStream;
		// inputStream = new ByteToFloatInputStream(inputStream);
		// inputStream = new
		// FiniteImpulseResponseBandapassFilterInputStream(inputStream, 100,
		// 2500);
		//inputStream = new MagnitudeInputStream(inputStream);
		// inputStream = new AbsoluteValueInputStream(inputStream);
		// inputStream = new DownsampleFilterInputStream(inputStream, 20);
		// inputStream = new BandPassFilter(inputStream, 1, 20000);
		// inputStream = new LowPassFilter(inputStream, 2000);
		format = audioInputStream.getFormat();
		System.err.println("frame size "
				+ audioInputStream.getFormat().getFrameSize());
		bufferSize = 3 * ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize()));

		// find the minimum of the set of power of two that are greater than
		// bufferSize = ((java.lang.Integer.highestOneBit((int)
		// Math.ceil(format.getFrameRate() * format.getFrameSize()))) << 1);

		buffer = new byte[bufferSize];
		breathAnalizer = new BreathAnalizer(format);
		plotter = new Plotter();
	}

	@Override
	public void run() {
		try {
			/*
			 * int bytesPerFrame = audioInputStream.getFormat().getFrameSize();
			 * if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) { // some audio
			 * formats may have unspecified frame size // in that case we may
			 * read any amount of bytes bytesPerFrame = 1; } // Set an arbitrary
			 * buffer size of 1024 frames. int numBytes = 1024 * bytesPerFrame;
			 * byte[] audioBytes = new byte[numBytes];
			 * 
			 * int numBytesRead = 0; int numFramesRead = 0; int totalFramesRead
			 * = 0; // Try to read numBytes bytes from the file. while
			 * ((numBytesRead = audioInputStream.read(audioBytes)) != -1) { //
			 * Calculate the number of frames actually read. numFramesRead =
			 * numBytesRead / bytesPerFrame; totalFramesRead = totalFramesRead +
			 * numFramesRead; // Here, do something useful with the audio data
			 * that's // now in the audioBytes array...
			 * 
			 * }
			 */
			int read = 0;
			// int totalRead = 0;
			double start = 0;
			while (!Thread.interrupted()
					&& ((read = inputStream.read(buffer)) > 0)) {
				// totalRead = totalRead + read;
				breathAnalizer.detect(buffer, 0, read);
				plotter.plotPoints(linspace(read, start, 0.5),
						byteToDoubleArray(buffer, 0, read));
				// if (totalRead >= (13 * format.getFrameSize() *
				// format.getFrameRate())) {
				// plotter = new Plotter();
				// }
				start = start + (0.5 * read);
				if (!breathAnalizer.isBreathing()) {
					System.out.println("not breathing");
				}
			}
			inputStream.close();
			buffer = null;
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
