package tuc.christos.citywalk.model;


import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Visit {

	private long sceneid;
	private Date created;
	private String username;
	private String scenename;


	private Scene scene;
	
	public Visit(){}
	
	public Visit(long id) {
		super();
		this.sceneid = id;
	}
	
	public Visit(long id, Scene scene) {
		super();
		this.sceneid = id;
		this.scene = scene;
	}
	
	public long getSceneid() {
		return sceneid;
	}

	public void setSceneid(long sceneid) {
		this.sceneid = sceneid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
	public String getScenename() {
		return scenename;
	}

	public void setScenename(String scenename) {
		this.scenename = scenename;
	}
	
	
	
}
