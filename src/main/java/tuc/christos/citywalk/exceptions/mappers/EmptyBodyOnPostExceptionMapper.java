package tuc.christos.citywalk.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import tuc.christos.citywalk.exceptions.EmptyBodyOnPostException;
import tuc.christos.citywalk.model.ErrorMessage;

public class EmptyBodyOnPostExceptionMapper implements ExceptionMapper<EmptyBodyOnPostException> {

	@Override
	public Response toResponse(EmptyBodyOnPostException exception) {

		ErrorMessage message = new ErrorMessage(exception.getMessage() , Status.BAD_REQUEST.getStatusCode() , "no documentation");		
		return Response.status(Status.BAD_REQUEST).entity(message).build();
	}

}
