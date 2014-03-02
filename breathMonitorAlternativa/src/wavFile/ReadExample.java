package wavFile;

import java.io.File;

import plot.Plotter;
import breathMonitor.BreathMonitor;

public class ReadExample {
	public static void main(final String[] args) {
		try {
			// Open the wav file specified as the first argument
			final WavFile wavFile = WavFile.openWavFile(new File(
					"/home/federico/tesi/Normal.wav"));
			final Plotter plotter = new Plotter();
			// Display information about the wav file
			wavFile.display();

			// Get the number of audio channels in the wav file
			final int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames
			final double[] buffer = new double[100 * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			int totalFrameRead = 0;
			do {
				// Read frames into buffer
				framesRead = wavFile.readFrames(buffer, 100);
				totalFrameRead = totalFrameRead + framesRead;
				plotter.plotLine(
						BreathMonitor.linspace(buffer.length, totalFrameRead
								* buffer.length, 1), buffer);
				System.err.println(java.util.Arrays.toString(buffer));
				// Loop through frames and look for minimum and maximum value
				for (int s = 0; s < (framesRead * numChannels); s++) {
					if (buffer[s] > max) {
						max = buffer[s];
					}
					if (buffer[s] < min) {
						min = buffer[s];
					}
				}
			} while (framesRead != 0);

			// Close the wavFile
			wavFile.close();

			// Output the minimum and maximum value
			System.out.printf("Min: %f, Max: %f\n", min, max);
		} catch (final Exception e) {
			System.err.println(e);
		}
	}
}
