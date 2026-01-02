package com.cydeo.service;

import com.cydeo.dto.TaskDTO;

import java.util.List;

public interface TaskService {                                       //Burasi service oldugu icin DTO ile calisiyor.
                                                                     //So verdigimiz/aldigimiz hersey DTO olmali!!

    //save
    void save(TaskDTO dto);

    //ListAll
    List<TaskDTO> listAllTasks();

    //Update
    void update(TaskDTO dto);

    //Delete
    void delete(Long id);

    //findById
    TaskDTO findById(Long id);

    int totalNonCompletedTask(String projectCode);  //For the Unfinished/Completed Column in the Project List in the Project Status page
    int totalCompletedTask(String projectCode);     //For the Unfinished/Completed Column in the Project List in the Project Status page

}
