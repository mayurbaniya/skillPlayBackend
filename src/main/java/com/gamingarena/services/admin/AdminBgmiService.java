package com.gamingarena.services.admin;

import com.gamingarena.entities.Users;
import com.gamingarena.entities.games.Bgmi;
import com.gamingarena.payload.ActivityType;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.BgmiRepository;
import com.gamingarena.repositories.UserRepository;
import com.gamingarena.services.activities.AdminActivityService;
import com.gamingarena.services.activities.UserActivityService;
import com.gamingarena.utils.HelperMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AdminBgmiService {

    @Autowired
    private BgmiRepository bgmiRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private AdminActivityService adminActivityService;

    @Autowired
    private HelperMethods helperMethods;

    public GlobalResponse createMatch(Bgmi matchData, long adminId){
        try {
            Bgmi savedMatch = bgmiRepository.save(matchData);
            
            Optional<Users> byId = userRepository.findById(adminId);
            Users adminUser = byId.get();

            RespStatusEnum adminStatus = helperMethods.validateAdmin(adminUser);

            if(adminStatus == RespStatusEnum.ADMIN) {

                if (savedMatch != null) {

                    adminActivityService.adminPerformedAnActivity(
                            adminUser,
                            ActivityType.CREATED_BGMIMATCH,
                            "ADMIN CREATED A BGMI MATCH",
                            new Date(),
                            null,
                            ""
                    );

                    return GlobalResponse.builder()
                            .msg("Match created Successfully")
                            .status(RespStatusEnum.SUCCESS)
                            .data(savedMatch)
                            .build();
                } else {
                    return GlobalResponse.builder()
                            .msg("Something Went Wrong")
                            .status(RespStatusEnum.FAILED)
                            .build();
                }
            }else{
                if(adminStatus == RespStatusEnum.USER){

                    userActivityService.otherActivity(
                            ActivityType.SUSPECIOUS,
                            "TRIED TO CREATE BGMI MATCH",
                            new Date(),
                            adminUser,
                            "USER "+ adminUser.getFirstName() + " TRIED TO CREATE BGMI MATCH, DO'NT HAVE ADMIN RIGHTS",
                            adminUser.getCoins()
                    );

                    return GlobalResponse.builder()
                            .msg("YOU ARE NOT AN ADMIN")
                            .status(RespStatusEnum.NOT_AN_ADMIN)
                            .build();
                } else if (adminStatus == RespStatusEnum.DELETED) {
                        adminActivityService.adminPerformedAnActivity(
                                adminUser,
                                ActivityType.SUSPECIOUS,
                                "DELETED USER TRIED TO CREATE BGMI MATCH",
                                new Date(),
                                null,
                                "USER "+ adminUser.getFirstName() + " TRIED TO CREATE BGMI MATCH,USER ACCOUNT IS DELETED ON "+ adminUser.getModified()
                        );
                    return GlobalResponse.builder()
                            .msg("ACCOUNT DELETED")
                            .status(RespStatusEnum.DELETED)
                            .build();
                } else if (adminStatus == RespStatusEnum.BANNED) {

                    adminActivityService.adminPerformedAnActivity(
                            adminUser,
                            ActivityType.SUSPECIOUS,
                            "BANNED USER TRIED TO CREATE BGMI MATCH",
                            new Date(),
                            adminUser,
                            "USER "+ adminUser.getFirstName() + " TRIED TO CREATE BGMI MATCH,USER IS BANNED ON "+ adminUser.getModified()

                    );

                    return GlobalResponse.builder()
                            .msg("BANNED")
                            .status(RespStatusEnum.BANNED)
                            .build();
                }else {
                    adminActivityService.adminPerformedAnActivity(
                            adminUser,
                            ActivityType.SUSPECIOUS,
                            "UNKNOWN USER STATUS : TRIED TO CREATE BGMI MATCH",
                            new Date(),
                            adminUser,
                            "USER "+ adminUser.getFirstName() + " TRIED TO CREATE BGMI MATCH,USER STATUS IS "+ adminUser.getStatus()+" LAST MODIFIED "+ adminUser.getModified()
                    );
                    return GlobalResponse.builder()
                            .msg("UNKNOWN OR EMPTY USER STATUS")
                            .status(RespStatusEnum.UNKNOWN)
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return GlobalResponse.builder()
                    .msg("Error Occurred")
                    .status(RespStatusEnum.ERROR)
                    .data(e.getMessage())
                    .build();
        }
    }
}
