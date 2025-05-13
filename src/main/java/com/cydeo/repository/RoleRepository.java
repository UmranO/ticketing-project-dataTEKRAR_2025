package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository  extends JpaRepository<Role, Long>{

     Role findByDescription(String description);

}
