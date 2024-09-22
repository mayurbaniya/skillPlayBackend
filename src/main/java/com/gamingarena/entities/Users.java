package com.gamingarena.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gamingarena.entities.games.Bgmi;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int status; // 1 active, 0 deleted, 99 banned // 2 admin // 999 creator
    private String password;
    @Temporal(TemporalType.DATE)
    private Date created;

    @Temporal(TemporalType.DATE)
    private Date modified;


    private int coins;

    private int miniToken;


    private String bgmiUsername;
    @ManyToMany(mappedBy = "participants")
//    @JsonBackReference
    private List<Bgmi> bgmiData;


}
