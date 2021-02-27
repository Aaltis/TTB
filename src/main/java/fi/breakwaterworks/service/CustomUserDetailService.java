package fi.breakwaterworks.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.UserRepository;
import fi.breakwaterworks.config.InitialDataLoader;
import fi.breakwaterworks.model.User;
import fi.breakwaterworks.model.UserRole;
import fi.breakwaterworks.model.WorkLog;

@Service
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;
	static Logger log = (Logger) LogManager.getLogger(InitialDataLoader.class);

	@Autowired
	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = new User();
		user = userRepository.findByName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user " + username);
		}
		return new CustomUserDetails(user);
	}

	public CustomUserDetails LoadUserInfoIfUserIsLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();
			User user = userRepository.findByName(username);
			if (user == null) {
				throw new UsernameNotFoundException("Could not find user " + username);
			}
			return new CustomUserDetails(user);
		}
		return null;
	}

	public class CustomUserDetails implements UserDetails {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private User user;

		private CustomUserDetails(User user) {
			this.user = user;
		}

		@Override
		public String getUsername() {
			return user.getName();
		}

		@Override
		public String getPassword() {
			return user.getPassword();
		}

		public String getSalt() {
			return user.getSalt();
		}

		public Set<WorkLog> getWorkLogs() {
			return user.getWorkLogs();
		}

		public void addWorkLog(WorkLog worklog) {
			user.getWorkLogs().add(worklog);
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		public User getUser() {
			return this.user;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			for (final UserRole role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
			return authorities;
		}

	}

}
