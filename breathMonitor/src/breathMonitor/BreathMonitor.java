package breathMonitor;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import beatFinder.BeatTokenizerStream;

public class BreathMonitor implements Runnable {
	/**
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws InterruptedException
	 */
	public static void main(String[] args)
			throws UnsupportedAudioFileException, IOException,
			InterruptedException {
		final String[] testFiles = {
				"/home/federico/tesi/Coarse crackles.wav",
				"/home/federico/tesi/Inspiratory stridor.wav",
				"/home/federico/tesi/MurmureVescicolare-Normal vesicular.wav",
				"/home/federico/tesi/Normal Split S1.wav",
				"/home/federico/tesi/Normal Split Second Sound.wav",

				"/home/federico/tesi/Normal vesicular.wav",

				"/home/federico/tesi/Normal.wav",
				"/home/federico/tesi/Pleural friction.wav",
				"/home/federico/tesi/RantoliGrossolani-Coarse crackles.wav",
				"/home/federico/tesi/SfregamentoPleurico-Pleural friction.wav",
				"/home/federico/tesi/Sibilo-Wheezing.wav",
				"/home/federico/tesi/StridoreInspiratorio-Inspiratory stridor.wav",
		//"/home/federico/tesi/Track 1.wav"
		};
		for (final String testFile : testFiles) {
			System.out.println("\n\n\n" + testFile);
			new BreathMonitor(
					AudioSystem.getAudioInputStream(new File(testFile))).run();
		}
	}

	public int bufferSize;

	private final AudioInputStream audioInputStream;
	private final AudioFormat format;
	private byte[] buffer;
	private final BeatTokenizerStream beatTokenizerStream;
	private final BreathAnalizer breathAnalizer;

	public BreathMonitor(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
		format = audioInputStream.getFormat();

		// this should be 10ms buffer size
		bufferSize = ((int) Math.ceil(format.getFrameRate()
				* format.getFrameSize())) / 100;

		// find the minimum of the set of power of two that are greater than
		// bufferSize = ((java.lang.Integer.highestOneBit((int)
		// Math.ceil(format.getFrameRate() * format.getFrameSize()))) << 1);
		buffer = new byte[bufferSize];
		// beatTokenizerStream = new
		// BeatTokenizerStream(audioInputStream.getFormat(), bufferSize);
		beatTokenizerStream = new BeatTokenizerStream(format, bufferSize);
		breathAnalizer = new BreathAnalizer(audioInputStream.getFormat());
	}

	@Override
	public void run() {
		try {
			int read = 0;
			while (!Thread.interrupted()
					&& (read = audioInputStream.read(buffer)) > 0) {

				beatTokenizerStream.write(buffer, 0, read);

				// TODO check for beat per second consistency

				if (beatTokenizerStream.hasNext()) {
					if (breathAnalizer.isABreaht(beatTokenizerStream.next())) {
						// System.out.println("breathing");
					} else {
						System.out.println("not breathing");
					}
				}
			}
			beatTokenizerStream.close();
			audioInputStream.close();
			breathAnalizer.shutdown();
			buffer = null;
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
