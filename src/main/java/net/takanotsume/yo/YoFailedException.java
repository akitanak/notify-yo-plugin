package net.takanotsume.yo;

public class YoFailedException extends Exception {

	private static final long serialVersionUID = 1608152911116024691L;
	
	private static String DEFAULT_MESSAGE = "Sending YO is Failed.";
	
	public YoFailedException() {
		this(DEFAULT_MESSAGE);
	}

	public YoFailedException(String message) {
		super(message);
		
	}

	public YoFailedException(Throwable cause) {
		this(DEFAULT_MESSAGE, cause);
		
	}

	public YoFailedException(String message, Throwable cause) {
		super(message, cause);
		
	}

}
