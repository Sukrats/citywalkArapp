package tuc.christos.citywalk.filters;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import tuc.christos.citywalk.database.DBUtility;
import tuc.christos.citywalk.model.ErrorMessage;


@Provider
public class BasicAuthFilter implements ContainerRequestFilter {
	
	private static final String AUTH_HEADER = "Authorization";
	private static final String AUTH_HEADER_PREFIX = "Basic ";
	private static final String SECURE_URL_PREFIX = "secure";
	private static final String LOGIN_PREFIX = "login";
	
	private static final String NO_AUTH = "No Authentication";
	private static final String AUTH_EMPTY = "Authorization credentials are empty";
	private static final String DB_ERROR = "Inernal DB Error on {Checklogin}";
	private static final String AUTH_PASSWORD = "Wrong Password";
	private static final String USER_DOES_NOT_EXIST = "Requested user does not exist";
	private static final String USER_DOES_NOT_EXIST_LOGIN = "Username/Email not recognised";
	private static final String AUTH_USER_MATCH = "You do not have permission to access this profile";


	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {

		if(requestContext.getUriInfo().getPath().contains(SECURE_URL_PREFIX)){
			
			List<String> auth = requestContext.getHeaders().get(AUTH_HEADER);
			if(auth == null || auth.size() <= 0){
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(NO_AUTH, Status.UNAUTHORIZED.getStatusCode(), "no documentation"))
						.build());
				return;
			}
			String authToken = auth.get(0);
			String encodedStr = authToken.replaceFirst(AUTH_HEADER_PREFIX, "");
			String decodedStr = Base64.decodeAsString(encodedStr);
			
			//if string only contains ':' abort
			if (decodedStr.equals(":")){				
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(AUTH_EMPTY, Status.UNAUTHORIZED.getStatusCode(), "no documentation"))
						.build());
				return;
			}
			String[] credentials = decodedStr.split(":");
			int action = (credentials[0].contains("@")?  1 : 2 );
			
			if(requestContext.getUriInfo().getPath().contains(LOGIN_PREFIX)){
				if(!DBUtility.CheckUser(credentials[0],action)){
					requestContext.abortWith(Response.status(Response.Status.NOT_FOUND)
							.header("Content-Type", "application/json")
							.entity(new ErrorMessage(USER_DOES_NOT_EXIST_LOGIN, Status.NOT_FOUND.getStatusCode(), "no documentation"))
							.build());
					return;
				}
				if(DBUtility.CheckLogin(credentials[0], credentials[1], action))
						return;
				
				requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(AUTH_PASSWORD, Status.UNAUTHORIZED.getStatusCode(), "no documentation"))
						.build());
				return;
					
			}
			//if requested user does not exist in DB abort
			String usrname = requestContext.getUriInfo().getPathParameters().getFirst("username");
			if(!DBUtility.CheckUser(usrname,action)){
				requestContext.abortWith(Response.status(Response.Status.NOT_FOUND)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(USER_DOES_NOT_EXIST, Status.NOT_FOUND.getStatusCode(), "no documentation"))
						.build());
				return;
			}
			//if username does not match requested user info abort   //check email or username
			if(action == 1 ? !DBUtility.matchUser(usrname, credentials[0]) : !usrname.equals(credentials[0])){
				requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(AUTH_USER_MATCH, Status.FORBIDDEN.getStatusCode(), "no documentation"))
						.build());
				return;
			}
			//if password does not match the user abort
			
			try {
				if(DBUtility.CheckLogin(credentials[0], credentials[1], action))
					return;
			} catch (Exception e) {
				e.printStackTrace();
				requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.header("Content-Type", "application/json")
						.entity(new ErrorMessage(DB_ERROR, Status.INTERNAL_SERVER_ERROR.getStatusCode(), "no documentation"))
						.build());
				return;
			}
			
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
					.header("Content-Type", "application/json")
					.entity(new ErrorMessage(AUTH_PASSWORD, Status.UNAUTHORIZED.getStatusCode(), "no documentation"))
					.build());
		}
		
	}

}