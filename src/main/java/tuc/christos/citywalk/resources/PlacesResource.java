package tuc.christos.citywalk.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import tuc.christos.citywalk.service.UserService;
import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.exceptions.EmptyBodyOnPostException;
import tuc.christos.citywalk.model.Place;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlacesResource {
	
	@GET
	public Response getPlaces(@PathParam("username")String username){
			
		List<Place> places = new ArrayList<>(UserService.getPlaces(username));
		GenericEntity<List<Place>> gPlaces = new GenericEntity<List<Place>>(places){};
		return Response.ok(gPlaces).build();
	}
	
	@POST
	public Response addPlace(@Context UriInfo uriInfo, 
			@PathParam("username")String username, Place place){
		if(!UserService.addPlace(username, place.getScene_id()))
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
		URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(place.getScene_id()))
				.build();
		
		return Response.created(uri).build();
	}
	
	@GET
	@Path("/{sceneid}")
	public Response getPlace(@PathParam("sceneid")long sceneid, @PathParam("username")String username){
		Place place = UserService.getPlace(username, sceneid);
		if(place == null)
			throw new DataNotFoundException("User: "+username+" has not marked scene with id: "+sceneid);
		
		return Response.ok(UserService.getPlace(username, sceneid)).build();
	}
	
	@PUT
	@Path("{sceneid}")
	public Response updatePlace(@Context UriInfo uriInfo,
			@PathParam("username")String username,@PathParam("sceneid")long sceneid, Place place){
		if(place == null)
			throw new EmptyBodyOnPostException("Post request in 'places' must contain a body object");
		place.setUser(username);
		place.setScene_id(sceneid);
		if(!UserService.updatePlace(place))
			throw new DataNotFoundException("Place for user: "+username+" and scene with id: "+sceneid+" ,not found!");

		URI uri = uriInfo.getAbsolutePathBuilder()
				.build();
		return Response.ok(uri).entity(place).build();
	}
	
	@DELETE
	@Path("{sceneid}")
	public Response deletePlace(@PathParam("username")String username, @PathParam("sceneid")long sceneid){
		if(!UserService.deletePlace(username, sceneid)){
			throw new DataNotFoundException("Place for user: "+username+" and scene with id: "+sceneid+" ,not found!");
		}
		
		return Response.status(Status.NO_CONTENT).build();
	}
}
