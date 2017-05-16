package tuc.christos.citywalk.exceptions;

public class ConstraintViolationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8129628134170869168L;
	
	public ConstraintViolationException (String message) {
		super(message);
	}

}
