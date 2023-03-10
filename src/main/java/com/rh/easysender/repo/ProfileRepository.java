package com.rh.easysender.repo;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rh.easysender.entity.Profile;



public interface ProfileRepository extends CrudRepository<Profile, Long> {

	
}
