package com.gamingarena.services.creator;

import com.gamingarena.dto.AdminActivityDto;
import com.gamingarena.entities.Users;
import com.gamingarena.entities.activities.AdminActivities;
import com.gamingarena.payload.ActivityType;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.AdminActivityRepo;
import com.gamingarena.repositories.UserRepository;;
import com.gamingarena.services.activities.UserActivityService;
import com.gamingarena.utils.CustomMappingService;
import com.gamingarena.utils.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CreatorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private HelperMethods helperMethods;

    @Autowired
    private AdminActivityRepo adminActivityRepo;

    @Autowired
    private CustomMappingService customMappingService;

    public GlobalResponse getAllAdminActivities(
            long creatorID,
            String creatorPass
    ){
        Users creator = userRepository.findByIdAndPassword(creatorID, creatorPass);
        RespStatusEnum result = helperMethods.validateCreator(creator);

        if(result == RespStatusEnum.CREATOR){
            List<AdminActivities> allAdminActivities  = adminActivityRepo.findAll();


            List<AdminActivityDto> adminActivityDtos = customMappingService.mapToAdminActivityDtoList(allAdminActivities);

            return GlobalResponse.builder()
                    .msg("FETCHED ALL ADMIN ACTIVITIES")
                    .status(RespStatusEnum.SUCCESS)
                    .data(allAdminActivities)
                    .build();
        }else {


            userActivityService.otherActivity(ActivityType.SUSPECIOUS,
                    "TRIED TO FETCH ADMIN ACTIVITIES",
                    new Date(),
                    creator,
                    "USER "+ creator.getUsername() + " TRIED TO FETCH ALL ADMIN ACTIVITIES WITHOUT HAVING CREATOR RIGHTS",
                    creator.getCoins()
                    );



            return  GlobalResponse.builder()
                    .msg("YOU DON'T HAVE RIGHTS")
                    .status(RespStatusEnum.FAILED)
                    .data(creator.getStatus())
                    .build();
        }

    }
}
