package com.cydeo.service.impl;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {  //By injection we're doing memory-level object reference assignment.
        this.taskRepository = taskRepository;                                       //It holds a reference to an existing object
        this.taskMapper = taskMapper;                                               //So we can call methods on it immediately
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public List<TaskDTO> listAllTasks() {       //we're getting all the tasks from TaklkRepo/stream/mapping each to a DTO/Collect to a List
        return taskRepository.findAll().stream().map(taskMapper::convertToDto).collect(Collectors.toList());
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public void save(TaskDTO dto) {

        dto.setTaskStatus(Status.OPEN);              //Since we don't have a place to give the status in the Form we're setting the status
        dto.setAssignedDate(LocalDate.now());        //Since we don't have a place to give the status in the Form we're setting AssignedDate
        Task task = taskMapper.convertToEntity(dto); //mapping Dto to Entity
        taskRepository.save(task);                   //saving to DB
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());      //Repo's findById(Long Id) returns Optional

        Task convertedTask = taskMapper.convertToEntity(dto);            //We convert the editted dto to entity

//--ilk once bunu yapmistik ama eger we want to Complete the task then the below code is wrong diye alttakini yazdik----
        //      if (task.isPresent()) {                                          //Since optional
        //          convertedTask.setTaskStatus(task.get().getTaskStatus());     //Setting the status which is missing in the Form
        //          convertedTask.setAssignedDate(task.get().getAssignedDate()); //Setting the AssignedDate which is missing in the Form
        //          taskRepository.save(convertedTask);                         //saving to DB
        //      }
//--Sonraki-------------------------------------------------------------------------------------------------------------
        if(task.isPresent()){
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        }
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public void delete(Long id) {

        Optional<Task> foundTask = taskRepository.findById(id);          //Repo's findById(Long Id) returns Optional

        if (foundTask.isPresent()) {                                     //Since optional
            foundTask.get().setIsDeleted(true);                          //Doing a soft delete
            taskRepository.save(foundTask.get());                        //with get() at the end we Optional->Task
        }
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public TaskDTO findById(Long id) {

        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            return taskMapper.convertToDto(task.get());
        }
        return null;                                                    //If not present return null
    }
//---------------------------------------------------------*------------------------------------------------------------
    //Asagida TaskRepository'de create ettigimiz projectCode'na bagli olarak buldugu specific bir projenin Unfinished &
    //Finished Task'lerini sayan ()'lari call ediyoruz.

    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }
  //----------------------------------------------------------------
    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.forEach(task -> delete(task.getId()));                     //delete oldugu icin dto'ya cevirmek gerekmedi
    }                                                                    //Oysa asagida:Status'leri update ettigimiz icin
                                                                         //stream's map ile teker teker Dto'ya cevirip
                                                                         //tek tek status'u COMPLETE yapiyoruz.
//---------------------------------------------------------*------------------------------------------------------------
    @Override
    public void completeByProject(ProjectDTO projectDTO) {                //update-status durumu oldugu icin it's ok to pass Entity instead of id or userName..
        Project project = projectMapper.convertToEntity(projectDTO);      //ProjectDto'yu Entity'ye ceviriyoruz ki
        List<Task> tasks = taskRepository.findAllByProject(project);      //taskRepository'de Project Entity'yi pass ederek
        tasks.stream().map(taskMapper::convertToDto)                      //tum task'lari bulabilelim. Burada Derived Q
                                                                          //oldugu icin it's ok to pass Entity instead of id or userName..
                .forEach(taskDTO -> {
                    taskDTO.setTaskStatus(Status.COMPLETE);
                    update(taskDTO);
                });


    }
//----------------------------------------------------------------------------------------------------------------------
//Asagisi CT'nin Pending Task icin yaptigi. Benimki  hemen altta ve iyi calisiyor, ama onunkini tutacagim:
    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        return null;
    }

//Asagisi benim Pending Task icin yaptigim. CT farkli yapmis. Bunu comment edecegim yukardaki CT'nin ki-----------------
//    @Override
//    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
//        UserDTO loggedInUser = userService.findByUserName("UMONAL@YAHOO.COM");
//        User loggedInEntity=userMapper.convertToEntity(loggedInUser);
//
//        List<TaskDTO> listStatusIsNot= taskRepository.findAllByAssignedEmployee(loggedInEntity).stream()
//                .map(taskMapper::convertToDto)
//                .filter(task->task.getTaskStatus() !=status)
//                .collect(Collectors.toList());
//
//        return listStatusIsNot;
//    }
//----------------------------------------------------------------------------------------------------------------------


}
