package com.rose.user.profile.mapper;

import com.rose.user.profile.dto.ProfileResponse;
import com.rose.user.profile.dto.PublicProfileResponse;
import com.rose.user.profile.dto.UpdateProfileRequest;
import com.rose.user.profile.entity.Profile;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "username", source = "user.username")
    ProfileResponse toDto(Profile userProfile);

    @Mapping(target = "username", source = "user.username")
    PublicProfileResponse toPublicDto(Profile userProfile);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            UpdateProfileRequest dto,
            @MappingTarget Profile userProfile
    );
}
