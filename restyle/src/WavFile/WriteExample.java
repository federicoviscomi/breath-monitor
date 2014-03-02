package wavFile;

import java.io.File;
import java.io.IOException;

public class WriteExample {
	public static void main(final String[] args) throws IOException,
			WavFileException {
		final int sampleRate = 44100; // Samples per second
		final double duration = 5.0; // Seconds

		// Calculate the number of frames required for specified duration
		final long numFrames = (long) (duration * sampleRate);

		// Create a wav file with the name specified as the first argument
		final WavFile wavFile = WavFile.newWavFile(new File(args[0]), 2,
				numFrames, 16, sampleRate);

		// Create a buffer of 100 frames
		final double[][] buffer = new double[2][100];

		// Initialise a local frame counter
		long frameCounter = 0;

		// Loop until all frames written
		while (frameCounter < numFrames) {
			// Determine how many frames to write, up to a maximum of the
			// buffer size
			final long remaining = wavFile.getFramesRemaining();
			final int toWrite = (remaining > 100) ? 100 : (int) remaining;

			// Fill the buffer, one tone per channel
			for (int s = 0; s < toWrite; s++, frameCounter++) {
				buffer[0][s] = Math.sin((2.0 * Math.PI * 400 * frameCounter)
						/ sampleRate);
				buffer[1][s] = Math.sin((2.0 * Math.PI * 500 * frameCounter)
						/ sampleRate);
			}

			// Write the buffer
			wavFile.writeFrames(buffer, toWrite);
		}

		// Close the wavFile
		wavFile.close();
	}
}
