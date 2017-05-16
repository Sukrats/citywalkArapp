package tuc.christos.citywalk.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import tuc.christos.citywalk.exceptions.DuplicateEntryException;
import tuc.christos.citywalk.model.ErrorMessage;

@Provider
public class DuplicateEntryMapper implements ExceptionMapper<DuplicateEntryException> {

	@Override
	public Response toResponse(DuplicateEntryException exception) {
		
		ErrorMessage message = new ErrorMessage(exception.getMessage() , Status.CONFLICT.getStatusCode() , "no documentation");		
		return Response.status(Status.CONFLICT).entity(message).build();
	}

}
