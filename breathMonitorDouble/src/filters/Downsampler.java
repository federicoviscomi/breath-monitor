package filters;

/*
 * Copyright 2007 Sun Microsystems, Inc.
 *
 * This file is part of jVoiceBridge.
 *
 * jVoiceBridge is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation and distributed hereunder
 * to you.
 *
 * jVoiceBridge is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Sun designates this particular file as subject to the "Classpath"
 * exception as provided by Sun in the License file that accompanied this
 * code.
 */

import java.io.IOException;

/**
 * Down sample
 */
public class Downsampler extends Resampler implements DoubleInputStream {
	private long totalTime;
	private int resampleCount;
	private final DoubleInputStream in;

	/*
	 * XXX We only support big endian 16 bit samples!
	 */
	public Downsampler(final DoubleInputStream in, final String id,
			final int inSampleRate, final int inChannels,
			final int outSampleRate, final int outChannels) throws IOException {

		super(id, inSampleRate, inChannels, outSampleRate, outChannels);
		this.in = in;

		if (inSampleRate < outSampleRate) {
			throw new IOException("Downsampler inSampleRate " + inSampleRate
					+ " < outSampleRate " + outSampleRate);
		}
		if (Logger.logLevel >= Logger.LOG_MOREINFO) {
			Logger.println("New DownSampler:  from " + inSampleRate + "/"
					+ inChannels + " to " + outSampleRate + "/" + outChannels);
		}
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	private double[] downsample(final double[] inSamples, final int off,
			final int len) {
		final int nFrames = len / outChannels;

		int sampleTime = (nFrames * 1000) / inSampleRate;

		if (sampleTime == 0) {
			sampleTime = 1;
		}

		int outLength = ((sampleTime * outSampleRate * outChannels) / 1000);

		if ((outLength & 1) != 0) {
			outLength++;
		}

		if ((outLength == 0) || (Logger.logLevel == -9)) {
			Logger.println("downsample:  inLength " + len + " nFrames "
					+ nFrames + " sampleTime " + sampleTime + " outLength "
					+ outLength);
		}

		final double[] outSamples = new double[outLength];

		final double frameIncr = (double) inSampleRate / (double) outSampleRate;

		int ix;

		double i = 0;

		int outIx = 0;

		if (Logger.logLevel == -9) {
			Logger.println("downsample frameIncr " + frameIncr + " nFrames "
					+ nFrames + " inLength " + len + " outLength " + outLength);

			Logger.println("inSamples");
		}

		/*
		 * Linear interpolation between the two closest samples.
		 */
		while (true) {
			ix = (int) i * outChannels;

			if (ix >= (len - outChannels)) {
				// Don't we need to continue until outIx >= outLength?
				if (Logger.logLevel == -9) {
					Logger.println("Out of here!  ix " + ix + " outIx " + outIx);
				}
				break;
			}

			double s1 = inSamples[ix + off];
			double s2 = inSamples[ix + outChannels + off];

			if (Logger.logLevel == -9) {
				Logger.println("s1 " + s1 + " s2 " + s2 + " int i " + (int) i
						+ " ix " + ix + " outIx " + outIx);
			}

			outSamples[outIx] = (int) ((s1 + ((s2 - s1) * (i - (int) i))));

			outIx++;

			if (outChannels == 2) {
				ix++;

				s1 = inSamples[ix + off];
				s2 = inSamples[ix + outChannels + off];

				outSamples[outIx] = (int) ((s1 + ((s2 - s1) * (i - (int) i))));

				if (Logger.logLevel == -9) {
					Logger.println("+s1 " + s1 + " s2 " + s2 + " int i "
							+ (int) i + " ix " + ix + " outIx " + outIx);
				}

				outIx++;
			}

			if (outIx >= outLength) {
				if (Logger.logLevel == -9) {
					Logger.println("Out of here!  outIX " + outIx + " ix " + ix);
				}
				break;
			}

			i += frameIncr;
		}

		if (Logger.logLevel == -9) {
			Logger.logLevel = 3;

			Logger.println("downsample in len " + len);
			Logger.println("downsample out len " + outSamples.length);
		}

		return outSamples;
	}

	public int[] downsample(final int[] inSamples) {
		final int nFrames = inSamples.length / outChannels;

		int sampleTime = (nFrames * 1000) / inSampleRate;

		if (sampleTime == 0) {
			sampleTime = 1;
		}

		int outLength = ((sampleTime * outSampleRate * outChannels) / 1000);

		if ((outLength & 1) != 0) {
			outLength++;
		}

		if ((outLength == 0) || (Logger.logLevel == -9)) {
			Logger.println("downsample:  inLength " + inSamples.length
					+ " nFrames " + nFrames + " sampleTime " + sampleTime
					+ " outLength " + outLength);
		}

		final int[] outSamples = new int[outLength];

		final double frameIncr = (double) inSampleRate / (double) outSampleRate;

		int ix;

		double i = 0;

		int outIx = 0;

		if (Logger.logLevel == -9) {
			Logger.println("downsample frameIncr " + frameIncr + " nFrames "
					+ nFrames + " inLength " + inSamples.length + " outLength "
					+ outLength);

			Logger.println("inSamples");
		}

		/*
		 * Linear interpolation between the two closest samples.
		 */
		while (true) {
			ix = (int) i * outChannels;

			if (ix >= (inSamples.length - outChannels)) {
				// Don't we need to continue until outIx >= outLength?
				if (Logger.logLevel == -9) {
					Logger.println("Out of here!  ix " + ix + " outIx " + outIx);
				}
				break;
			}

			int s1 = inSamples[ix];
			int s2 = inSamples[ix + outChannels];

			if (Logger.logLevel == -9) {
				Logger.println("s1 " + s1 + " s2 " + s2 + " int i " + (int) i
						+ " ix " + ix + " outIx " + outIx);
			}

			outSamples[outIx] = (int) ((s1 + ((s2 - s1) * (i - (int) i))));

			outIx++;

			if (outChannels == 2) {
				ix++;

				s1 = inSamples[ix];
				s2 = inSamples[ix + outChannels];

				outSamples[outIx] = (int) ((s1 + ((s2 - s1) * (i - (int) i))));

				if (Logger.logLevel == -9) {
					Logger.println("+s1 " + s1 + " s2 " + s2 + " int i "
							+ (int) i + " ix " + ix + " outIx " + outIx);
				}

				outIx++;
			}

			if (outIx >= outLength) {
				if (Logger.logLevel == -9) {
					Logger.println("Out of here!  outIX " + outIx + " ix " + ix);
				}
				break;
			}

			i += frameIncr;
		}

		if (Logger.logLevel == -9) {
			Logger.logLevel = 3;

			Logger.println("downsample in len " + inSamples.length);
			Logger.println("downsample out len " + outSamples.length);
		}

		return outSamples;
	}

	@Override
	public void printStatistics() {
		if (resampleCount == 0) {
			return;
		}

		double avg = (double) totalTime / resampleCount;

		final long timeUnitsPerSecond = CurrentTime.getTimeUnitsPerSecond();

		avg = (avg / timeUnitsPerSecond) * 1000;

		String s = "";

		if (id != null) {
			s += "Call " + id + ":  ";
		}

		Logger.writeFile(s + avg + "ms avg downsample time from "
				+ inSampleRate + "/" + inChannels + " to " + outSampleRate
				+ "/" + outChannels);

		lowPassFilter.printStatistics();
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		final int read = in.read(b, off, len);

		// double[] downSampledData = this.downsample(b, off, read);
		// System.arraycopy(downSampledData, 0, b, off, downSampledData.length);
		// return downSampledData.length;

		final int[] data = new int[read];
		for (int i = 0; i < read; i++) {
			data[i] = (int) b[i + off];
		}
		final int[] downSampled = this.downsample(data);
		for (int i = 0; i < downSampled.length; i++) {
			b[i + off] = downSampled[i];
		}
		return downSampled.length;
	}

	@Override
	public byte[] resample(final byte[] inSamples, final int offset, int length)
			throws IOException {

		length = length & ~1; // round down

		int[] ints = new int[length / 2];

		AudioConversion.bytesToInts(inSamples, offset, length, ints);

		ints = resample(ints);

		final byte[] bytes = new byte[ints.length * 2];

		AudioConversion.intsToBytes(ints, bytes, offset);

		return bytes;
	}

	public double[] resample(final double[] inSamples, final int off,
			final int len) throws IOException {
		if ((inSampleRate == outSampleRate) && (inChannels == outChannels)) {
			return inSamples;
		}
		resampleCount++;
		final long start = CurrentTime.getTime();
		double[] outSamples = reChannel(inSamples, off, len);
		if (inSampleRate == outSampleRate) {
			return outSamples; // no need to resample
		}
		outSamples = lowPassFilter.lpf(outSamples, off, len);

		outSamples = downsample(outSamples, off, len);

		totalTime += (CurrentTime.getTime() - start);

		return outSamples;
	}

	@Override
	public int[] resample(final int[] inSamples) throws IOException {

		if ((inSampleRate == outSampleRate) && (inChannels == outChannels)) {
			return inSamples;
		}

		resampleCount++;

		final long start = CurrentTime.getTime();

		int[] outSamples = reChannel(inSamples);

		if (inSampleRate == outSampleRate) {
			return outSamples; // no need to resample
		}

		outSamples = lowPassFilter.lpf(outSamples);

		outSamples = downsample(outSamples);

		totalTime += (CurrentTime.getTime() - start);

		return outSamples;
	}

	@Override
	public void reset() {
	}

}