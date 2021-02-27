package fi.breakwaterworks.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import fi.breakwaterworks.config.security.TokenUtils;
import fi.breakwaterworks.model.AuthenticationResponse;
import fi.breakwaterworks.model.User;
import fi.breakwaterworks.model.request.AuthenticationRequest;
import fi.breakwaterworks.model.request.UserRequest;
import fi.breakwaterworks.service.CustomUserDetailService;
import fi.breakwaterworks.service.UserService;
import fi.breakwaterworks.service.CustomUserDetailService.CustomUserDetails;

@RestController
@RequestMapping(value = "/api/authentication")
public class AuthenticationController {

	private final Logger logger = (Logger) LogManager.getLogger(AuthenticationController.class);

	@Value("${auth.token}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private CustomUserDetailService userDetailsService;

	@Autowired
	private UserService userService;

	@PostMapping(consumes = "application/json")
	public ResponseEntity<?> authenticationRequest(@RequestBody AuthenticationRequest authenticationRequest)
			throws AuthenticationException {

		// Perform the authentication
		try {
			Authentication authentication = this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
							authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			CustomUserDetails userDetails = this.userDetailsService
					.loadUserByUsername(authenticationRequest.getUsername());

			// Reload password post-authentication so we can generate token
			String token = this.tokenUtils.generateToken(userDetails);

			// Return the token
			return ResponseEntity.ok(new AuthenticationResponse(token));
		} catch (Exception ex) {
			logger.error(ex);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/refresh", method = RequestMethod.GET)
	public ResponseEntity<?> authenticationRequest(HttpServletRequest request) {
		String token = request.getHeader(this.tokenHeader);
		String username = this.tokenUtils.getUsernameFromToken(token);
		CustomUserDetails cUserDetails = this.userDetailsService.loadUserByUsername(username);
		User user = cUserDetails.getUser();
		if (this.tokenUtils.canTokenBeRefreshed(token, user.getLastPasswordReset())) {
			String refreshedToken = this.tokenUtils.refreshToken(token);
			return ResponseEntity.ok(new AuthenticationResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@RequestMapping(value = "/register", consumes = "application/json", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> RegisterUser(@RequestBody UserRequest userRequest) {

		try {
			User existingUser = userService.GetUserByName(userRequest.getName());
			if (existingUser != null) {
				return new ResponseEntity<>("Username exists.", HttpStatus.BAD_REQUEST);
			}
			User user = userService.CreateUser(userRequest);

			Authentication authentication = this.authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userRequest.getName(), userRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			CustomUserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getName());

			// Reload password post-authentication so we can generate token
			String token = this.tokenUtils.generateToken(userDetails);

			// Return the token
			return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.CREATED);

		} catch (Exception ex) {
			logger.error(ex.toString());
			// return (ResponseEntity<AuthenticationResponse>) ResponseEntity.badRequest();
		}
		return null;
	}

}
