/*
	Copyright (c) 2009-2011
		Speech Group at Informatik 5, Univ. Erlangen-Nuremberg, GERMANY
		Korbinian Riedhammer
		Tobias Bocklet

	This file is part of the Java Speech Toolkit (JSTK).

	The JSTK is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.

	The JSTK is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with the JSTK. If not, see <http://www.gnu.org/licenses/>.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Use the IOUtil methods to simplify the binary input/output handling of byte,
 * int, short, float and double values and arrays
 * 
 * @author sikoried
 */
public final class IOUtil {
	/**
	 * Read a single byte from the InputStream
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte readByte(final InputStream is) throws IOException {
		final byte[] buf = new byte[1];
		if (is.read(buf) != 1) {
			throw new IOException("could not read requested byte");
		}
		return buf[0];
	}

	/**
	 * Read a byte array from the InputStream
	 * 
	 * @param is
	 * @param buf
	 * @return false if the buffer could not be filled
	 * @throws IOException
	 */
	public static boolean readByte(final InputStream is, final byte[] buf)
			throws IOException {
		final int read = is.read(buf);
		return read == buf.length;
	}

	/**
	 * Read len bytes from the InputStream
	 * 
	 * @param is
	 * @param buf
	 * @param len
	 * @return false if less bytes read than requested
	 * @throws IOException
	 */
	public static boolean readByte(final InputStream is, final byte[] buf,
			final int len) throws IOException {
		final int read = is.read(buf, 0, len);
		return read == len;
	}

	/**
	 * Reads doubles from ASCII stream
	 * 
	 * @param br
	 *            allocated BufferedReader
	 * @param buf
	 *            (output) buffer
	 * @return true on success, false else.
	 * @throws IOException
	 */
	public static boolean readDouble(final BufferedReader br, final double[] buf)
			throws IOException {
		final String line = br.readLine();

		if (line == null) {
			return false;
		}

		final String[] tokens = line.trim().split("\\s+");

		if (tokens.length != buf.length) {
			return false;
		}

		for (int i = 0; i < tokens.length; ++i) {
			buf[i] = Double.valueOf(tokens[i]);
		}

		return true;
	}

	/**
	 * Read a single Double from the InputStream
	 * 
	 * @param is
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static double readDouble(final InputStream is, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[Double.SIZE / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			throw new IOException("could not read required bytes");
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		return bb.getDouble();
	}

	/**
	 * Reads double array from a binary InputStream
	 * 
	 * @param is
	 *            binary InputStream
	 * @param buf
	 *            (output) buffer
	 * @return true on success, false else.
	 * @throws IOException
	 */
	public static boolean readDouble(final InputStream is, final double[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Double.SIZE) / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			return false;
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getDouble();
		}

		return true;
	}

	/**
	 * Reads float array from ASCII stream
	 * 
	 * @param br
	 *            allocated BufferedReader
	 * @param buf
	 *            (output) buffer
	 * @return true on success, false else.
	 * @throws IOException
	 */
	public static boolean readFloat(final BufferedReader br, final float[] buf)
			throws IOException {
		final String line = br.readLine();

		if (line == null) {
			return false;
		}

		final String[] tokens = line.trim().split("\\s+");

		if (tokens.length != buf.length) {
			return false;
		}

		for (int i = 0; i < tokens.length; ++i) {
			buf[i] = Float.valueOf(tokens[i]);
		}

		return true;
	}

	/**
	 * Read a single Float from the InputStream using given ByteOrder
	 * 
	 * @param is
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static float readFloat(final InputStream is, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[Float.SIZE / 8];
		final int read = is.read(bbuf);

		// complete frame?
		if (read < bbuf.length) {
			throw new IOException("could not read required bytes");
		}

		// decode the double
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		return bb.getFloat();
	}

	/**
	 * Reads floats from a binary InputStream and store them in the double
	 * buffer using given ByteOrder
	 * 
	 * @param is
	 *            binary InputStream
	 * @param buf
	 *            (output) buffer
	 * @return true on success, false else.
	 * @throws IOException
	 */
	public static boolean readFloat(final InputStream is, final double[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Float.SIZE) / 8];
		final int read = is.read(bbuf);

		// complete frame?
		if (read < bbuf.length) {
			return false;
		}

		// decode the double
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getFloat();
		}

		return true;
	}

	public static int readFloat(final InputStream in, final double[] buf,
			final int off, final int length, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[(length * Float.SIZE) / 8];
		in.read(bbuf);

		// decode the double
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);
		bb.asFloatBuffer();

		int i = off;
		for (; (i < (off + length)) && bb.hasRemaining(); ++i) {
			buf[i] = bb.getFloat();
		}

		return i - off;
	}

	/**
	 * Reads float array from a InputStream using given ByteOrder
	 * 
	 * @param is
	 *            binary InputStream
	 * @param buf
	 *            (output) buffer
	 * @return true on success, false else.
	 * @throws IOException
	 */
	public static boolean readFloat(final InputStream is, final float[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Float.SIZE) / 8];
		final int read = is.read(bbuf);

		// complete frame?
		if (read < bbuf.length) {
			return false;
		}

		// decode the double
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getFloat();
		}

		return true;
	}

	/**
	 * Read a single int from the InputStream using given ByteOrder pointer
	 * respectively.
	 * 
	 * @param is
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static int readInt(final InputStream is, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[Integer.SIZE / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			throw new IOException("could not read required bytes");
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		return bb.getInt();
	}

	/**
	 * Read a single int from the InputStream using given ByteOrder pointer
	 * respectively.
	 * 
	 * @param is
	 * @param buf
	 * @param bo
	 * @return false if frame could not be filled
	 * @throws IOException
	 */
	public static boolean readInt(final InputStream is, final int[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Integer.SIZE) / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			return false;
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getInt();
		}

		return true;
	}

	/**
	 * Read a single long from the InputStream using given ByteOrder pointer
	 * respectively.
	 * 
	 * @param is
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static long readLong(final InputStream is, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[Long.SIZE / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			throw new IOException("could not read required bytes");
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		return bb.getLong();
	}

	/**
	 * Read a long array from the InputStream using given ByteOrder pointer
	 * respectively.
	 * 
	 * @param is
	 * @param buf
	 * @param bo
	 * @return false if frame could not be filled
	 * @throws IOException
	 */
	public static boolean readLong(final InputStream is, final long[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Long.SIZE) / 8];
		final int read = is.read(bbuf);

		if (read < bbuf.length) {
			return false;
		}

		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getLong();
		}

		return true;
	}

	/**
	 * Read a single short value from the given InputStream using given
	 * ByteOrder
	 * 
	 * @param is
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static short readShort(final InputStream is, final ByteOrder bo)
			throws IOException {
		final byte[] bbuf = new byte[Short.SIZE / 8];
		final int read = is.read(bbuf);

		// complete frame?
		if (read < bbuf.length) {
			throw new IOException("could not read required bytes");
		}

		// decode the double
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		return bb.getShort();
	}

	/**
	 * Read an array of shorts from the given InputStream using given ByteOrder
	 * 
	 * @param is
	 * @param buf
	 * @param bo
	 * @return false if buffer could not be filled
	 * @throws IOException
	 */
	public static boolean readShort(final InputStream is, final short[] buf,
			final ByteOrder bo) throws IOException {
		final byte[] bbuf = new byte[(buf.length * Short.SIZE) / 8];
		final int read = is.read(bbuf);

		// complete frame?
		if (read < bbuf.length) {
			return false;
		}

		// decode the short
		final ByteBuffer bb = ByteBuffer.wrap(bbuf);
		bb.order(bo);

		for (int i = 0; i < buf.length; ++i) {
			buf[i] = bb.getShort();
		}

		return true;
	}

	/**
	 * Write a single byte to the OutputStream
	 * 
	 * @param os
	 * @param b
	 * @throws IOException
	 */
	public static void writeByte(final OutputStream os, final byte b)
			throws IOException {
		os.write(new byte[] { b });
	}

	/**
	 * Write a byte array to the OutputStream
	 * 
	 * @param os
	 * @param buf
	 * @throws IOException
	 */
	public static void writeByte(final OutputStream os, final byte[] buf)
			throws IOException {
		os.write(buf);
	}

	/**
	 * Write len bytes to the OutputStream
	 * 
	 * @param os
	 * @param buf
	 * @param len
	 * @throws IOException
	 */
	public static void writeByte(final OutputStream os, final byte[] buf,
			final int len) throws IOException {
		os.write(buf, 0, len);
	}

	/**
	 * Write a given double array to the ASCII stream
	 * 
	 * @param bw
	 * @param buf
	 * @throws IOException
	 */
	public static void writeDouble(final BufferedWriter bw, final double[] buf)
			throws IOException {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < (buf.length - 1); ++i) {
			sb.append(Double.toString(buf[i]) + " ");
		}
		sb.append(Double.toString(buf[buf.length - 1]) + "\n");
		bw.append(sb.toString());
	}

	/**
	 * Write a double to the OutputStream pointer respectively.
	 * 
	 * @param os
	 * @param val
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static void writeDouble(final OutputStream os, final double val,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate(Double.SIZE / 8);
		bb.order(bo);
		bb.putDouble(val);
		os.write(bb.array());
	}

	/**
	 * Write the given double array to the OutputStream using the specified
	 * ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeDouble(final OutputStream os, final double[] buf,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer
				.allocate((buf.length * Double.SIZE) / 8);
		bb.order(bo);
		for (final double d : buf) {
			bb.putDouble(d);
		}
		os.write(bb.array());
	}

	/**
	 * Write a given float array to the ASCII stream
	 * 
	 * @param bw
	 * @param buf
	 * @throws IOException
	 */
	public static void writeFloat(final BufferedWriter bw, final float[] buf)
			throws IOException {
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < (buf.length - 1); ++i) {
			sb.append(Float.toString(buf[i]) + " ");
		}
		sb.append(Float.toString(buf[buf.length - 1]) + "\n");
		bw.append(sb.toString());
	}

	/**
	 * Write the given double array as floats to the OutputStream using the
	 * specified ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeFloat(final OutputStream os, final double[] buf,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer
				.allocate((buf.length * Float.SIZE) / 8);
		bb.order(bo);
		for (final double d : buf) {
			bb.putFloat((float) d);
		}
		os.write(bb.array());
	}

	/**
	 * Write a float to the OutputStream pointer respectively.
	 * 
	 * @param os
	 * @param val
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static void writeFloat(final OutputStream os, final float val,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate(Float.SIZE / 8);
		bb.order(bo);
		bb.putFloat(val);
		os.write(bb.array());
	}

	/**
	 * Write the given float array to the OutputStream using the specified
	 * ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeFloat(final OutputStream os, final float[] buf,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer
				.allocate((buf.length * Float.SIZE) / 8);
		bb.order(bo);
		for (final float f : buf) {
			bb.putFloat(f);
		}
		os.write(bb.array());
	}

	/**
	 * Write an int to the OutputStream and advance the stream pointer
	 * respectively.
	 * 
	 * @param os
	 * @param val
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static void writeInt(final OutputStream os, final int val,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate(Integer.SIZE / 8);
		bb.order(bo);
		bb.putInt(val);
		os.write(bb.array());
	}

	/**
	 * Write the given int array to the OutputStream using given ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeInt(final OutputStream os, final int[] buf,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer
				.allocate((buf.length * Integer.SIZE) / 8);
		bb.order(bo);
		for (final int d : buf) {
			bb.putInt(d);
		}
		os.write(bb.array());
	}

	/**
	 * Write an int to the OutputStream and advance the stream pointer
	 * respectively.
	 * 
	 * @param os
	 * @param val
	 * @param bo
	 * @return
	 * @throws IOException
	 */
	public static void writeLong(final OutputStream os, final long val,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate(Long.SIZE / 8);
		bb.order(bo);
		bb.putLong(val);
		os.write(bb.array());
	}

	/**
	 * Write the given long array to the OutputStream using given ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeLong(final OutputStream os, final long[] buf,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate((buf.length * Long.SIZE) / 8);
		bb.order(bo);
		for (final long d : buf) {
			bb.putLong(d);
		}
		os.write(bb.array());
	}

	/**
	 * Write a single short to the OutputStream pointer respectively.
	 * 
	 * @param os
	 * @param val
	 * @param bo
	 * @throws IOException
	 */
	public static void writeShort(final OutputStream os, final short val,
			final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer.allocate(Short.SIZE / 8);
		bb.order(bo);
		bb.putShort(val);
		os.write(bb.array());
	}

	/**
	 * Write the given short array to the OutputStream using given ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeShort(final OutputStream os, final short[] buf,
			final ByteOrder bo) throws IOException {
		writeShort(os, buf, buf.length, bo);
	}

	/**
	 * Write the given short array to the OutputStream using given ByteOrder
	 * 
	 * @param os
	 * @param buf
	 * @param bo
	 * @throws IOException
	 */
	public static void writeShort(final OutputStream os, final short[] buf,
			final int length, final ByteOrder bo) throws IOException {
		final ByteBuffer bb = ByteBuffer
				.allocate((buf.length * Short.SIZE) / 8);
		bb.order(bo);
		for (int i = 0; i < length; ++i) {
			bb.putShort(buf[i]);
		}
		os.write(bb.array());
	}

	/**
	 * Read a double array from the stream without knowing its size ahead of
	 * time
	 * 
	 * @param br
	 * @return
	 */
	public double[] readDouble(final BufferedReader br) throws IOException {
		final String line = br.readLine();

		if (line == null) {
			return null;
		}

		final String[] tokens = line.trim().split("\\s+");

		final double[] buf = new double[tokens.length];

		for (int i = 0; i < tokens.length; ++i) {
			buf[i] = Double.valueOf(tokens[i]);
		}

		return buf;
	}

	/**
	 * Read a float array from the stream without knowing its size ahead of time
	 * 
	 * @param br
	 * @return float array
	 */
	public float[] readFloat(final BufferedReader br) throws IOException {
		final String line = br.readLine();

		if (line == null) {
			return null;
		}

		final String[] tokens = line.trim().split("\\s+");

		final float[] buf = new float[tokens.length];

		for (int i = 0; i < tokens.length; ++i) {
			buf[i] = Float.valueOf(tokens[i]);
		}

		return buf;
	}

}
