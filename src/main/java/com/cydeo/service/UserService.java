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

    //Asagidakini sonra yazdik cunku Project Create de dop down'da mevcut Manager'lari gormemiz lazim. Manager'da bir
    //User olduguna gore onu burada Create etmemiz gerekiyor:

    List<UserDTO> listAllByRole(String role);

}