package tuc.christos.citywalk.exceptions;

public class EmptyBodyOnPostException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6645026232712297786L;
	
	public EmptyBodyOnPostException(String message){
		super(message);
	}

}
