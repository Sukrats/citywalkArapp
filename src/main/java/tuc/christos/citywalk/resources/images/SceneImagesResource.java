package tuc.christos.citywalk.resources.images;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.Image;
import tuc.christos.citywalk.model.Scene;
import tuc.christos.citywalk.service.SceneService;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SceneImagesResource {
	//PATH: web_App_Resources_Path/IMAGES/scenes/folder_name/thumb
	//PATH: web_App_Resources_Path/IMAGES/scenes/folder_name/*
	@GET
	@Produces({"text/plain","application/json"})
	public List<Image> amagad(@Context ServletContext SC,@Context UriInfo uriInfo, @PathParam("id")Long id){//get scene, get image location, return image
		Scene scene = SceneService.getScene(id);
		//GET IMAGES FROM PATH LOCATION
		String location = scene.getImgLoc();
		if(location.isEmpty())
			throw new DataNotFoundException("images for scene with id: "+ id+ " not found!");
		Set<String> set = SC.getResourcePaths(location);
		if(set.isEmpty())
			throw new DataNotFoundException("images for scene with id: "+ id+ " not found!");
		ArrayList<Image> images = new ArrayList<>();
		scene.setImages(images);
		//ITERATE THROUGH IMAGES AND GET INDIVIDUAL IDs
		if (!set.isEmpty())
		for(String s : set){
			String[] st = s.split("/");
			for(String str: st){
				if(str.contains(".jpg")){
					System.out.println(str);
					URI uri = uriInfo.getAbsolutePathBuilder().path(str).build();
					images.add(new Image(uri.toString()));
					}
			}
		}
		System.out.println("Scene urls: "+scene.getImages().toString());
		
		return scene.getImages();
	}
	
	@GET
	@Path("/{name}")
	@Produces({"image/jpg","image/png","text/plain","application/json"})
	public Response  test(@Context ServletContext SC, @PathParam("name")String imgname, @PathParam("id")Long id){
		Scene temp = SceneService.getScene(id);
		if(temp.getImgLoc().isEmpty())
			throw new DataNotFoundException("Image "+imgname+" for scene whith id: "+temp.getId()+" does not exist!");
		String path = SC.getRealPath(temp.getImgLoc()+imgname);
		File image = new File(path);
		Response res = Response.ok((Object)image,"image/jpg").header("content-attachment", "filename="+temp.getName()+".jpg").build();
		return res;
	}
	

}
