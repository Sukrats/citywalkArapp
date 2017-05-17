package tuc.christos.citywalk.database;

import java.sql.*;
import tuc.christos.citywalk.model.Profile;
 
public class DBUtility {
	
	public static final int LOGIN_EMAIL = 1;
	public static final int LOGIN_USERNAME = 2;
    /**
     * Method to create DB Connection
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("finally")
    public static Connection createConnection() throws Exception {
        Connection con = null;
        try {
			System.out.println("Connecting to database...");
            Class.forName(Constants.JDBC_DRIVER);
            con = DriverManager.getConnection(Constants.dbUrl, Constants.dbUser, Constants.dbPwd);
        }catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
            return con;
        }
    }
    /**
     * Method to check whether uname and pwd combination are correct
     * 
     * @param uname
     * @param pwd
     * @return
     * @throws Exception
     */
    public static boolean CheckLogin(String uname, String pwd, int action)  {
        boolean result = false;
        Connection dbConn = null;
        CallableStatement  stmt = null;
        try {
            dbConn = DBUtility.createConnection();
            System.out.println("Authenticating...");

            if(action == LOGIN_EMAIL){
                stmt = dbConn.prepareCall("{call login_user_email(?, ?, ?)}");
            }
            else if(action == LOGIN_USERNAME){
                stmt = dbConn.prepareCall("{call login_user_username(?, ?, ?)}");
            }
			stmt.setString(1, uname);
			stmt.setString(2, pwd);
			stmt.registerOutParameter(3, Types.INTEGER);
			stmt.execute();
			result = (stmt.getInt(3) > 0);
			
            dbConn.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (Exception e) {
            // 
            if (dbConn != null) {
                try {
					dbConn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
            }
            e.printStackTrace();
        } finally {
            if (dbConn != null)
				try {
					dbConn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
        }
        return result;
    }
    
    /**
     * Method to check if a user exists in DB
     * @param username
     * @return boolean
     */
    public static boolean CheckUser(String cred, int action){
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
        	conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			String query = "";
            if(action == LOGIN_EMAIL)
            	query = "SELECT username FROM player WHERE email = '" + cred+"'";
            else if(action == LOGIN_USERNAME)
            	query = "SELECT username FROM player WHERE username = '" + cred+"'";
            
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
				return true;
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
            if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
        }
    	return false;
    }
    public static boolean matchUser(String username, String email){
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
        	conn = DBUtility.createConnection();
			stmt = conn.createStatement();
			
			String query = "SELECT username FROM player WHERE email = '"+email+"' AND username = '"+username+"';";
			ResultSet rs = stmt.executeQuery(query);
			if(rs.next())
				return true;
			conn.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
            if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
            if(stmt != null)
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
        }
    	return false;
    }
    
    /**
     * Method to insert uname and pwd in DB
     * 
     * @param name
     * @param uname
     * @param pwd
     * @return
     * @throws SQLException
     * @throws Exception
     */
    public static Profile RegisterUser(Profile player) throws SQLException, Exception {
        boolean insertStatus = false;
        Connection dbConn = null;
		Statement stmt = null;
        try {
            
            dbConn = DBUtility.createConnection();
            
            stmt = dbConn.createStatement();
            
            String query = "INSERT into player( username, email, firstname, lastname, password, created) "
            		+ "values('"+player.getUsername()+ "',"+"'"+ player.getEmail()+
            		"','"+player.getFirstname() + "','"+player.getLastname() +
            		"','"+player.getPassword()+"',' CURDATE()')";
            System.out.println(query);
            
            int records = stmt.executeUpdate(query);
            //System.out.println(records);
            //When record is successfully inserted
            if (records > 0) {
                insertStatus = true;
            }
		    //clean up environment
		      stmt.close();
		      dbConn.close();
        } catch (SQLException sqle) {
            //sqle.printStackTrace();
            throw sqle;
        } finally{
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
		   System.out.println("WHALECUM!!");
		   if(insertStatus)
			   return player;
		   else
			   return null;
    }
    
    
    
}