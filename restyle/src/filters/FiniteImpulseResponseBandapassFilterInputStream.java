package filters;

import java.io.IOException;

public class FiniteImpulseResponseBandapassFilterInputStream implements
		DoubleInputStream {

	private final DoubleInputStream inputStream;

	public FiniteImpulseResponseBandapassFilterInputStream(
			final DoubleInputStream inputStream, final int lowerFrequency,
			final int higherFrequency) {
		this.inputStream = inputStream;
	}

	@Override
	public void close() throws IOException {
		inputStream.close();
	}

	@Override
	public int read(final double[] b, final int off, final int len)
			throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
