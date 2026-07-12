package com.rose.user.mapper;

import org.mapstruct.Mapper;
import com.rose.user.dto.user.UserDto;
import com.rose.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
