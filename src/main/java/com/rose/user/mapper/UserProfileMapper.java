package com.rose.user.mapper;

import com.rose.user.dto.user_profile.PublicUserProfileDto;
import com.rose.user.dto.user_profile.UpdateUserProfileDto;
import com.rose.user.dto.user_profile.UserProfileDto;
import com.rose.user.entity.UserProfile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(target = "username", source = "user.username")
    UserProfileDto toDto(UserProfile userProfile);

    @Mapping(target = "username", source = "user.username")
    PublicUserProfileDto toPublicDto(UserProfile userProfile);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            UpdateUserProfileDto dto,
            @MappingTarget UserProfile userProfile
    );
}
