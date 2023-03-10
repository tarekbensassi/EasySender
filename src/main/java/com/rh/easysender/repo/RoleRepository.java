package com.rh.easysender.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.rh.easysender.entity.*;

public interface RoleRepository extends CrudRepository<Role, Integer> {

}
