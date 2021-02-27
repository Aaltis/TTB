package fi.breakwaterworks.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import fi.breakwaterworks.service.CustomUserDetailService;
import fi.breakwaterworks.service.CustomUserDetailService.CustomUserDetails;

public class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

	private final Logger logger = (Logger) LogManager.getLogger(AuthenticationTokenFilter.class);

	@Value("${auth.token}")
	private String tokenHeader;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		try {
			ParseTokenAuth(request);
			chain.doFilter(request, response);

		} catch (Exception ex) {
			String x = ex.getMessage();
			logger.error(x);
		}
	}

	private void ParseTokenAuth(ServletRequest request) {
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			String authToken = httpRequest.getHeader(this.tokenHeader);
			String username = this.tokenUtils.getUsernameFromToken(authToken);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				CustomUserDetails cUserDetails = this.userDetailsService.loadUserByUsername(username);
				if (this.tokenUtils.validateToken(authToken, cUserDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							cUserDetails, null, cUserDetails.getAuthorities());
					authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		} catch (Exception ex) {
			logger.error(ex);
		}
	}

}
