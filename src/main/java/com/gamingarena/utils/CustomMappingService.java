package com.gamingarena.utils;

import com.gamingarena.dto.AdminActivityDto;
import com.gamingarena.dto.BgmiDto;
import com.gamingarena.dto.UserDto;

import com.gamingarena.entities.Users;
import com.gamingarena.entities.activities.AdminActivities;
import com.gamingarena.entities.games.Bgmi;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomMappingService {

    private final ModelMapper modelMapper;

    @Autowired
    public CustomMappingService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BgmiDto mapToBgmiDto(Bgmi match) {
        BgmiDto bgmiDto = modelMapper.map(match, BgmiDto.class);
        Set<UserDto> participantDtos = match.getParticipants().stream()
                .map(participant -> {
                    UserDto userDto = modelMapper.map(participant, UserDto.class);
                    userDto.setBgmiData(null); // Prevent nested bgmiData
                    return userDto;
                })
                .collect(Collectors.toSet());
        bgmiDto.setParticipants(participantDtos.stream().toList());
        return bgmiDto;
    }

    public UserDto mapToUserDto(Users user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        Set<BgmiDto> bgmiDtos = user.getBgmiData().stream()
                .map(bgmi -> {
                    BgmiDto bgmiDto = modelMapper.map(bgmi, BgmiDto.class);
                    bgmiDto.setParticipants(null); // Prevent nested participants
                    return bgmiDto;
                })
                .collect(Collectors.toSet());
        userDto.setBgmiData(bgmiDtos.stream().toList());
        return userDto;
    }

    public List<UserDto> mapToUserDtoList(List<Users> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    public AdminActivityDto mapToAdminActivityDto(AdminActivities adminActivity) {
        AdminActivityDto dto = modelMapper.map(adminActivity, AdminActivityDto.class);
        dto.setAdminUser(sanitizeUser(adminActivity.getAdminUser()));
        dto.setAffectedUser(adminActivity.getAffectedUser() != null ? sanitizeUser(adminActivity.getAffectedUser()) : null);
        return dto;
    }

    public List<AdminActivityDto> mapToAdminActivityDtoList(List<AdminActivities> adminActivities) {
        return adminActivities.stream()
                .map(this::mapToAdminActivityDto)
                .collect(Collectors.toList());
    }

    private Users sanitizeUser(Users user) {
        Users sanitizedUser = modelMapper.map(user, Users.class);
        sanitizedUser.setBgmiData(null); // Prevent nested bgmiData
        return sanitizedUser;
    }
}
