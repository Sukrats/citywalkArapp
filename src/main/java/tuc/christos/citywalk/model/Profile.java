package tuc.christos.citywalk.model;


import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.*;

@XmlRootElement
public class Profile {
	private long id;
	private String username;
	private String email;
	private String password;
	private String firstname;
	private String lastname;
	private Date created;
	private String recentActivity;
	
	private Map<Long,Visit> visits = new HashMap<>();
	private long numOfVisits = visits.size();
	
	private Map<Long,Place> places = new HashMap<>();
	private long numOfPlaces = places.size();
	
	private List<Link> links = new ArrayList<>();
	
	
	public Profile(){}
	
	public Profile(long id, String username, String email, String password, String firstname, String lastname,
			Date created) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.created = created;
	}

	public Profile(long id, String profilename, String firstname, String lastname){
		this.id = id;
		this.username = profilename;
		this.firstname = firstname;
		this.lastname = lastname;
	}
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String profilename) {
		this.username = profilename;
	}

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@XmlTransient
	public Map<Long,Visit> getVisits() {
		return visits;
	}

	public void setVisits(Map<Long,Visit> visits) {
		this.visits = visits;
	}
	
	public Visit addVisit(Visit visit){
		this.numOfVisits++;
		return this.visits.put(visit.getSceneid(), visit);
	}

	public long getNumofvisits() {
		return numOfVisits;
	}

	public void setNumofvisits(long numofvisits) {
		this.numOfVisits = numofvisits;
	}
	
	@XmlTransient
	public Map<Long, Place> getPlaces() {
		return places;
	}

	public void setPlaces(Map<Long, Place> myPlaces) {
		this.places = myPlaces;
	}
	
	public String getRecentActivity() {
		return recentActivity;
	}

	public void setRecentActivity(String recentActivity) {
		this.recentActivity = recentActivity;
	}

	public Place addPlace(Place place){
		this.numOfPlaces++;
		return this.places.put(place.getId(), place);
	}

	public long getNumOfPlaces() {
		return numOfPlaces;
	}

	public void setNumOfPlaces(long numOfPlaces) {
		this.numOfPlaces = numOfPlaces;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public void addLink(Link link) {
		this.links.add(link);
	}
	
	
}