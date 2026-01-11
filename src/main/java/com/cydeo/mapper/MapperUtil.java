package com.cydeo.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class MapperUtil {
    ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    // Some comments to explain what it does. CT prefers this alternative:
    public <T> T convert(Object objectToBeConverted, T convertedObject) {               //Here we return T Type 1st we get the
        return modelMapper.map(objectToBeConverted, (Type) convertedObject.getClass()); //class from this convertedObject
                                                                                        //then we get its Type-This (Type)
    }                                                                                   //is necessary since it's Generic
                                                                                        //If it wasn't like String then not necessary

//    public <T> T convert(Object objectToBeConverted, Class<T> convertedObject) { //UO:There's a Class class & this convertedObject
//        return modelMapper.map(objectToBeConverted, convertedObject);            //is a variable holding the Class object eg:TaskDTO.class
                                                                                   //& by writing Class<T> in front of it we are saying that
                                                                                   //his variable is a Class object representing type T & in
                                                                                   //reality it'll be whatever we pass for T
//    }



}
