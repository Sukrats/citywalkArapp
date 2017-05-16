package tuc.christos.citywalk.exceptions;

public class DuplicateEntryException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3576822126914458508L;
	
	public DuplicateEntryException (String message) {
		super(message);
	}

}
