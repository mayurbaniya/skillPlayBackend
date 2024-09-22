package com.gamingarena.dto;

import com.gamingarena.payload.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivitiesDto {

    private int id;
    private Date activityTime;
    private ActivityType activityType;
    private ActivityType gameType;
    private long gameId;
    private String note;
    private int coins;
    private String details;
    private int userId; // Assuming you only need the userId for the user reference

}
