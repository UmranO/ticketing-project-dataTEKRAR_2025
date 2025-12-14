package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
//--------------------------------------------------------------------------------------------------------------------
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

//--------------------------------------------------------------------------------------------------------------------
    //-------O----------------------------------------------------
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return userMapper.convertToDto(user);}
//--------------------------------------------------------------------------------------------------------------------
    //------UO----------------------------------------------------
//    @Override
//    public UserDTO findByUserName(String username) {
//        return userMapper.convertToDto(userRepository.findByUserName(username));}
//--------------------------------------------------------------------------------------------------------------------

    public UserDTO update(UserDTO user) {

        //O: Find current user
        User user1 = userRepository.findByUserName(user.getUserName());  //user1 has id-
                                                                         //Update button'a bastigimizda Form'a
                                                                         //populate eden DTO'dan aldigimiz userName ile
                                                                         //DB'den ilgili User'i bul
        //Map updated UserDto to Entity object
        User convertedUser = userMapper.convertToEntity(user);           //Form'da Edit ettigin user which is a DTo'yu'i
                                                                         //Entity'ye cevir. Does it have id?-NO!
                                                                         //Yukarda DB'den buldugun Entity user1'da id var
        //set id to the converted object
        convertedUser.setId(user1.getId());                              //Form'da edit ettigimiz & Entity'ye cevirdigimiz
                                                                         //object(convertedUser)'a DB'den ilk once DB'den
                                                                         //userName ile buldugumuz user1 Entity'sine ait
                                                                         //id'yi set ediyoruz cunku bunu yapmazsak yeni
                                                                         //bir Entity olarak save ediyor. Oysa biz ayni
                                                                         //Entity'yi edit ettik & ayni id ile save etmeye
                                                                         //calisiyoruz.
        //save the updated user in the DB
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());  //Bu metodu da Security icin Returning olarak create ediyoruz.
                                                    //Degisiklikleri save ettikten sonra ayni userName'le ayni Entity'yi
                                                    //buluyoruz. Tek fark simdi updated olmasi.

    }

//--------------------------------------------------------------------------------------------------------------------
    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

//--------------------------------------------------------------------------------------------------------------------
    @Override
    public void deleteByUserName(String username) {

    }
}