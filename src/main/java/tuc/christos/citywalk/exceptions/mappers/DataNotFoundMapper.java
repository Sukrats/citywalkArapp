package tuc.christos.citywalk.exceptions.mappers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.ErrorMessage;

@Provider
public class DataNotFoundMapper implements ExceptionMapper<DataNotFoundException>{

	@Override
	public Response toResponse(DataNotFoundException exception) {

		ErrorMessage message = new ErrorMessage(exception.getMessage() , Status.NOT_FOUND.getStatusCode() , "no documentation");		
		return Response.status(Status.NOT_FOUND).entity(message).build();

	}

}
