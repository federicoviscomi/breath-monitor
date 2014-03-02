package filters;

/*
 WAVOutputStream - writes a WAV file.
 see http://www.mediatel.lu/workshop/audio/fileformat for format spec.
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WavOutputStream extends java.io.FilterOutputStream {
	private static final int RIFFid = ('R' << 24)
			| (('I' << 16) + ('F' << 8) + 'F');
	private static final int WAVEid = ('W' << 24)
			| (('A' << 16) + ('V' << 8) + 'E');
	private static final int fmtid = ('f' << 24)
			| (('m' << 16) + ('t' << 8) + ' ');
	private static final int dataid = ('d' << 24)
			| (('a' << 16) + ('t' << 8) + 'a');

	private final int numChannels;
	// this allows us to write binary data to the stream
	private final DataOutputStream DataOut;

	public WavOutputStream(final OutputStream _out, final int channels,
			final int rate, final int depth, final int length)
			throws IOException, Exception {
		super(_out);

		numChannels = channels;
		DataOut = new DataOutputStream(out);

		// write "RIFF"
		DataOut.writeInt(RIFFid);
		// write chunk size
		writeInt(4 + 4 + 4 + 24 + 4 + 4 + ((channels * depth * length) / 8));
		// read "WAVE"
		DataOut.writeInt(WAVEid);
		// write format chunk
		DataOut.writeInt(fmtid);
		writeInt(16);
		writeShort((short) 1);
		writeShort((short) channels);
		writeInt(rate);
		writeInt((rate * channels * depth) / 8);
		writeShort((short) ((channels * depth) / 8));
		writeShort((short) depth);
		DataOut.writeInt(dataid);
		writeInt((channels * depth * length) / 8);
	}

	public void readSample(final int b[], final int start, final int length)
			throws IOException {
		for (int off = 0; off < length; off++) {
			for (int channel = 0; channel < numChannels; channel++) {
				writeInt(b[channel + ((start + off) * numChannels)]);
			}
		}
	}

	private void writeInt(final int val) throws IOException {
		out.write(val & 0xFF);
		out.write((val >> 8) & 0xFF);
		out.write((val >> 16) & 0xFF);
		out.write((val >> 24) & 0xFF);
	}

	public void writeSample(final byte b[], final int start, final int length)
			throws IOException {
		out.write(b, start * numChannels, length * numChannels);
	}

	public void writeSample(final short b[], final int start, final int length)
			throws IOException {
		for (int off = 0; off < length; off++) {
			for (int channel = 0; channel < numChannels; channel++) {
				writeShort(b[channel + ((start + off) * numChannels)]);
			}
		}
	}

	// for intel byte order. YAY!
	private void writeShort(final short val) throws IOException {
		out.write(val & 0xFF);
		out.write((val >> 8) & 0xFF);
	}
}
