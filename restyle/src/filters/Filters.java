package filters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Filters {

	enum filterType {
		LOW_PASS, HIGH_PASS, BAND_PASS, BAND_STOP
	};

	enum windowType {
		RECTANGULAR, BARTLETT, HANNING, HAMMING, BLACKMAN
	};

	// Transition Width (transWidth) is given in Hz
	// Sampling Frequency (sampFreq) is given in Hz
	// Window Length (windowLength) will be set
	public static int calculateKaiserParams(final double ripple,
			final double transWidth, final double sampFreq, final double[] beta) {
		// Calculate delta w
		final double dw = (2 * Math.PI * transWidth) / sampFreq;

		// Calculate ripple dB
		final double a = -20.0 * Math.log10(ripple);

		// Calculate filter order
		int m;
		if (a > 21) {
			m = (int) Math.ceil((a - 7.95) / (2.285 * dw));
		} else {
			m = (int) Math.ceil(5.79 / dw);
		}

		if (a <= 21) {
			beta[0] = 0.0;
		} else if (a <= 50) {
			beta[0] = (0.5842 * Math.pow(a - 21, 0.4)) + (0.07886 * (a - 21));
		} else {
			beta[0] = 0.1102 * (a - 8.7);
		}
		return m + 1;
	}

	// Create Math.sinc function for filter with 1 transition - Low and High
	// pass filters
	public static double[] create1TransSinc(final int windowLength,
			final double transFreq, final double sampFreq, final filterType type) {
		int n;

		// Allocate memory for the window
		final double[] window = new double[windowLength];

		if ((type != filterType.LOW_PASS) && (type != filterType.HIGH_PASS)) {
			System.err
					.println("create1TransSinc: Bad filter type, should be either LOW_PASS of HIGH_PASS\n");
			return null;
		}

		// Calculate the normalised transistion frequency. As transFreq should
		// be
		// less than or equal to sampFreq / 2, ft should be less than 0.5
		double ft = transFreq / sampFreq;

		final double m_2 = 0.5 * (windowLength - 1);
		final int halfLength = windowLength / 2;

		// Set centre tap, if present
		// This avoids a divide by zero
		if ((2 * halfLength) != windowLength) {
			double val = 2.0 * ft;

			// If we want a high pass filter, subtract Math.sinc function from a
			// dirac pulse
			if (type == filterType.HIGH_PASS) {
				val = 1.0 - val;
			}

			window[halfLength] = val;
		} else if (type == filterType.HIGH_PASS) {
			System.err
					.println("create1TransSinc: For high pass filter, window length must be odd\n");
			return null;
		}

		// This has the effect of inverting all weight values
		if (type == filterType.HIGH_PASS) {
			ft = -ft;
		}

		// Calculate taps
		// Due to symmetry, only need to calculate half the window
		for (n = 0; n < halfLength; n++) {
			final double val = Math.sin(2.0 * Math.PI * ft * (n - m_2))
					/ (Math.PI * (n - m_2));

			window[n] = val;
			window[windowLength - n - 1] = val;
		}

		return window;
	}

	// Create two Math.sinc functions for filter with 2 transitions - Band pass
	// and band stop filters
	public static double[] create2TransSinc(final int windowLength,
			final double trans1Freq, final double trans2Freq,
			final double sampFreq, final filterType type) {
		// Allocate memory for the window
		final double[] window = new double[windowLength];

		if ((type != filterType.BAND_PASS) && (type != filterType.BAND_STOP)) {
			System.err
					.println("create2TransSinc: Bad filter type, should be either BAND_PASS or BAND_STOP\n");
			return null;
		}

		// Calculate the normalised transistion frequencies.
		double ft1 = trans1Freq / sampFreq;
		double ft2 = trans2Freq / sampFreq;

		final double m_2 = 0.5 * (windowLength - 1);
		final int halfLength = windowLength / 2;

		// Set centre tap, if present
		// This avoids a divide by zero
		if ((2 * halfLength) != windowLength) {
			double val = 2.0 * (ft2 - ft1);

			// If we want a band stop filter, subtract Math.sinc functions from
			// a dirac pulse
			if (type == filterType.BAND_STOP) {
				val = 1.0 - val;
			}

			window[halfLength] = val;
		} else {
			System.err
					.println("create1TransSinc: For band pass and band stop filters, window length must be odd\n");
			return null;
		}

		// Swap transition points if Band Stop
		if (type == filterType.BAND_STOP) {
			final double tmp = ft1;
			ft1 = ft2;
			ft2 = tmp;
		}

		// Calculate taps
		// Due to symmetry, only need to calculate half the window
		for (int n = 0; n < halfLength; n++) {
			final double val1 = Math.sin(2.0 * Math.PI * ft1 * (n - m_2))
					/ (Math.PI * (n - m_2));
			final double val2 = Math.sin(2.0 * Math.PI * ft2 * (n - m_2))
					/ (Math.PI * (n - m_2));

			window[n] = val2 - val1;
			window[windowLength - n - 1] = val2 - val1;
		}

		return window;
	}

	public static double[] createKaiserWindow(final double[] in, double[] out,
			final int windowLength, final double[] beta) {
		final double m_2 = (windowLength - 1) / 2.0;
		final double denom = modZeroBessel(beta[0]); // Denominator of Kaiser
														// function

		// If output buffer has not been allocated, allocate memory now
		if (out == null) {
			out = new double[windowLength];
		}

		int n;
		for (n = 0; n < windowLength; n++) {
			double val = ((n) - m_2) / m_2;
			val = 1 - (val * val);
			out[n] = modZeroBessel(beta[0] * Math.sqrt(val)) / denom;
		}

		// If input has been given, multiply with out
		if (in != null) {
			for (n = 0; n < windowLength; n++) {
				out[n] *= in[n];
			}
		}

		return out;
	}

	// Create a set of window weights
	// in - If not null, each value will be multiplied with the window weight
	// out - The output weight values, if null and new array will be allocated
	// windowLength - the number of weights
	// windowType - The window type
	public static double[] createWindow(final double[] in, double[] out,
			final int windowLength, final windowType type) {
		// If output buffer has not been allocated, allocate memory now
		if (out == null) {
			out = new double[windowLength];
		}

		int n;
		final int m = windowLength - 1;
		final int halfLength = windowLength / 2;

		// Calculate taps
		// Due to symmetry, only need to calculate half the window
		switch (type) {
		case RECTANGULAR:
			for (n = 0; n < windowLength; n++) {
				out[n] = 1.0;
			}
			break;

		case BARTLETT:
			for (n = 0; n <= halfLength; n++) {
				final double tmp = n - ((double) m / 2);
				final double val = 1.0 - ((2.0 * Math.abs(tmp)) / m);
				out[n] = val;
				out[windowLength - n - 1] = val;
			}

			break;

		case HANNING:
			for (n = 0; n <= halfLength; n++) {
				final double val = 0.5 - (0.5 * Math.cos((2.0 * Math.PI * n)
						/ m));
				out[n] = val;
				out[windowLength - n - 1] = val;
			}

			break;

		case HAMMING:
			for (n = 0; n <= halfLength; n++) {
				final double val = 0.54 - (0.46 * Math.cos((2.0 * Math.PI * n)
						/ m));
				out[n] = val;
				out[windowLength - n - 1] = val;
			}
			break;

		case BLACKMAN:
			for (n = 0; n <= halfLength; n++) {
				final double val = (0.42 - (0.5 * Math.cos((2.0 * Math.PI * n)
						/ m)))
						+ (0.08 * Math.cos((4.0 * Math.PI * n) / m));
				out[n] = val;
				out[windowLength - n - 1] = val;
			}
			break;
		}

		// If input has been given, multiply with out
		if (in != null) {
			for (n = 0; n < windowLength; n++) {
				out[n] *= in[n];
			}
		}

		return out;
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		System.out.println("ciao");
		final int windowLength = 201;
		final double sampFreq = 44100;

		// Low and high pass filters
		final double transFreq = 10000;

		double[] lpf = create1TransSinc(windowLength, transFreq, sampFreq,
				filterType.LOW_PASS);
		final double[] lpf_hamming = createWindow(lpf, null, windowLength,
				windowType.HAMMING);
		final double[] lpf_blackman = createWindow(lpf, null, windowLength,
				windowType.BLACKMAN);

		final double[] hpf = create1TransSinc(windowLength, transFreq,
				sampFreq, filterType.HIGH_PASS);
		final double[] hpf_hamming = createWindow(hpf, null, windowLength,
				windowType.HAMMING);

		outputFFT("lpf-hamming.dat", lpf_hamming, windowLength, sampFreq);
		outputFFT("lpf-blackman.dat", lpf_blackman, windowLength, sampFreq);
		outputFFT("hpf-hamming.dat", hpf_hamming, windowLength, sampFreq);

		// Band pass and band stop filters
		final double trans1Freq = 5000;
		final double trans2Freq = 17050;

		final double[] bpf = create2TransSinc(windowLength, trans1Freq,
				trans2Freq, sampFreq, filterType.BAND_PASS);
		final double[] bpf_hamming = createWindow(bpf, null, windowLength,
				windowType.HAMMING);

		final double[] bsf = create2TransSinc(windowLength, trans1Freq,
				trans2Freq, sampFreq, filterType.BAND_STOP);
		final double[] bsf_hamming = createWindow(bsf, null, windowLength,
				windowType.HAMMING);

		outputFFT("bpf-hamming.dat", bpf_hamming, windowLength, sampFreq);
		outputFFT("bsf-hamming.dat", bsf_hamming, windowLength, sampFreq);

		// Kaiser Window
		int kaiserWindowLength;
		final double[] beta = new double[1];

		kaiserWindowLength = calculateKaiserParams(0.001, 800, sampFreq, beta);

		lpf = create1TransSinc(kaiserWindowLength, transFreq, sampFreq,
				filterType.LOW_PASS);
		final double[] lpf_kaiser = createKaiserWindow(lpf, null,
				kaiserWindowLength, beta);

		outputFFT("lpf-kaiser.dat", lpf_kaiser, kaiserWindowLength, sampFreq);
	}

	public static double modZeroBessel(final double x) {
		int i;

		final double x_2 = x / 2;
		double num = 1;
		double fact = 1;
		double result = 1;

		for (i = 1; i < 20; i++) {
			num *= x_2 * x_2;
			fact *= i;
			result += num / (fact * fact);
			// printf("%f %f %f\n", num, fact, result);
		}

		return result;
	}

	public static int outputFFT(final String Filename, final double[] window,
			final int windowLength, final double sampFreq) throws IOException {
		int i;
		PrintWriter fp;
		double[] in;
		fftw_complex out;
		fftw_plan plan;
		final int result = 0;

		// If the window length is short, zero padding will be used
		final int fftSize = (windowLength < 2048) ? 2048 : windowLength;

		// Calculate size of result data
		final int resultSize = (fftSize / 2) + 1;

		// Allocate memory to hold input and output data
		in = new double[fftSize];
		out = new fftw_complex(resultSize);

		// Create the plan
		plan = fftw_plan.fftw_plan_dft_r2c_1d(fftSize, in, out,
				fftw.FFTW_MEASURE);

		// Copy window and add zero padding (if required)
		for (i = 0; i < windowLength; i++) {
			in[i] = window[i];

		}
		for (; i < fftSize; i++) {
			in[i] = 0;
		}

		// Perform fft
		plan.fftw_execute();

		// Open File for writing
		fp = new PrintWriter(new FileWriter(new File(Filename)));

		// Output result
		for (i = 0; i < resultSize; i++) {
			final double freq = (sampFreq * i) / fftSize;
			final double mag = Math.sqrt((out.get(i, 0) * out.get(i, 0))
					+ (out.get(i, 1) * out.get(i, 1)));
			final double magdB = 20 * Math.log10(mag);
			final double phase = Math.atan2(out.get(i, 1), out.get(i, 0));
			fp.format("%02d %f %f %f %f\n", i, freq, mag, magdB, phase);
		}
		fp.close();
		return result;
	}
}
