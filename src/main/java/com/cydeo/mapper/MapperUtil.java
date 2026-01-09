package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperUtil {
    ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper){
        this.modelMapper=modelMapper;

    }

}
