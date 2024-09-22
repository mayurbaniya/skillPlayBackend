package com.gamingarena.dto;

import com.gamingarena.entities.Users;
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
public class AdminActivityDto {


    private long id;
    private Date activityTime;
    private ActivityType activityType;
    private String note;
    private String details;
    private long affectedUserId;
    private Users affectedUser;
    private Users adminUser;

}
