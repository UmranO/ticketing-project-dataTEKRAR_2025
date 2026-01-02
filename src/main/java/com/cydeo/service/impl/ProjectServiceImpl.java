package com.cydeo.service.impl;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        ProjectDTO projectDTO = projectMapper.convertToDto(project);
        return projectDTO;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));  //We're getting All the Projects frm DB in a sorted way
        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList()); //Converting the Entities to Dtos and returning
    }

    @Override
    public void save(ProjectDTO dto) {                         //This dto doesn't have sattus now

        dto.setProjectStatus(Status.OPEN);                     //Bec. no field for status in UI. If we don't set it
        Project project = projectMapper.convertToEntity(dto);  //before we save, we'll get an error
        projectRepository.save(project);
    }

//    @Override
//    public void update(ProjectDTO dto) {                       //this dto is the edited dto coming from the FORM
//        //Get projectCode of dto (since unique)
//        //Find the Project from DB with that projectCode
//        //Get its unique id given by Postgres
//        //Convert dto to Entity
//        //Set that id to the newly converted dto(edited dto that came from the UI & converted to Entity)
//        //Get the Status since there's no field in UI to set it.
//        //Set the Status since there's no field in UI to set it. We do it manually by ourselves
//        //Save back in the DB
////--Step BY Step--------------------------------------------------------------------------------------------------------
////--Get projectCode of dto (since unique)-------------------------------------------------------------------------------
//        String projectCode=dto.getProjectCode();                             //Updated DTO with unique projectCode
//
////--Find the Project Entity from DB with that projectCode---------------------------------------------------
//        Project project=projectRepository.findByProjectCode(projectCode);   //Entity(project) now has the id
//
////--Get that Entity which is found from DB's unique id given by Postgres -----------------------------------------------
//        Long projectId=project.getId();
//
////--Convert-Map that dto to Entity--------------------------------------------------------------------------------------
//        Project dtoConvertedToEntity =projectMapper.convertToEntity(dto);
//
////--Set that id to the newly converted dto(edited dto that came from the UI & converted to Entity)----------------------
//        dtoConvertedToEntity.setId(projectId);
//
////--Get the Status since there's no field in UI to set it.--------------------------------------------------------------
//        Status status= project.getProjectStatus();
//
////--Set the Status since there's no field in UI to set it. We do it manually by ourselves-------------------------------
//        dtoConvertedToEntity.setProjectStatus(status);
//
////--Save back in the DB-------------------------------------------------------------------------------------------------
//        projectRepository.save(dtoConvertedToEntity);
//    }

//Update() in short:O'nunki---------------------------------------------------------------------------------------------
        @Override
        public void update(ProjectDTO dto) {

            Project project = projectRepository.findByProjectCode(dto.getProjectCode()); //dto'dan aldigimiz projectCode'la
                                                                                         //gidip DB'den ilgili Project'i bulduk.
            Project convertedProject = projectMapper.convertToEntity(dto);               //Conver ettigimiz dto'yu convertedProject'ya assign ettik

            convertedProject.setId(project.getId());                                     //Sonra bu convertedProject'ya DB'de buldugumuz project'in id'sini set ettik

            convertedProject.setProjectStatus(project.getProjectStatus());               //Sonra bu convertedProject'ya DB'de buldugumuz project'in status'unu set ettik

            projectRepository.save(convertedProject);                                    //En sonda da herseyi tam olan convertedProject'yu save ettik.

//Since we don't have any object as a field/No need to create any relation for ProjectDTO/So no id field in ProjectDTO is needed like we did in UserDto & ProjectDto
}

    @Override
    public void delete(String code) {

        Project project = projectRepository.findByProjectCode(code);   //We're doing a soft Delete so
        project.setIsDeleted(true);                                    //we're not actually deletting. We're only

        project.setProjectCode(project.getProjectCode() + "-" + project.getId());  // SP03-4
                                                                       //We added this line bec if we delete a project
                                                                       //and if we want to use that project code,the deleted
                                                                       //project should no longer be same. depends on Comp. policy
        projectRepository.save(project);                               //setting the isDeleted field to true so that we
                                                                       //can still see it in the DB BUT not in UI
    }
                                                                       //Basically bring the project from DB, change the
                                                                       //isDeleted to true & save it.Similar to User delete
    @Override
    public void complete(String projectCode) {
        Project project= projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

    }
    @Override
     public List<ProjectDTO> listAllProjectDetails() {

//Capture User who is logged in the system with Security.For now find the User with userName="harold@manager.com":
             UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");

//I need to go to the DB & I need to get all the Projects assigned to this manager who logged in the system
//So convert that UserDTO to Entity:
             User user = userMapper.convertToEntity(currentUserDTO);

//Find All the Projects belong to that User with the help of findAllByAssignedManager(user)                   with Status details of Tasks
             List<Project> list = projectRepository.findAllByAssignedManager(user);

             return list.stream().map(project -> {

                         ProjectDTO obj = projectMapper.convertToDto(project);
//----------------------------------------------------------------------------------------------------------------------
//The below are the fields in the ProjectDTO/ We don't have those fields in the Project Entity
// private int completeTaskCounts;
// private int unfinishedTaskCounts;
//----------------------------------------------------------------------------------------------------------------------
                         obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                         obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));

                         return obj;
                     }

             ).collect(Collectors.toList());
         }
//--Yukardakinin commentsiz hali----------------------------------------------------------------------------------------
//    @Override
//    public List<ProjectDTO> listAllProjectDetails() {
//
//        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");
//        User user = userMapper.convertToEntity(currentUserDTO);
//
//        List<Project> list = projectRepository.findAllByAssignedManager(user);
//        return list.stream().map(project -> {
//                    ProjectDTO obj = projectMapper.convertToDto(project);
//                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
//                    obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
//                    return obj;
//                }
//        ).collect(Collectors.toList());
//         }
}

