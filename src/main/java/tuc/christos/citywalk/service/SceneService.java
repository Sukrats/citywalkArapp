package tuc.christos.citywalk.service;

import java.sql.*;
import java.util.ArrayList;

import tuc.christos.citywalk.database.DBUtility;
import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.Scene;
import tuc.christos.citywalk.model.tempScenes;

public class SceneService {

	public static ArrayList<Scene> getAllScenes(){
		ArrayList<Scene> ScenesList = new ArrayList<>();
		
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM scene";
			
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				Scene temp = new Scene();
				temp.setId(rs.getLong(1));
				temp.setName(rs.getString(2));
				temp.setDescription(rs.getString(3));
				temp.setLatitude(rs.getDouble(4));
				temp.setLongitude(rs.getDouble(5));
				temp.setNumOfVisits(rs.getInt(6));
				temp.setPeriodId(rs.getLong(7));
				
				temp.setThumb((rs.getString("thumb_loc") == null ? "" : rs.getString("thumb_loc")));
				temp.setImgLoc((rs.getString("images_loc") == null ? "" : rs.getString("images_loc")));
				ScenesList.add(temp);
			}
			
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		
		if(ScenesList.isEmpty())
			throw new DataNotFoundException("No scenes found!");
		
		return ScenesList; 
	}
	
	public static ArrayList<Scene> getScenesForPeriod(long periodId){
		ArrayList<Scene> scenesList = new ArrayList<Scene>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String query = "SELECT * FROM scene WHERE period_id = '"+periodId+"';";
			
			if(stmt.execute(query))
			{
				ResultSet rs = stmt.getResultSet();
				while(rs.next()){
					Scene temp = new Scene();
					temp.setId(rs.getLong("scene_id"));
					temp.setName(rs.getString("name"));
					temp.setDescription(rs.getString("description"));
					temp.setLatitude(rs.getDouble("latitude"));
					temp.setLongitude(rs.getDouble("longitude"));
					temp.setNumOfVisits(rs.getInt("numofvisits"));
					temp.setPeriodId(rs.getLong("period_id"));
					
					temp.setThumb((rs.getString("thumb_loc") == null ? "" : rs.getString("thumb_loc")));
					temp.setImgLoc((rs.getString("images_loc") == null ? "" : rs.getString("images_loc")));
					scenesList.add(temp);
				}
			}
			
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
		
		
		if(scenesList.isEmpty())
			throw new DataNotFoundException("Scenes for period with id: "+periodId+" not found!");
		return scenesList;
	}
	
	public static Scene getScene(long id){
		Scene temp = new Scene();
		boolean empty = false;
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM scene WHERE scene_id = '"+id+"';";
			
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())			
			{
				empty = true;
				temp.setId(rs.getLong(1));
				temp.setName(rs.getString(2));
				temp.setDescription(rs.getString(3));
				temp.setLatitude(rs.getDouble(4));
				temp.setLongitude(rs.getDouble(5));
				temp.setNumOfVisits(rs.getInt(6));
				temp.setPeriodId(rs.getLong(7));
				
				temp.setThumb((rs.getString("thumb_loc") == null ? "" : rs.getString("thumb_loc")));
				temp.setImgLoc((rs.getString("images_loc") == null ? "" : rs.getString("images_loc")));
			}
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			
			
		if(!empty)
			throw new DataNotFoundException("Scene with id "+ id +" not found!");
		return temp;
	}
	
	public static Scene addScene(Scene scene){
		
		return new Scene();
		
	}
	
	public static boolean addScenes(tempScenes scenes){
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DBUtility.createConnection();
			
			stmt = conn.createStatement();
			String query = "";
			for(Scene temp: scenes.getScenes()){
				String tempQ= "INSERT INTO scene ( name , description , latitude , longitude , numofvisits , period_id ) "
						+ "VALUES ( "+ "'" + temp.getName() + "',"+
						"'"+temp.getDescription() +"',"+
						"'"+temp.getLatitude() +"',"+
						"'"+temp.getLongitude() +"',"+
						"'"+ 0 +"',"+
						"'"+temp.getPeriodId() +"');";
				query +=tempQ;
			}
			System.out.println(query);
			if(!query.equals("")){
				int count = stmt.executeUpdate(query);
				System.out.print(count);
				if(count > 0){
					return true;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
	
	public static Scene updateScene(Scene scene){
		return new Scene();
	}
	
	public static Scene deleteScene(long id){
		return 	new Scene();
	}
	
	
}
