package wavFile;

import java.awt.Color;
import java.io.File;

import plot.DiscreteTimePlotter;
import util.Util;
import filters.DoubleInputStream;

public class ReadExample {
	public static void main(final String[] args) {
		try {
			// Open the wav file specified as the first argument
			final WavFile wavFile = WavFile.openWavFile(new File(
					"/home/federico/tesi/Normal.wav"));
			final DiscreteTimePlotter plotter = new DiscreteTimePlotter("");
			// Display information about the wav file
			wavFile.display();

			final DoubleInputStream dis = new WavFileDoubleInputStream(wavFile);
			// dis = new AbsoluteValueFilterInputStream(dis);
			final int bufferSize = 100;
			// dis = new BandPassFilterJSTK(dis, 50, 2500, 8000, bufferSize);

			// Get the number of audio channels in the wav file
			final int numChannels = wavFile.getNumChannels();

			// Create a buffer of 100 frames

			final double[] buffer = new double[bufferSize * numChannels];

			int framesRead;
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			int totalFrameRead = 0;

			do {
				// Read frames into buffer
				framesRead = dis.read(buffer, 0, bufferSize);
				totalFrameRead = totalFrameRead + framesRead;
				plotter.plotLines(
						Util.linspace(buffer.length, totalFrameRead
								* buffer.length, 1), buffer, Color.BLACK);
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
