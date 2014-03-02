package filters;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;

/**
 * The class represents Gabor Filter implementation
 * 
 * @author Alexander Jipa
 * @version 0.2, 09/03/2011
 */
public class GaborFilter {
	private static final double[] DEFAULT_ORIENTATIONS = new double[] { 0 };
	private static final double DEFAULT_WAVE_LENGTH = 1;
	private static final double DEFAULT_PHASE_OFFSET = 0;
	private static final double DEFAULT_ASPECT_RATIO = 0.5;
	private static final double DEFAULT_BANDWIDTH = 1;
	private static final int DEFAULT_WIDTH = 3;
	private static final int DEFAULT_HEIGHT = 3;

	private static final double MIN_ASPECT_RATIO = 0;
	private static final double MAX_ASPECT_RATIO = 1;

	/**
	 * Calculates the Sigma for the given Wave Length and Bandwidth
	 * 
	 * @param waveLength
	 *            - Wave Length
	 * @param bandwidth
	 *            - Bandwidth
	 * @return - Sigma (Deviation)
	 */
	private static double calculateSigma(final double waveLength,
			final double bandwidth) {
		return (waveLength * Math.sqrt(Math.log(2) / 2) * (Math.pow(2,
				bandwidth) + 1)) / ((Math.pow(2, bandwidth) - 1) * Math.PI);
	}

	/**
	 * Calculates Gabor function value for the given data
	 * 
	 * @param x
	 *            - X
	 * @param y
	 *            - Y
	 * @param sigma
	 *            - Sigma
	 * @param aspectRatio
	 *            - Aspect Ratio
	 * @param waveLength
	 *            - Wave Length
	 * @param phaseOffset
	 *            - Phase Offset
	 * @return - Gabor function value
	 */
	private static double gaborFunction(final double x, final double y,
			final double sigma, final double aspectRatio,
			final double waveLength, final double phaseOffset) {
		final double gaborReal = Math.exp(-(Math.pow(x / sigma, 2) + Math.pow(
				(y * aspectRatio) / sigma, 2)) / 2)
				* Math.cos(((2 * Math.PI * x) / waveLength) + phaseOffset);
		final double gaborImage = Math.exp(-(Math.pow(x / sigma, 2) + Math.pow(
				(y * aspectRatio) / sigma, 2)) / 2)
				* Math.sin(((2 * Math.PI * x) / waveLength) + phaseOffset);
		return Math.sqrt(Math.pow(gaborReal, 2) + Math.pow(gaborImage, 2));
	}

	private double[] orientations;
	private double waveLength;
	private double phaseOffset;
	private double aspectRatio;
	private double bandwidth;

	private int width;

	private int height;

	/**
	 * Default constructor
	 */
	public GaborFilter() {
		this(DEFAULT_WAVE_LENGTH);
	}

	public GaborFilter(final double waveLength) {
		this(waveLength, DEFAULT_ORIENTATIONS);
	}

	public GaborFilter(final double waveLength, final double[] orientations) {
		this(waveLength, orientations, DEFAULT_PHASE_OFFSET);
	}

	public GaborFilter(final double waveLength, final double[] orientations,
			final double phaseOffset) {
		this(waveLength, orientations, phaseOffset, DEFAULT_ASPECT_RATIO);
	}

	public GaborFilter(final double waveLength, final double[] orientations,
			final double phaseOffset, final double aspectRatio) {
		this(waveLength, orientations, phaseOffset, aspectRatio,
				DEFAULT_BANDWIDTH);
	}

	public GaborFilter(final double waveLength, final double[] orientations,
			final double phaseOffset, final double aspectRatio,
			final double bandwidth) {
		this(waveLength, orientations, phaseOffset, aspectRatio, bandwidth,
				DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}

	public GaborFilter(final double waveLength, final double[] orientations,
			final double phaseOffset, final double aspectRatio,
			final double bandwidth, final int width, final int height) {
		this.waveLength = waveLength;
		this.orientations = orientations;
		this.phaseOffset = phaseOffset;
		this.aspectRatio = aspectRatio;
		this.bandwidth = bandwidth;
		this.width = width;
		this.height = height;
	}

	/**
	 * Filters the bufferedImage using the Gabor filter. If the
	 * bufferedImageDestination is not null the bufferedImage is used as the
	 * destination
	 * 
	 * @param bufferedImage
	 *            - buffered image to be used as the source
	 * @param bufferedImageDestination
	 *            - buffered image to be used as the destination
	 * @return - the rendered image
	 */
	public RenderedImage filter(final BufferedImage bufferedImage,
			final BufferedImage bufferedImageDestination) {
		return getConvolveOp().filter(bufferedImage, bufferedImageDestination);
	}

	/**
	 * Gets the Aspect Ratio
	 * 
	 * @return - Aspect Ratio
	 */
	public double getAspectRatio() {
		return aspectRatio;
	}

	/**
	 * Gets the Bandwidth
	 * 
	 * @return - Bandwidth
	 */
	public double getBandwidth() {
		return bandwidth;
	}

	/**
	 * Returns the ConvolveOp for the Gabor Filter
	 * 
	 * @return - ConvolveOp
	 */
	public ConvolveOp getConvolveOp() {
		return new ConvolveOp(getKernel(), ConvolveOp.EDGE_NO_OP, null);
	}

	/**
	 * Gets the Height
	 * 
	 * @return - Height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the Kernel for the Gabor filter
	 * 
	 * @return - Kernel
	 */
	public Kernel getKernel() {
		final double sigma = calculateSigma(waveLength, bandwidth);
		final float[] data = new float[width * height];
		for (int k = 0, x = -width / 2; x <= (width / 2); x++) {
			for (int y = -height / 2; y <= (height / 2); y++) {
				for (final double orientation : orientations) {
					final double x1 = (x * Math.cos(orientation))
							+ (y * Math.sin(orientation));
					final double y1 = (-x * Math.sin(orientation))
							+ (y * Math.cos(orientation));
					data[k] += (float) (gaborFunction(x1, y1, sigma,
							aspectRatio, waveLength, phaseOffset));
				}
				k++;
			}
		}
		float sum = 0f;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				sum += data[(i * j) + j];
			}
		}
		sum /= width * height;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				data[(i * j) + j] -= sum;
			}
		}
		return new Kernel(width, height, data);
	}

	/**
	 * Gets the Orientations array
	 * 
	 * @return - an array of Orientations
	 */
	public double[] getOrientations() {
		return orientations;
	}

	/**
	 * Gets the Phase Offset
	 * 
	 * @return - Phase Offset
	 */
	public double getPhaseOffset() {
		return phaseOffset;
	}

	/**
	 * Gets the Wave Length
	 * 
	 * @return - Wave Length
	 */
	public double getWaveLength() {
		return waveLength;
	}

	/**
	 * Gets the Width
	 * 
	 * @return - Width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the Aspect Ratio
	 * 
	 * @param aspectRatio
	 *            - a new Aspect Ratio
	 */
	public void setAspectRatio(final double aspectRatio) {
		if ((aspectRatio <= MAX_ASPECT_RATIO)
				&& (aspectRatio >= MIN_ASPECT_RATIO)) {
			this.aspectRatio = aspectRatio;
		} else {
			System.out.println("The Aspect Ratio should be in the range ["
					+ MIN_ASPECT_RATIO + "; " + MAX_ASPECT_RATIO + "]");
		}
	}

	/**
	 * Sets the Bandwidth
	 * 
	 * @param bandwidth
	 *            - a new Bandwidth
	 */
	public void setBandwidth(final double bandwidth) {
		this.bandwidth = bandwidth;
	}

	/**
	 * Sets the Height
	 * 
	 * @param height
	 *            - a new Height
	 */
	public void setHeight(final int height) {
		this.height = height;
	}

	/**
	 * Sets the Orientations array
	 * 
	 * @param orientations
	 *            - a new Orientations array
	 */
	public void setOrientations(final double[] orientations) {
		this.orientations = orientations;
	}

	/**
	 * Sets the Phase Offset
	 * 
	 * @param phaseOffset
	 *            - a new Phase Offset
	 */
	public void setPhaseOffset(final double phaseOffset) {
		this.phaseOffset = phaseOffset;
	}

	/**
	 * Sets the Wave Length
	 * 
	 * @param waveLength
	 *            - a new Wave Length
	 */
	public void setWaveLength(final double waveLength) {
		if (waveLength > 0) {
			this.waveLength = waveLength;
		} else {
			System.out.println("The Wave Length should be a positive number");
		}
	}

	/**
	 * Sets the Width
	 * 
	 * @param width
	 *            - a new Width
	 */
	public void setWidth(final int width) {
		this.width = width;
	}
}