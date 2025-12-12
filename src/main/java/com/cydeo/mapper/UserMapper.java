package com.cydeo.mapper;

import org.modelmapper.ModelMapper;

public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
