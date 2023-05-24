package com.example.bookshare_project.models.mappers;

import com.example.bookshare_project.models.dto.UserDto;
import com.example.bookshare_project.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "lastname", source = "user.lastname")
    UserDto userToUserDto(User user);
}
