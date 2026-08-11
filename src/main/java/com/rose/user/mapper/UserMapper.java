package com.rose.user.mapper;

import org.mapstruct.Mapper;
import com.rose.user.dto.UserResponse;
import com.rose.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User user);
}
