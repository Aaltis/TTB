package fi.breakwaterworks.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import fi.breakwaterworks.DAO.RoleRepository;
import fi.breakwaterworks.DAO.UserRepository;
import fi.breakwaterworks.config.security.acl.dao.AclSidRepository;
import fi.breakwaterworks.config.security.acl.model.AclSid;
import fi.breakwaterworks.model.Privilege;
import fi.breakwaterworks.model.User;
import fi.breakwaterworks.model.UserRole;
import fi.breakwaterworks.model.request.UserRequest;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private AclSidRepository aclSidRepo;

	@Autowired
	private RoleRepository rRepo;

	public User CreateUser(String username, String password) {
		UserRole userRole = rRepo.findByName("ROLE_USER");
		Set<UserRole> roleSet = new HashSet<UserRole>(); 
		roleSet.add(userRole); 
		User user = userRepo.save(new User(username, User.GeneratePassword(password), roleSet));
		aclSidRepo.save(new AclSid(1, user.getName()));
		return user;
	}

	/*
	 * public UserDetails GetUserByName(String name) // throws
	 * UsernameNotFoundException {
	 * 
	 * User user = uRepo.findByName(name); if (user == null &&
	 * IsAllowedToUserInfo(user)) { return new
	 * org.springframework.security.core.userdetails.User( " ", " ", true, true,
	 * true, true, getAuthorities(Arrays.asList( rRepo.findByName("ROLE_USER")))); }
	 * 
	 * return new org.springframework.security.core.userdetails.User(
	 * user.getEmail(), user.getPassword(),true, true, true, true,
	 * getAuthorities(user.getRoles())); }
	 */

	private boolean IsAllowedToUserInfo(User user) {
		User userContext = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (userContext.getId() == user.getId())
			return true;
		else {
			return false;
		}
	}
	public void DeleteUserDataWithName(String name) throws Exception {
		 userRepo.deleteByName(name);
		 
		 aclSidRepo.deleteBySID(name);
	}

	public User GetUserByName(String name) throws Exception {
		return userRepo.findByName(name);
		/*
		 * boolean access = IsAllowedToUserInfo(user); if (user == null || access ==
		 * false) { throw new Exception(); } else { return new
		 * org.springframework.security.core.userdetails.User(user.getEmail(),
		 * user.getPassword(), true, true, true, true, (Collection<? extends
		 * GrantedAuthority>) getAuthorities(user.getRoles())); }
		 */
	}
	
	/*public Optional<User> GetUserById(String id) throws Exception {
		return userRepo.findById(Long.parseLong(id));
		
	}*/
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<UserRole> roles) {

		return getGrantedAuthorities(getPrivileges(roles));
	}

	private List<String> getPrivileges(Collection<UserRole> roles) {

		List<String> privileges = new ArrayList<>();
		List<Privilege> collection = new ArrayList<>();
		for (UserRole role : roles) {
			collection.addAll(role.getPrivileges());
		}
		for (Privilege item : collection) {
			privileges.add(item.getName());
		}
		return privileges;
	}

	private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (String privilege : privileges) {
			authorities.add(new SimpleGrantedAuthority(privilege));
		}
		return authorities;
	}

}
