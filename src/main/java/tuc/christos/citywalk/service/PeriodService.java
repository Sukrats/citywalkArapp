package tuc.christos.citywalk.service;

import java.sql.*;
import java.util.ArrayList;

import tuc.christos.citywalk.database.DBUtility;
import tuc.christos.citywalk.exceptions.DataNotFoundException;
import tuc.christos.citywalk.model.Period;

public class PeriodService {
	
	public static ArrayList<Period> getAllPeriods(){
		ArrayList<Period> PeriodsList = new ArrayList<Period>();
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM period";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				Period temp = new Period();
				temp.setId(rs.getLong(1));
				temp.setName(rs.getString(2));
				temp.setDescription(rs.getString(3));
				
				temp.setStarted(rs.getString("started"));
				temp.setEnded(rs.getString("ended"));
				
				temp.setimagesLoc((rs.getString("images_loc") == null ? "" : rs.getString("images_loc")));
				PeriodsList.add(temp);
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
			
			
		
		if(PeriodsList.isEmpty())
			throw new DataNotFoundException("No periods Found!");
		return PeriodsList; 
	}
	
	
	public static Period getPeriod(long id){
		Period period = null;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT * FROM period WHERE period_id = '"+ id +"';";
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()){
				period = new Period();
				period.setId(rs.getLong(1));
				period.setName(rs.getString(2));
				period.setDescription(rs.getString(3));
				period.setStarted(rs.getString("started"));
				period.setEnded(rs.getString("ended"));

				period.setimagesLoc((rs.getString("images_loc") == null ? "" : rs.getString("images_loc")));
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
		
		if(period == null)
			throw new DataNotFoundException("Period with id: "+id+" not Found!");
		return period;
	}
	
	
	public static Period addPeriod(Period period){
		return new Period();
		
	}
	
	public static Timestamp checkSync(){
		Connection conn = null;
		Statement stmt = null;
		Timestamp time = null;
		try {
			conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String query = "SELECT update_time "
					+ "FROM   modifications "
					+ "WHERE  table_name = 'period' ";

			ResultSet rs = stmt.executeQuery(query);
			if(rs.next()){
				time = (rs.getTimestamp("update_time"));
			}
			if(time!= null){
				System.out.println(time.toString());
			}else{
				System.out.println("No time stamp");
			}
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	/*
	public static Period updatePeriod(Period Period){
		if(Period.getName().equals(""))
				return null;
		if(!periods.containsKey(Period.getId()))
			throw new DataNotFoundException("Period with id: "+Period.getId()+" not Found!");
		Period newPeriod = periods.put(Period.getId(),Period);
		return newPeriod;
	}
	
	
	public static Period deletePeriod(long id){
		return 	periods.remove(id);
	}*/
}