package com.gamingarena.entities.activities;


import com.gamingarena.entities.Users;
import com.gamingarena.payload.ActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserActivities {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date activityTime;

    private ActivityType activityType;

    @Enumerated(EnumType.STRING)
    private ActivityType gameType;

    private long gameId;

    private String note;

    private int coins;

    private String details;

    @ManyToOne
    private Users user;

}


