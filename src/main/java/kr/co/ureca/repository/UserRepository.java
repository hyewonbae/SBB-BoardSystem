package kr.co.ureca.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.co.ureca.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	public Optional<SiteUser> findByUsername(String username);

}
