package tuc.christos.citywalk.model;


import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
public class Period {
	private long id;
	private String name;
	private String description;
	private String imagesUrl;	

	private String started;
	private String ended;
	
	private List<Link> links = new ArrayList<>();

	private String imagesLoc;
	private List<Image> images = new ArrayList<>();
	//private List<Scene> scenes = new ArrayList<>();

	public Period(){}
	
	public Period(long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	
	public Period(long id, String name, String description,String started, String ended) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.started = started;
		this.ended = ended;
	}
	


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getImagesLoc() {
		return imagesLoc;
	}


	public void setimagesLoc(String imagesLoc) {
		this.imagesLoc = imagesLoc;
	}


	public String getStarted() {
		return started;
	}


	public void setStarted(String started) {
		this.started = started;
	}


	public String getEnded() {
		return ended;
	}


	public void setEnded(String ended) {
		this.ended = ended;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(Link link){
		this.links.add(link);
	}
	/*public List<Scene> getScenes(){
		return scenes;
	}
	
	public void setScenes(List<Scene> scenes){
		this.scenes = scenes;
	}
	
	public void addScene(Scene scene){
		scenes.add(scene);
	}*/

	public String getImagesUrl() {
		return imagesUrl;
	}

	public void setImagesUrl(String imagesUrl) {
		this.imagesUrl = imagesUrl;
	}

	@XmlTransient
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	public void addImage(Image image){
		this.images.add(image);
		
	}
}
