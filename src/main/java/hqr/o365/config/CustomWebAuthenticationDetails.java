package hqr.o365.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {

	private static final long serialVersionUID = 1L;
	private String token = "";
	
	public CustomWebAuthenticationDetails(HttpServletRequest request) {
		super(request);
		token = request.getParameter("ggtoken");
	}
	
	public String getToken() {
        return token;
    }
	
	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(" [");
		sb.append("RemoteIpAddress=").append(this.getRemoteAddress()).append(", ");
		sb.append("SessionId=").append(this.getSessionId()).append(", ");
		sb.append("Token=").append(this.getSessionId()).append("]");
        return sb.toString();
    }
}
