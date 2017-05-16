package tuc.christos.citywalk.resources.images;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;

import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.Image;
import tuc.christos.citywalk.model.Period;
import tuc.christos.citywalk.service.PeriodService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PeriodImageResource {
		//PATH: web_App_Resources_Path/IMAGES/periods/{period_id}/logo
		//PATH: web_App_Resources_Path/IMAGES/periods/{period_id}/*

		@GET
		@Produces({"text/plain","application/json"})
		public List<Image> amagad(@Context ServletContext SC,@Context UriInfo uriInfo, @PathParam("periodid")Long id){//get period, get image location, return image
			Period period = PeriodService.getPeriod(id);
			//GET IMAGES FROM PATH LOCATION
			String location = period.getImagesLoc();
			if(location.isEmpty() )
				throw new DataNotFoundException("images for period with id: "+ id+ " not found!");
				
			Set<String> mapPath = SC.getResourcePaths(location);
			if(mapPath.isEmpty() )
				throw new DataNotFoundException("images for period with id: "+ id+ " not found!");
			ArrayList<Image> images = new ArrayList<>();
			//ITERATE THROUGH IMAGES AND GET INDIVIDUAL IDs
			if (!mapPath.isEmpty())
			for(String s : mapPath){
				String[] st = s.split("/");
				for(String str: st){
					if(str.contains(".jpg")){
						System.out.println(str);
						URI uri = uriInfo.getAbsolutePathBuilder().path(str).build();
						images.add(new Image(uri.toString()));
						}
				}
			}
			period.setImages(images);
			System.out.println("Period urls: "+period.getImages().toString());
			
			return period.getImages();
		}
		
		@GET
		@Path("/{name}")
		@Produces({"image/jpg","image/png","text/plain","application/json"})
		public Response  test(@Context ServletContext SC, @PathParam("name")String imgname, @PathParam("periodid")Long id){
			Period temp = PeriodService.getPeriod(id);
			if(temp.getImagesLoc().isEmpty())
				throw new DataNotFoundException("images for period with id: "+ id+ " not found!");
			String path = SC.getRealPath(temp.getImagesLoc()+"/"+imgname);
			File image = new File(path);
			Response res = Response.ok((Object)image,"image/jpg").header("content-attachment", "filename="+temp.getName()+".jpg").build();
			return res;
		}
		
		

}
