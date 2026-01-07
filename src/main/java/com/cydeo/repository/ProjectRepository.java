package com.cydeo.repository;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {  //Here we give the Entity that will be working
                                                                          //with this Repo.& the Primary is Long(DB icin)
    Project findByProjectCode(String code);                               //Here the Project Code is Unique & it returns
                                                                          //Entity(Buradaki uniqe UI icin)


    List<Project> findAllByAssignedManager(User manager);                 //gives all the projects that are assigned to a certain User

    List<Project> findAllByProjectStatusIsNotAndAssignedManager(Status status, User assignedManager);

}
