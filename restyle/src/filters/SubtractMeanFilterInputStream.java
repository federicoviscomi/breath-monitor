package filters;

import java.io.IOException;

public class SubtractMeanFilterInputStream extends DoubleFilterInputStream {

	public SubtractMeanFilterInputStream(DoubleInputStream inputStream) {
		super(inputStream);
	}

	@Override
	public int read(double[] b, int off, int len) throws IOException {
		int read = in.read(b, off, len);

		double total = 0;
		for (int i = off; i < off + len; i++) {
			total = total + b[i];
		}

		for (int i = off; i < off + len; i++) {
			b[i] = b[i] - (total / len);
		}

		return read;
	}

}
