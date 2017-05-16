package tuc.christos.citywalk.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class Scene {

	private long id;
	private long periodId;
	private String name;
	private String description;
	private double latitude;
	private double longitude;
	private long numOfVisits;
	private String thumb;
	
	private String imgLoc;
	private List<Image> images = new ArrayList<>();
	
	private List<Link> links = new ArrayList<>();
	
	public Scene(){}
	
	public Scene(long id, String name, String description, double latitude, double longitude, long periodId) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
		this.periodId = periodId;
	}
	
	public Scene(long id, String name, String description, double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Scene(long id, String name,long periodId, String description, double latitude, double longitude) {
		super();
		this.id = id;
		this.name = name;
		this.periodId = periodId;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longitude;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public long getPeriodId() {
		return periodId;
	}

	public void setPeriodId(long periodId) {
		this.periodId = periodId;
	}

	public long getNumOfVisits() {
		return numOfVisits;
	}

	public void setNumOfVisits(long numOfVisits) {
		this.numOfVisits = numOfVisits;
	}
	@XmlTransient
	public String getThumb(){
		return thumb;
	}
	public void setThumb(String loc){
		this.thumb = loc;
	}

	@XmlTransient
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	public void addImage(Image image){
		images.add(image);
	}

	@XmlTransient
	public String getImgLoc() {
		return imgLoc;
	}

	public void setImgLoc(String imgLoc) {
		this.imgLoc = imgLoc;
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
	
}
