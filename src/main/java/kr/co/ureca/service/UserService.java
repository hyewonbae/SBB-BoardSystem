package kr.co.ureca.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.ureca.entity.SiteUser;
import kr.co.ureca.excption.DataNotFoundException;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	public SiteUser create(String username, String password, String email) {
		SiteUser siteUser = new SiteUser();
		siteUser.setUsername(username);
		siteUser.setEmail(email);
		siteUser.setPassword(passwordEncoder.encode(password));
		SiteUser savedSiteUser = userRepository.save(siteUser);

		return savedSiteUser;
	} // create

	public SiteUser getUser(String username) {
		Optional<SiteUser> opSiteUser = userRepository.findByUsername(username);
		if(opSiteUser.isPresent()) {
			return opSiteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	} // getUser

} // class







