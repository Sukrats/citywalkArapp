package tuc.christos.citywalk.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class tempScenes {

	private ArrayList<Scene> scenes = new ArrayList<>();
	
	public tempScenes(){}

	public ArrayList<Scene> getScenes() {
		return scenes;
	}

	public void setScenes(ArrayList<Scene> scenes) {
		this.scenes = scenes;
	}
	
	public void addScene(Scene scene){
		this.scenes.add(scene);
	}
	
	
}
