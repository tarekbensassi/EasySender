package com.rh.easysender.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rh.easysender.entity.User;


public interface UserRepository extends CrudRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserByUsername(@Param("username") String username);
	
	 
	    
	    User findTopByOrderByIdDesc();
		 
		   public User findUserByUsername(String username);
		   public User findUserByEmail(String email);
		   
		   @Query("SELECT c FROM User c WHERE c.email = ?1")
		    public User findByEmail(String email); 
		     
		    public User findByResetPasswordToken(String token);
} 
