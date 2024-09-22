package com.gamingarena.entities.games;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gamingarena.entities.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data

@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Bgmi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime startTime;
    private String type; // solo, duo, squad
    private String isPaid;
    private String map;
    private String version; //tpp or fpp
    private int status; // 0 finished,1 active, 2 ongoing, 99 cancelled
    private int matchCapacity;
    private int entryFee;
    private String matchLink;

    private String matchMode; // tdm, classic

    private int winningAmount;
    private int perKill;

    @ManyToMany
    @JoinTable(name = "bgmi_players", joinColumns = @JoinColumn(name = "bgmi_id"
    ), inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JsonManagedReference
    private List<Users> participants;



    @ManyToMany
    @JoinTable(name = "bgmi_winning_team", joinColumns = @JoinColumn(name = "bgmi_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> winningTeam;

    @ManyToMany
    @JoinTable(name = "bgmi_second_winning_team", joinColumns = @JoinColumn(name = "bgmi_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> secondWinningTeam;

    @ManyToMany
    @JoinTable(name = "bgmi_third_winning_team", joinColumns = @JoinColumn(name = "bgmi_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<Users> thirdWinningTeam;

}
