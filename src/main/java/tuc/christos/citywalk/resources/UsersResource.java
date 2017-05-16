package tuc.christos.citywalk.resources;

import java.net.URI;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import javax.ws.rs.core.UriInfo;

import tuc.christos.citywalk.model.Link;
import tuc.christos.citywalk.model.Profile;
import tuc.christos.citywalk.service.UserService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsersResource {
	
	@GET
	@Path("admin")
	public Response getUsers(@Context UriInfo uriInfo){
		
		List<Profile> users = UserService.getAllProfiles();
		
		for(Profile profile: users){
			profile.getLinks().clear();
			profile.addLink(new Link(uriForSelf(uriInfo,profile),"self"));
			profile.addLink(new Link(uriForPlaces(uriInfo,profile),"places"));
			profile.addLink(new Link(uriForVisits(uriInfo,profile),"visits"));
		}
		GenericEntity<List<Profile>> gProfiles = new GenericEntity<List<Profile>>(users){};
		return Response.ok(gProfiles).build();
	}
	
	
	
	@POST
	public Response postUser(@Context UriInfo uriInfo, Profile profile){
		Profile prof = UserService.addProfile(profile); 
		
		if(profile == null)
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					   .build();
		

		prof.getLinks().clear();
		prof.addLink(new Link(uriForSelf(uriInfo,profile),"self"));
		prof.addLink(new Link(uriForPlaces(uriInfo,profile),"places"));
		prof.addLink(new Link(uriForVisits(uriInfo,profile),"visits"));
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/citywalk/ArApp/users
						 .path("secure")
						 .path(prof.getUsername())//append /{userName}
						 .build();
		
		return Response.created(uri)
					   .entity(prof)
					   .build();
		
	}
	
	@GET
	@Path("/secure/login")
	public Response attemptLogin(@QueryParam("auth")String value, @Context UriInfo uriInfo){
		int action = ( value.contains("@") ?  1 : 2);
		Profile profile = UserService.getLogin(value,action); 
		
		profile.getLinks().clear();
		profile.addLink(new Link(uriForSelf(uriInfo,profile),"self"));
		profile.addLink(new Link(uriForPlaces(uriInfo,profile),"places"));
		profile.addLink(new Link(uriForVisits(uriInfo,profile),"visits"));
		
		System.out.println("/***********************************GOT USER***********************************\\");
		System.out.println("Username: "+ profile.getUsername()+
			    		   "\nEmail: "+ profile.getEmail()+
			    		   "\nFirst Name: " + profile.getFirstname()+
			    		   "\nLast Name: " + profile.getLastname()+
			    		   "\nCreated: " + profile.getCreated()+
			    		   "\nRecent Activity: " + profile.getRecentActivity()+
			    		   "\nPassword: "+ profile.getPassword());
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(profile.getUsername()))// {userName}
						 .build();
				
		return Response.ok(uri)
					   .entity(profile)
					   .build();
	}
	
	@GET
	@Path("/secure/{username}")
	public Response getUser(@Context UriInfo uriInfo,@PathParam("username") String username){
		
		Profile profile = UserService.getProfile(username); 

		profile.getLinks().clear();
		profile.addLink(new Link(uriForSelf(uriInfo,profile),"self"));
		profile.addLink(new Link(uriForPlaces(uriInfo,profile),"places"));
		profile.addLink(new Link(uriForVisits(uriInfo,profile),"visits"));
		
		System.out.println("/***********************************GOT USER***********************************\\");
		System.out.println("Username: "+ profile.getUsername()+
			    		   "\nEmail: "+ profile.getEmail()+
			    		   "\nFirst Name: " + profile.getFirstname()+
			    		   "\nLast Name: " + profile.getLastname()+
			    		   "\nCreated: " + profile.getCreated()+
			    		   "\nPassword: "+ profile.getPassword());
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(profile.getUsername()))// {userName}
						 .build();
				
		return Response.ok(uri)
					   .entity(profile)
					   .build();
	}
	
	@PUT
	@Path("/secure/{username}")
	public Response updateUser(@HeaderParam("Authorization")String value, @Context UriInfo uriInfo, @PathParam("username")String username, Profile profile){
		Profile uProfile = UserService.updateProfile(profile, username);
		System.out.println("/***********************************UPDATED USER***********************************\\");
		System.out.println("Username: "+ uProfile.getUsername()+
			    		   "\nEmail: "+ uProfile.getEmail()+
			    		   "\nFirst Name: " + uProfile.getFirstname()+
			    		   "\nLast Name: " + uProfile.getLastname()+
			    		   "\nPassword: "+ uProfile.getPassword());
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(uProfile.getUsername()))// {userName}
						 .build();
				
		return Response.ok(uri)
					   .entity(uProfile)
					   .build();
	}
	
	@DELETE
	@Path("/secure/{username}")
	public Response deleteUser(@Context UriInfo uriInfo,@PathParam("username")String username){
		UserService.deleteProfile(username);
		
		return Response.status(Status.NO_CONTENT)
				   .build();
	}
	
	@Path("/secure/{username}/visits")
	public VisitsResource getVisits(){
		return new VisitsResource();
	}
		
	@Path("/secure/{username}/places")
	public PlacesResource getPlaces(){
		return new PlacesResource();
	}
	
	
	private String uriForSelf(UriInfo uriInfo, Profile profile) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("username", profile.getUsername())
				.build()
				.toString();
		return uri;
	}
	
	private String uriForPlaces(UriInfo uriInfo, Profile profile) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("username", profile.getUsername())
				.path("places")
				.build()
				.toString();
		return uri;
	}
	
	private String uriForVisits(UriInfo uriInfo, Profile profile) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(UsersResource.class)
				.path(UsersResource.class,"getUser")
				.resolveTemplate("username", profile.getUsername())
				.path("visits")
				.build()
				.toString();
		return uri;
	}
	
}
