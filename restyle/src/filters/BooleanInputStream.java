package filters;

import java.io.IOException;

public interface BooleanInputStream {

	public void close();

	public int read(boolean[] b, int off, int len) throws IOException;
}
