package tuc.christos.citywalk.service;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import tuc.christos.citywalk.database.DBUtility;
import tuc.christos.citywalk.exceptions.ConstraintViolationException;
import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.exceptions.DuplicateEntryException;
import tuc.christos.citywalk.model.Place;
import tuc.christos.citywalk.model.Profile;
import tuc.christos.citywalk.model.Visit;

public class UserService {

	public static ArrayList<Profile> getAllProfiles(){
		ArrayList<Profile> profilesList = new ArrayList<>();

        Connection dbConn = null;
        Statement stmt = null;
        try{
	        dbConn = DBUtility.createConnection();
	        
	        stmt = dbConn.createStatement();
	        String query = "SELECT * FROM player";
	        System.out.println(query);
	        
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.isClosed())
	        	System.out.println("closed result set");
	        while (rs.next()) {
	        	Profile  profile = new Profile();
	        	profile.setEmail(rs.getString("email"));
	        	profile.setUsername(rs.getString("username"));
	        	profile.setPassword(rs.getString("password"));
	        	profile.setFirstname(rs.getString("firstname"));
	        	profile.setLastname(rs.getString("lastname"));
	        	profile.setCreated(rs.getDate("created"));
	        	profile.setRecentActivity(rs.getTimestamp("recent_activity").toString());
	        	profilesList.add(profile);
	        }
		    //clean up environment
		    stmt.close();
		    dbConn.close();
        }catch (SQLException sqle){
        	sqle.printStackTrace();
        }catch (Exception e){
        	e.printStackTrace();
        }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		if(profilesList.isEmpty())
			throw new DataNotFoundException("No users are registered!");
		
		return profilesList; 
	}
	public static Profile getLogin(String cred, int action){

        Profile profile = null;
        Connection dbConn = null;
        Statement stmt = null;
        try{
	        dbConn = DBUtility.createConnection();
	        
	        stmt = dbConn.createStatement();
	        String query = "";
	        if(action == 1)
	        	query = "SELECT * FROM player WHERE email = '" + cred + "'";
	        else
	        	query = "SELECT * FROM player WHERE username = '" + cred + "'";
	        System.out.println(query);
	        
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.isClosed())
	        	System.out.println("closed result set");
	        profile = new Profile();
	        while (rs.next()) {
	        	profile.setEmail(rs.getString("email"));
	        	profile.setUsername(rs.getString("username"));
	        	profile.setPassword(rs.getString("password"));
	        	profile.setFirstname(rs.getString("firstname"));
	        	profile.setLastname(rs.getString("lastname"));
	        	profile.setCreated(rs.getDate("created"));
	        	profile.setRecentActivity(rs.getTimestamp("recent_activity").toString());
	        }
		    //clean up environment
		    stmt.close();
		    dbConn.close();
        }catch (SQLException sqle){
        	sqle.printStackTrace();
        }catch (Exception e){
        	e.printStackTrace();
        }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
        
		if(profile == null)
			throw new DataNotFoundException("User: "+cred+" not found!");
		return profile;
	}
	
	public static Profile getProfile(String username){
        Profile profile = null;
        Connection dbConn = null;
        Statement stmt = null;
        try{
	        dbConn = DBUtility.createConnection();
	        
	        stmt = dbConn.createStatement();
	        String query = "SELECT * FROM player WHERE username = '" + username + "'";
	        System.out.println(query);
	        
	        ResultSet rs = stmt.executeQuery(query);
	        if(rs.isClosed())
	        	System.out.println("closed result set");
	        profile = new Profile();
	        while (rs.next()) {
	        	profile.setEmail(rs.getString("email"));
	        	profile.setUsername(rs.getString("username"));
	        	profile.setPassword(rs.getString("password"));
	        	profile.setFirstname(rs.getString("firstname"));
	        	profile.setLastname(rs.getString("lastname"));
	        	profile.setCreated(rs.getDate("created"));
	        	profile.setRecentActivity(rs.getTimestamp("recent_activity").toString());
	        }
		    //clean up environment
		    stmt.close();
		    dbConn.close();
        }catch (SQLException sqle){
        	sqle.printStackTrace();
        }catch (Exception e){
        	e.printStackTrace();
        }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
        
		if(profile == null)
			throw new DataNotFoundException("User: "+username+" not found!");
		return profile;
	}
	
	
	public static Profile addProfile(Profile profile){
		if(!isUnameAndEmailAvailable(profile.getUsername(), profile.getEmail()))
			throw new DuplicateEntryException("Username or Email is already registered!");
		int records = 0;
        Connection dbConn = null;
		Statement stmt = null;
		try {
			dbConn = DBUtility.createConnection();
			stmt = dbConn.createStatement();
			
            String query = "INSERT into player( username, email, firstname, lastname, password, created, recent_activity) "
            		+ "values('"+profile.getUsername()+ "',"+"'"+ profile.getEmail()+
            		"','"+profile.getFirstname() + "','"+profile.getLastname() +
            		"','"+profile.getPassword()+"', CURDATE(), UTC_TIMESTAMP() )";
            System.out.println(query);
            
            records = stmt.executeUpdate(query);
		    //clean up environment
		    stmt.close();
		    dbConn.close();
			
		}catch (SQLException sqle) {
			System.out.println("SQL State: "+sqle.getSQLState());
			System.out.println("SQL error code: " + sqle.getErrorCode());
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try

        if (records == 0)
        	return null;
        else
        	return getProfile(profile.getUsername());
	}
	
	public static Profile updateProfile(Profile profile, String username){
		Connection dbConn = null;
		Statement stmt = null;
		
		try {
			dbConn = DBUtility.createConnection();
			stmt = dbConn.createStatement();
			
			String query = "UPDATE player SET"+
					" username =" + "'" + profile.getUsername()+"'"+
					", email =" + "'" + profile.getEmail() + "'" +
            		", firstname =" + "'" + profile.getFirstname() + "'"+
            		", lastname =" + "'" + profile.getLastname() + "'" +
            		", password =" + "'" + profile.getPassword() + "'" +
            		", recent_activity =" + "UTC_TIMESTAMP()" +
            		" WHERE "+"username ="+"'" + username+"'";
            System.out.println(query);
            
			stmt.executeUpdate(query);
			 //clean up environment
		    stmt.close();
		    dbConn.close();
		    
        }catch (SQLException sqle){
        	sqle.printStackTrace();
        }catch (Exception e){
        	e.printStackTrace();
        }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		return getProfile(profile.getUsername());
	}
	
	public static void deleteProfile(String username){
	    Connection dbConn = null;
	    Statement stmt = null;
	    int count = 0;
	    try{
	        dbConn = DBUtility.createConnection();
	        
	        stmt = dbConn.createStatement();
	        String query = "DELETE FROM player WHERE username = '" + username + "'";
	        System.out.println(query);
	        
	        count = stmt.executeUpdate(query);
	        System.out.println("Deleted: " + count + " rows.");
		    //clean up environment
		    stmt.close();
		    dbConn.close();
	    }catch (SQLException sqle){
	    	sqle.printStackTrace();
	    }catch (Exception e){
	    	e.printStackTrace();
	    }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(dbConn!=null)
		        	dbConn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try

		if(count == 0)
			throw new DataNotFoundException("User: "+username+" not found!");
	}
	
	//CHECK AVAILABILITY FOR USER REGISTER
	public static boolean isUnameAndEmailAvailable(String username, String email){
		Connection conn = null;
		CallableStatement stmt = null;
		boolean result = false;
		try {
			conn = DBUtility.createConnection();
			stmt = conn.prepareCall("{call check_user_info(?,?,?)}");
			stmt.setString(1, username);
			stmt.setString(2, email);
			stmt.registerOutParameter(3, Types.INTEGER);
			
			stmt.execute();
			result = (stmt.getInt(3) == 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
		return result;
	}
	
	
	/**
	 * get Places For User
	 * @param username
	 * @return new Place with attrs(comment, user, sceneid, scene_name, date)
	 * 
	 */
	public static ArrayList<Place> getPlaces(String username){
		ArrayList<Place> places = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT s.name , pl.username , p.comment , p.created "+
			"FROM (places AS p JOIN player AS pl ON p.player_id = pl.player_id) JOIN scene AS s ON p.scene_id = s.scene_id "+
			"WHERE pl.username = '"+ username +"';";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				Place p = new Place();
				p.setComment(rs.getString(3));
				p.setUser(rs.getString(2));
				p.setSceneName(rs.getString(1));
				p.setCreated((rs.getDate(4)));
				places.add(p);
			}
			
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
		return places;
	}
	
	
	public static Place getPlace(String username, long sceneid){
		Place p = null;
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();

			String query = "SELECT s.name , pl.username , p.comment , p.created, s.scene_id "+
			"FROM (places AS p JOIN player AS pl ON p.player_id = pl.player_id) JOIN scene AS s ON p.scene_id = s.scene_id "+
			"WHERE pl.username = '"+ username +"' AND s.scene_id = '"+sceneid + "';";
			
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				p= new Place();
				p.setComment(rs.getString(3));
				p.setUser(rs.getString(2));
				p.setSceneName(rs.getString(1));
				p.setCreated((rs.getDate(4)));	
				p.setScene_id(rs.getLong(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
		
		return p;
	}
	
	public static boolean addPlace(String username, long sceneid){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String selectUser = "(SELECT player_id FROM player WHERE username = '"+username +"')";
			String query = "INSERT INTO places (player_id, scene_id, comment, created) VALUES("+selectUser+","+
			"'"+sceneid+"','"+"', CURDATE()"+");";
			
			if(stmt.executeUpdate(query) == 1){
				conn.close();
				stmt.close();
				return true;
			}
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			if(e.getClass().equals(MySQLIntegrityConstraintViolationException.class)){
				throw new ConstraintViolationException(e.getMessage());
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		return false;
	}
	
	public static boolean deletePlace(String username, long sceneid){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String selectUser = "(SELECT player_id FROM player WHERE username = '"+username +"')";
			String delete = "DELETE FROM places WHERE "+selectUser+" = '"+username+"' AND scene_id = '"+ sceneid+"';";
			
			if(stmt.executeUpdate(delete) == 1){
				conn.close();
				stmt.close();
				return true;
			}
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		return false;
	}
	
	
	public static boolean updatePlace( Place place){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String selectUser = "(SELECT player_id FROM player WHERE username = '"+place.getUser() +"')";
			String update = "UPDATE places SET places.comment = '" + place.getComment()+"', places.created = CURDATE()"
					+ "WHERE player_id = "+selectUser+" AND scene_id = '"+place.getScene_id()+"';";
	
			if(stmt.executeUpdate(update) == 1){
				conn.close();
				stmt.close();
				return true;
			}
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		return false;
	}
	
	/**
	 * GET Visits
	 * @param username
	 * @return ArrayList<Visits> or null if list is empty
	 */
	public static ArrayList<Visit> getVisits(String username){
		ArrayList<Visit> visits = new ArrayList<>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT s.name, pl.username , v.created, s.scene_id  "+
			"FROM (visits AS v JOIN player AS pl ON v.player_id = pl.player_id) JOIN scene AS s ON v.scene_id = s.scene_id "+
			"WHERE pl.username = '"+ username +"';";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				Visit v = new Visit();
				v.setScenename(rs.getString(1));
				v.setUsername(rs.getString(2));
				v.setCreated(rs.getDate(3));	
				v.setSceneid(rs.getLong(4));
				visits.add(v);
			}
			
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
		return visits;
	}
	
	
	
	
	public static Visit getVisit(String username, long sceneid){
		Visit v = null;
		Connection conn = null;
		Statement stmt = null;

		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();

			String query = "SELECT s.name , pl.username , v.created, s.scene_id "+
			"FROM (visits AS v JOIN player AS pl ON v.player_id = pl.player_id) JOIN scene AS s ON v.scene_id = s.scene_id "+
			"WHERE pl.username = '"+ username +"' AND v.scene_id = '"+sceneid + "';";
			
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				v = new Visit();
				v.setScenename(rs.getString(1));
				v.setUsername(rs.getString(2));
				v.setCreated(rs.getDate(3));	
				v.setSceneid(rs.getLong(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		
		
		return v;
	}
	
	public static boolean addVisit(String username, long sceneid){
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String selectUser = "(SELECT player_id FROM player WHERE username = '" +username +"')";
			String query = "INSERT INTO visits ( scene_id, player_id, created) VALUES('"+sceneid+"',"+selectUser+","+
			" CURDATE() );";
			
			if(stmt.executeUpdate(query) == 1){
				conn.close();
				stmt.close();
				return true;
			}
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			if(e.getClass().equals(MySQLIntegrityConstraintViolationException.class)){
				throw new ConstraintViolationException(e.getMessage());
			}
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		        	stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(conn!=null)
		        	 conn.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		return false;
	}
}
