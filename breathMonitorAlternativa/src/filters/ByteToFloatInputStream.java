package filters;

import java.io.FilterInputStream;
import java.io.InputStream;

public class ByteToFloatInputStream extends FilterInputStream {

	public ByteToFloatInputStream(final InputStream in) {
		super(in);
	}

}
