package com.cydeo.repository;
import com.cydeo.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {  //Here we give the Entity that will be working
                                                                          //with this Repo.& the Primary is Long(DB icin)
    Project findByProjectCode(String code);                               //Here the Project Code is Unique & it returns
                                                                          //Entity(Buradaki uniqe UI icin)
}
