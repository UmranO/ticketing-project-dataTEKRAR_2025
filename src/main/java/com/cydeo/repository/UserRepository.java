package com.cydeo.repository;

import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.Negative;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUserName(String username);
//------------------------------------------------------------------------------------------------------------
    @Transactional
    void deleteByUserName(String username);


//    Key rule in Spring Data JPA: All modifying queries require a transaction
//        This includes:
//        •	DELETE
//        •	UPDATE
//        •	BULK operations

//Built-in CRUD methods
//  are Already annotated with @Transactional
//Derived modifying queries:
//  NOT transactional by default
//  YOU must declare transactional behavior
//------------------------------------------------------------------------------------------------------------

    List<User> findByRoleDescriptionIgnoreCase(String description);
}