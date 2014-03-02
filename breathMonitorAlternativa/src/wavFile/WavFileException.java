package wavFile;

public class WavFileException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8551633801537517243L;

	public WavFileException() {
		super();
	}

	public WavFileException(final String message) {
		super(message);
	}

	public WavFileException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public WavFileException(final Throwable cause) {
		super(cause);
	}
}
