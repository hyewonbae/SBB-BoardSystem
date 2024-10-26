package kr.co.ureca.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.ureca.dto.UserRole;
import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSecurityService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<SiteUser> opSiteUser = userRepository.findByUsername(username);

		if( opSiteUser.isEmpty() ) {
			throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
		}

		SiteUser siteUser = opSiteUser.get();

		List<GrantedAuthority> authorities = new ArrayList<>();
		if(username.equals("admin")) {
			authorities.add( new SimpleGrantedAuthority( UserRole.ADMIN.getValue() ) );
		} else {
			authorities.add( new SimpleGrantedAuthority( UserRole.USER.getValue() ) );
		}

		return new User( siteUser.getUsername(), siteUser.getPassword(), authorities );
	}

}
