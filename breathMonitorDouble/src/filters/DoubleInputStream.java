package filters;

import java.io.Closeable;
import java.io.IOException;

public interface DoubleInputStream extends Closeable {
	public int read(double b[], int off, int len) throws IOException;
}
