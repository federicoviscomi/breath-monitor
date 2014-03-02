package stethoscopeInterface;

import java.io.IOException;

import filters.DoubleInputStream;

public interface StethoscopeInterface extends DoubleInputStream {
	public void connect() throws IOException;

	public int skip(int toSkip) throws IOException;
}
