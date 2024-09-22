package com.gamingarena.services.admin;

import com.gamingarena.dto.UserActivitiesDto;
import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.entities.activities.UserActivities;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.UserActivityRepo;
import com.gamingarena.repositories.UserRepository;
import com.gamingarena.utils.CustomMappingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MonitoringServices {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserActivityRepo userActivityRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CustomMappingService customMappingService;

    public GlobalResponse getAllUserActivities() {
        List<UserActivities> allActivities = userActivityRepo.findAll();

        List<UserActivitiesDto> all = allActivities.stream()
                .map(a -> modelMapper.map(a, UserActivitiesDto.class)).toList();

        if (!all.isEmpty()) {
            return GlobalResponse.builder()
                    .msg("Fetched All Activities")
                    .status(RespStatusEnum.SUCCESS)
                    .data(all)
                    .build();
        } else {
            return GlobalResponse.builder()
                    .msg("Failed to Fetch Activities, something Went Wrong")
                    .status(RespStatusEnum.ERROR)
                    .data(null)
                    .build();
        }
    }

    public GlobalResponse getUserActivityById(int userId) {
        try {
            List<UserActivities> activitiesByUserId = userActivityRepo.findByUserId(userId);

            List<UserActivitiesDto> activityDTOs = activitiesByUserId.stream()
                    .map(a -> modelMapper.map(a, UserActivitiesDto.class))
                    .toList();

            if (!activityDTOs.isEmpty()) {
                return GlobalResponse.builder()
                        .msg("Fetched Activities for User ID: " + userId)
                        .status(RespStatusEnum.SUCCESS)
                        .data(activityDTOs)
                        .build();
            } else {
                return GlobalResponse.builder()
                        .msg("No Activities Found for User ID: " + userId)
                        .status(RespStatusEnum.ERROR)
                        .data(null)
                        .build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return GlobalResponse.builder()
                    .msg("Error Fetching Activities for User ID: " + userId)
                    .status(RespStatusEnum.ERROR)
                    .data(null)
                    .build();
        }
    }
    public GlobalResponse getAllUsers(){
        try {
            List<Users> allUsers = userRepo.findAll();

            List<UserDto> userDTOs = customMappingService.mapToUserDtoList(allUsers);


            if (!userDTOs.isEmpty()) {
                return GlobalResponse.builder()
                        .msg("FETCHED ALL USERS")
                        .status(RespStatusEnum.SUCCESS)
                        .data(userDTOs)
                        .build();
            } else {
                return GlobalResponse.builder()
                        .msg("OOPS, NO USERS FOUND")
                        .status(RespStatusEnum.EMPTY)
                        .data(null)
                        .build();
            }

        } catch (Exception ex) {
            return GlobalResponse.builder()
                    .msg("SERVER SIDE ERROR, SOMETHING WENT WRONG")
                    .status(RespStatusEnum.ERROR)
                    .data(null)
                    .err(ex.getMessage())
                    .build();
        }
    }

    public GlobalResponse getUserById(long id){

        try{

            Optional<Users> byId = userRepo.findById(id);
            Users user = byId.get();

            UserDto userDto = modelMapper.map(user, UserDto.class);

            if (!byId.isEmpty()) {
                return GlobalResponse.builder()
                        .msg("FOUND USER WITH ID"+ id)
                        .status(RespStatusEnum.SUCCESS)
                        .data(userDto)
                        .build();
            } else {
                return GlobalResponse.builder()

                        .msg("OOPS, NO USERS FOUND")
                        .status(RespStatusEnum.EMPTY)
                        .data(null)

                        .build();
            }

        }catch (Exception e){

            return GlobalResponse.builder()
                    .msg("SERVER SIDE ERROR, SOMETHING WENT WRONG")
                    .status(RespStatusEnum.ERROR)
                    .data(null)
                    .err(e.getMessage())
                    .build();
        }
    }

}
