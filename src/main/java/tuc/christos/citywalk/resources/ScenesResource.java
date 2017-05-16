package tuc.christos.citywalk.resources;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import tuc.christos.citywalk.model.Link;
import tuc.christos.citywalk.model.Scene;
import tuc.christos.citywalk.model.tempScenes;
import tuc.christos.citywalk.resources.images.SceneImagesResource;
import tuc.christos.citywalk.service.SceneService;

@Path("/scenes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScenesResource {
	
	@GET
	public List<Scene> getScenes(@Context UriInfo uriInfo, @QueryParam("period")long periodId){
		List<Scene> scenes = new ArrayList<>();
		if(periodId > 0){
			scenes = SceneService.getScenesForPeriod(periodId);
			for(Scene scene: scenes){
				scene.addLink(new Link(uriForSelf(uriInfo, scene),"self"));
				if(!scene.getImgLoc().isEmpty()){
					scene.addLink(new Link(uriForThumbnail(uriInfo, scene),"thumbnail"));
					scene.addLink(new Link(uriForImages(uriInfo, scene),"images"));
				}
			}
			return scenes;
		}
		
		scenes = SceneService.getAllScenes();
		for(Scene scene: scenes){
			scene.addLink(new Link(uriForSelf(uriInfo, scene),"self"));
			if(!scene.getImgLoc().isEmpty()){
				scene.addLink(new Link(uriForThumbnail(uriInfo, scene),"thumbnail"));
				scene.addLink(new Link(uriForImages(uriInfo, scene),"images"));
			}
		}
		return scenes;
	}
	
	@POST
	public Response postScene(@Context UriInfo uriInfo,Scene Scene){
		Scene scene = SceneService.addScene(Scene); 
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/citywalk/ArApp/users
						 .path(String.valueOf(scene.getId()))//append /{userName}
						 .build();
		
		return Response.created(uri)
					   .entity(scene)
					   .build();
		
	}
	
	public Response postScenes(@Context UriInfo uriInfo,tempScenes scenes){
		boolean success = SceneService.addScenes(scenes);
		if(success)
			return Response.status(Status.CREATED).build();
		else
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
			
	}
	
	@GET
	@Path("/{id}")
	public Response getScene(@Context UriInfo uriInfo,@PathParam("id") long id){
		Scene scene = SceneService.getScene(id);		
		if(scene == null){
			return Response.status(Status.NO_CONTENT)
						   .entity(scene)
						   .build();
		}

		scene.addLink(new Link(uriForSelf(uriInfo, scene),"self"));
		if(!scene.getImgLoc().isEmpty()){
			scene.addLink(new Link(uriForThumbnail(uriInfo, scene),"thumbnail"));
			scene.addLink(new Link(uriForImages(uriInfo, scene),"images"));
		}
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(id))// {userName}
						 .build();
		
		System.out.println("Absolute Path: " + uriInfo.getAbsolutePath().toString());
		
		return Response.ok(uri)
					   .entity(scene)
					   .build();
	}
	

	@Path("/{id}/images")
	public SceneImagesResource getImageResource(){
		return new SceneImagesResource();
	}
	
	private String uriForSelf(UriInfo uriInfo, Scene scene) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(ScenesResource.class)
				.path(ScenesResource.class,"getScene")
				.resolveTemplate("id", scene.getId())
				.build()
				.toString();
		return uri;
	}
	 
	private String uriForImages(UriInfo uriInfo, Scene scene) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(ScenesResource.class)
				.path(ScenesResource.class,"getScene")
				.resolveTemplate("id", scene.getId())
				.path("images")
				.build()
				.toString();
		return uri;
	}
	
	private String uriForThumbnail(UriInfo uriInfo, Scene scene) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(ScenesResource.class)
				.path(ScenesResource.class,"getScene")
				.resolveTemplate("id", scene.getId())
				.path("images/thumb.jpg")
				.build()
				.toString();
		return uri;
	}
}
