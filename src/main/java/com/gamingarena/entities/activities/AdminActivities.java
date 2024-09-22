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
public class AdminActivities {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date activityTime;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    private String note;

    private String details;

    @ManyToOne
    private Users affectedUser;

    @ManyToOne
    private Users adminUser;

}


