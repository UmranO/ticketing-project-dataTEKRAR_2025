package com.cydeo.service;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;


import java.util.List;

public interface UserService  {

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO user);
    void deleteByUserName(String username);     //this deletes from both the DB & the UI
    UserDTO update(UserDTO user);
    void delete(String username);               //we added this to show how to keep in DB but delete from UI

    //Asagidakini sonra yazdik
    List<UserDTO> listAllByRole(String role);

}