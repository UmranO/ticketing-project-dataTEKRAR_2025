package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

//--------------------------------------------------------------------------------------------------------------------
    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        return userMapper.convertToDto(user);
    }
//--------------------------------------------------------------------------------------------------------------------
    public UserDTO update(UserDTO user) {

                                                                                                  //O: Find current user
    User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);    //user1 has id-
                                                                        //Update button'a bastigimizda Form'a
                                                                        //populate eden DTO'dan aldigimiz userName ile
                                                                        //DB'den ilgili User'i bul
                                                                        //Map updated UserDto to Entity object
        User convertedUser = userMapper.convertToEntity(user);          //Form'da Edit ettigin user which is a DTo'yu'i
                                                                        //Entity'ye cevir. Does it have id?-NO!
                                                                        //Yukarda DB'den buldugun Entity user1'da id var
                                                                        //set id to the converted object
        convertedUser.setId(user1.getId());                             //Form'da edit ettigimiz & Entity'ye cevirdigimiz
                                                                        //object(convertedUser)'a DB'den ilk once DB'den
                                                                        //userName ile buldugumuz user1 Entity'sine ait
                                                                        //id'yi set ediyoruz cunku bunu yapmazsak yeni
                                                                        //bir Entity olarak save ediyor. Oysa biz ayni
                                                                        //Entity'yi edit ettik & ayni id ile save etmeye
                                                                        //calisiyoruz.
                                                                        //save the updated user in the DB
        userRepository.save(convertedUser);

            return findByUserName(user.getUserName());  //Bu metodu da Security icin Returning olarak create ediyoruz.
                                                        //Degisiklikleri save ettikten sonra ayni userName'le ayni
                                                        //Entity'yi buluyoruz. Tek fark simdi updated olmasi.

    }
//--------------------------------------------------------------------------------------------------------------------
    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }
//--------------------------------------------------------------------------------------------------------------------
//    @Override
//    public void deleteByUserName(String username) {
        //UO----------------------------------------------------------------------------------------------------------------
        //UserController'daki public String deleteUser(@PathVariable("username") String username) metodunda
        //capture ettigin username'i o metodun icindeki UserService ait olan deleteByUserName(username);'e geciriyorsun
        //Ve userService.deleteByUserName(username); 'u call ettiginde O da UserServiceImp'de UserRepository'den
        //deleteByUsername(username)'i call ediyor ve senin UserService aktarmis oldugun unique username'i pass ediyor-
        //Asagida. VE O userName ile DB'den corresponding User Entity'yi buluyor.
        //Sonra bu Entity'yi delete ediyorsun.

        //User user= userRepository.findByUserName(username);
        //userRepository.delete(user);}

        //*************************************************************************************************************

//O ise hazirda deleteByUserName yok diye bunu create edelim dedi. Yukardaki benim kodum.Cunku hazirda deleteByUser vardi.
//Denedim calisti ama Sinifla ayni olsun diye asagida O'nunkini koydum.
//        @Override
//        public void deleteByUserName(String username) {
//        userRepository.deleteByUserName(username);                //HardDelete. We don't use it so can comment it Day 26
//    }
    //Bu yukardaki public void deleteByUserName(String username) {} hem DB'de hem de Ui'da delete ediyor User'i oysa
    //Company'lerde hicbirsey delete edilmiyor. O yuzden Base Entity'ye bir isDeleted diye bir field ekledik ve bunu
    //false'a set ettik. Eger bu field'in degeri false oldugu surece delete edilmemis oluyor. Ayni zamanda DB'de duruyor.
    //Ama eger isDeleted true olursa o zaman UI'da gorunmuyor(bunun icin baska bir sey daha yapilmali Don't forget) ama
    //DB'de deleted true oluyor.

    //O yuzden yukardaki yerine su anda asagidakini kullaniyoruz.  *****

    @Override
    public void delete(String username) {                         //*****Soft Delete
                                                                  //Go to DB & get that user with the given username
                                                                  //change the isDeleted field to true
                                                                  //save the object in the DB

        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());  // harold@manager.com-2
            userRepository.save(user);
        }
    }

//Asagisis Benimki Calisti Ama I'll keep O's:---------------------------------------------------------------------------------------------------

//    public List<UserDTO> listAllByRole(String role) {
//        //Find all the User Entities
//        //Filter the Users who are managers
//        //Convert them to DTOs
//        //Return them as a List of UserDtos
//        List<User> users=userRepository.findAll();
//        return users.stream().filter(user -> user.getRole().getDescription().equalsIgnoreCase(role)).map(userMapper::convertToDto).collect(Collectors.toList());
//    }
//Asagisi Ozzy's:---------------------------------------------------------------------------------------------------

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);  //Find all the Users who are ....
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());                    //Convert them to UserDtos and return them
    }
//--------------------------------------------------------------------------------------------------------------------
//The method's purpose is this:I want to check if I can delete that User or not. private cunku baska bir yerde kullanilmayacak

    private boolean checkIfUserCanBeDeleted(User user) {

    switch (user.getRole().getDescription()) {     //we use switch case to figure out if the User is Manager or Employee
        case "Manager":
            List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));
            return projectDTOList.size() == 0;
        case "Employee":
            List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
            return taskDTOList.size() == 0;
        default:
            return true;
    }
}
//Yukarda once user type'i buluyoruz. Sonra bu logged in user icin noncompleted tasks/project'leri icine koydugumuz
//listenin sifirdan(O) buyuk olup olmadigini kontrol ediyoruz. Eger buyukse bitmemis task/project var demektir, o zaman da
//0'a esit olmayacagi icin askDTOList.size() == 0 veya projectDTOList.size() == 0 false return edecek eger esitse o zaman da
//default value yani true olacak. Ben butun bu logic'i delete'in icine koymustum ama boylesi daha temiz ve dogru.
}
