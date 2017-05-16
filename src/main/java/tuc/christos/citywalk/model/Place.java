package tuc.christos.citywalk.model;


import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement
public class Place {

	private long id;
	private Date created;
	private long scene_id;
	private Scene scene;
	private String comment;
	private String user;
	private String sceneName;
	
	public Place(){}
	
	public Place(long id, Scene scene) {
		super();
		this.id = id;
		this.scene = scene;
	}
	
	public Place(long id, Scene scene, String comment) {
		super();
		this.id = id;
		this.scene = scene;
		this.comment = comment;
	}
	
	public Place(long id, Date created, Scene scene) {
		super();
		this.id = id;
		this.created = created;
		this.scene = scene;
	}


	@XmlTransient
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public Scene getScene() {
		return scene;
	}


	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public long getScene_id() {
		return scene_id;
	}

	public void setScene_id(long scene_id) {
		this.scene_id = scene_id;
	}
	

}
