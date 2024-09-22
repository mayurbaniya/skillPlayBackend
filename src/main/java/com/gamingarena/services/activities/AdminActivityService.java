package com.gamingarena.services.activities;

import com.gamingarena.entities.Users;
import com.gamingarena.entities.activities.AdminActivities;
import com.gamingarena.payload.ActivityType;
import com.gamingarena.repositories.AdminActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
@RequiredArgsConstructor
public class AdminActivityService {



    @Autowired
    private AdminActivityRepo adminActivityRepo;



    public AdminActivities adminPerformedAnActivity(Users adminUser, ActivityType activityType, String note, Date activityTime, Users user, String activityDetails) {

        try {
            return adminActivityRepo.save(AdminActivities.builder()
                    .note(note)
                    .activityType(activityType)
                    .activityTime(activityTime)
                    .affectedUser(user)
                    .adminUser(adminUser)
                    .details(activityDetails)
                    .build());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


}
