package tuc.christos.citywalk.resources;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import tuc.christos.citywalk.model.Link;
import tuc.christos.citywalk.model.Period;
import tuc.christos.citywalk.model.Scene;
import tuc.christos.citywalk.resources.images.PeriodImageResource;
import tuc.christos.citywalk.service.PeriodService;
import tuc.christos.citywalk.service.SceneService;



@Path("periods")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeriodsResource {
	
	@GET
	public Response getPeriods(@Context UriInfo uriInfo){
		List<Period> per = PeriodService.getAllPeriods();
		
		for(Period period: per){
			period.getLinks().clear();
			period.addLink(new Link(uriForSelf(uriInfo, period),"self"));
			period.addLink(new Link(uriForScenes(uriInfo,period),"scenes"));
			period.addLink(new Link(uriForLogo(uriInfo, period),"logo"));
			period.addLink(new Link(uriForImages(uriInfo, period),"images"));
		}
		GenericEntity<List<Period>> gPer = new GenericEntity<List<Period>>(per){};
		return Response.ok(gPer)
				   .build();
	}
	
	@GET
	@Path("/sync")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getSync(@Context UriInfo uriInfo){
		Timestamp time = PeriodService.checkSync();
		
		return Response.ok(time.toString()).build();
	}
	
	@POST
	public Response postPeriod(@Context UriInfo uriInfo,Period Period){
		Period period = PeriodService.addPeriod(Period); 
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/citywalk/ArApp/users
						 .path(String.valueOf(period.getName()))//append /{userName}
						 .build();
		
		return Response.created(uri)
					   .entity(period)
					   .build();
		
	}
	
	@GET
	@Path("/{periodid}")
	public Response getPeriod(@Context UriInfo uriInfo,@PathParam("periodid") long periodid){
		Period period = PeriodService.getPeriod(periodid); 
		
		if(period == null){
			return Response.status(Status.NOT_FOUND)
						   .build();
		}

		period.getLinks().clear();
		period.addLink(new Link(uriForSelf(uriInfo, period),"self"));
		period.addLink(new Link(uriForScenes(uriInfo,period),"scenes"));
		period.addLink(new Link(uriForLogo(uriInfo, period),"logo"));
		period.addLink(new Link(uriForImages(uriInfo, period),"images"));
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(periodid))// {userName}
						 .build();
		
		System.out.println("Absolute Path: " + uriInfo.getAbsolutePath().toString());
		
		return Response.ok(uri)
					   .entity(period)
					   .build();
	}
	
	@GET
	@Path("/{periodid}/scenes")
	public Response getPeriodScenes(@Context UriInfo uriInfo, @PathParam("periodid") long periodid){
		Period period = PeriodService.getPeriod(periodid); 
		
		if(period == null){
			return Response.status(Status.NO_CONTENT)
						   .entity(period)
						   .build();
		}
		
		List<Scene> pScenes = SceneService.getScenesForPeriod(periodid);
		for(Scene scene: pScenes){
			scene.addLink(new Link(uriForSelf(uriInfo, scene),"self"));
			if(!scene.getImgLoc().isEmpty()){
				scene.addLink(new Link(uriForThumbnail(uriInfo, scene),"thumbnail"));
				scene.addLink(new Link(uriForImages(uriInfo, scene),"images"));
			}
		}
		GenericEntity<List<Scene>> gScenes = new GenericEntity<List<Scene>>(pScenes){};
		
		URI uri = uriInfo.getAbsolutePathBuilder()//localhost:8080/chaniacitywalk/ArApp/users/
						 .path(String.valueOf(periodid))
						 .path("scenes")
						 .build();
		
		System.out.println("Absolute Path: " + uriInfo.getAbsolutePath().toString());
		
		return Response.ok(uri)
					   .entity(gScenes)
					   .build();
	}
	
	@Path("{periodid}/images")
	public PeriodImageResource getPeriodImages(){
		return new PeriodImageResource();
	}
	
	private String uriForSelf(UriInfo uriInfo, Period period) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(PeriodsResource.class)
				.path(PeriodsResource.class,"getPeriod")
				.resolveTemplate("periodid", period.getId())
				.build()
				.toString();
		return uri;
	}
	
	private String uriForScenes(UriInfo uriInfo, Period period) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(PeriodsResource.class)
				.path(PeriodsResource.class,"getPeriod")
				.resolveTemplate("periodid", period.getId())
				.path("scenes")
				.build()
				.toString();
		return uri;
	}
	
	private String uriForImages(UriInfo uriInfo, Period period) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(PeriodsResource.class)
				.path(PeriodsResource.class,"getPeriod")
				.resolveTemplate("periodid", period.getId())
				.path("images")
				.build()
				.toString();
		return uri;
	}
	
	private String uriForLogo(UriInfo uriInfo, Period period) {
		String uri = uriInfo.getBaseUriBuilder()
				.path(PeriodsResource.class)
				.path(PeriodsResource.class,"getPeriod")
				.resolveTemplate("periodid", period.getId())
				.path("images/logo.jpg")
				.build()
				.toString();
		return uri;
	}

	//SCENES URI
	
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
