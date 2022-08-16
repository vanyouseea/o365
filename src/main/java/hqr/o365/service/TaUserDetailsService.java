package hqr.o365.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import hqr.o365.dao.TaUserRepo;
import hqr.o365.domain.TaUser;

@Component
public class TaUserDetailsService implements UserDetailsService{

	@Autowired
	private TaUserRepo tup;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		TaUser taUser = tup.findByUserId(username);
		if(taUser!=null) {
			return new User(username, taUser.getPasswd(),AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
		}
		else {
			return null;
		}
	}

}
