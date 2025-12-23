package com.cydeo.service;
import com.cydeo.dto.ProjectDTO;
import java.util.List;

public interface ProjectService {

    ProjectDTO getByProjectCode(String code);            //Since Service it reeturns DTO
    List<ProjectDTO> listAllProjects();                  //Since we have Table in the Project Create /UI
    void save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete(String code);

    void complete(String projectCode);


}
