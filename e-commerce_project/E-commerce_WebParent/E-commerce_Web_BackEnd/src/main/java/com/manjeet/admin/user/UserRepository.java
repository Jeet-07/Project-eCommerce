package com.manjeet.admin.user;

import com.manjeet.admin.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.manjeet.common.entity.User;

public interface UserRepository extends SearchRepository<User,Integer> {
	@Query("SELECT u FROM User u WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id =?1")
	@Modifying
	public void updateEnableStatus(Integer id, boolean enabled);

	@Query("SELECT u FROM User u WHERE CONCAT(u.id,' ',u.firstName,' ',u.lastName,' ',u.email) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);

	@Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
	public Long countEnabled();
}
