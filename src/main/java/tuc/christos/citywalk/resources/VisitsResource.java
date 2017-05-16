package tuc.christos.citywalk.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.Visit;
import tuc.christos.citywalk.service.UserService;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitsResource {

	@GET
	public List<Visit> getVisits(@PathParam("username")String username){
		List<Visit> visits = UserService.getVisits(username);
			if(visits.isEmpty())
				throw new DataNotFoundException("User "+username+" has no visits!");
		return visits;
	}
	
	@POST
	public Response addVisit(@Context UriInfo uriInfo,
			@PathParam("username")String username, Visit visit){
		visit.setUsername(username);
		if(!UserService.addVisit(username, visit.getSceneid())){
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		
		URI uri = uriInfo.getAbsolutePathBuilder()
				.build();
		
		return Response.created(uri).entity(visit).build();
	}


}
