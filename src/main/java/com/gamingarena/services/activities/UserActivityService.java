package com.gamingarena.services.activities;


import com.gamingarena.entities.Users;
import com.gamingarena.entities.activities.AdminActivities;
import com.gamingarena.entities.activities.UserActivities;
import com.gamingarena.payload.ActivityType;
import com.gamingarena.repositories.AdminActivityRepo;
import com.gamingarena.repositories.UserActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    @Autowired
    private UserActivityRepo userActivityRepo;


    public UserActivities joinedMatchActivity(ActivityType gameType, long matchId, ActivityType activityType, String note, Date activityTime, Users user, String activityDetails, int coins) {

        try {
            return userActivityRepo.save(UserActivities.builder()
                    .note(note)
                    .gameType(gameType)
                    .gameId(matchId)
                    .activityType(activityType)
                    .activityTime(activityTime)
                    .user(user)
                    .details(activityDetails)
                    .coins(coins)
                    .build());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UserActivities accountCreated(ActivityType activityType, String note, Date activityTime, Users user, String activityDetails, int coins) {


        try {
            return userActivityRepo.save(UserActivities.builder()
                    .note(note)
                    .activityType(activityType)
                    .activityTime(activityTime)
                    .user(user)
                    .details(activityDetails)
                    .coins(coins)
                    .build());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UserActivities accountUpdated(ActivityType activityType, String note, Date activityTime, Users user, String activityDetails, int coins) {


        try {
            return userActivityRepo.save(UserActivities.builder()
                    .note(note)
                    .activityType(activityType)
                    .activityTime(activityTime)
                    .user(user)
                    .details(activityDetails)
                    .coins(coins)
                    .build());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public UserActivities otherActivity(ActivityType activityType, String note, Date activityTime, Users user, String activityDetails, int coins) {

        try {
            return userActivityRepo.save(UserActivities.builder()
                    .note(note)
                    .activityType(activityType)
                    .activityTime(activityTime)
                    .user(user)
                    .details(activityDetails)
                    .coins(coins)
                    .build());

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

}
