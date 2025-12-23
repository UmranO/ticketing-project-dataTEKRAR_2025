package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.ProjectService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final UserService userService;
    private final ProjectService projectService;

    public ProjectController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @GetMapping("/create")                                                            //Meaning send the request & see the  page
    public String createProject(Model model) {
                                                                                           //We'll see the below on the page:
        model.addAttribute("project", new ProjectDTO());                      //Empty Project Form
        model.addAttribute("managers", userService.listAllByRole("manager")); //DropDown-See all the Managers
        model.addAttribute("projects", projectService.listAllProjects());     //Show all the Projects in the Project List Table

        return "/project/create";

    }

//    @PostMapping("/create")
//    public String insertProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//
//            model.addAttribute("managers", userService.findManagers());
//            model.addAttribute("projects", projectService.findAll());
//
//            return "/project/create";
//
//        }
//
//        projectService.save(project);
//
//        return "redirect:/project/create";
//
//    }
//
//    @GetMapping("/delete/{projectCode}")
//    public String deleteProject(@PathVariable("projectCode") String projectCode) {
//        projectService.deleteById(projectCode);
//        return "redirect:/project/create";
//    }
//
//    @GetMapping("/complete/{projectCode}")
//    public String completeProject(@PathVariable("projectCode") String projectCode) {
//        projectService.complete(projectService.findById(projectCode));
//        return "redirect:/project/create";
//    }
//
//    @GetMapping("/update/{projectCode}")
//    public String editProject(@PathVariable("projectCode") String projectCode, Model model){
//
//        model.addAttribute("project", projectService.findById(projectCode));
//        model.addAttribute("managers", userService.findManagers());
//        model.addAttribute("projects", projectService.findAll());
//
//        return "/project/update";
//
//    }
//
//    @PostMapping("/update")
//    public String updateProject(@Valid @ModelAttribute("project") ProjectDTO project, BindingResult bindingResult, Model model) {
//
//        if (bindingResult.hasErrors()) {
//
//            model.addAttribute("managers", userService.findManagers());
//            model.addAttribute("projects", projectService.findAll());
//
//            return "/project/update";
//
//        }
//
//        projectService.update(project);
//
//        return "redirect:/project/create";
//
//    }
//
//    @GetMapping("/manager/project-status")
//    public String getProjectByManager(Model model) {
//
//        UserDTO manager = userService.findById("john@cydeo.com");
//        List<ProjectDTO> projects = projectService.getCountedListOfProjectDTO(manager);
//
//        model.addAttribute("projects", projects);
//
//        return "/manager/project-status";
//
//    }
//
//    @GetMapping("/manager/complete/{projectCode}")
//    public String managerCompleteProject(@PathVariable("projectCode") String projectCode) {
//        projectService.complete(projectService.findById(projectCode));
//        return "redirect:/project/manager/project-status";
//    }

}