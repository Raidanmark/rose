package com.rose.user.mapper;

import org.mapstruct.Mapper;
import com.rose.user.dto.UserDto;
import com.rose.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
