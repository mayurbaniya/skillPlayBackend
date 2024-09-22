package com.gamingarena.payload;

import com.gamingarena.entities.games.Bgmi;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserData {

    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int status;
    private Date created;
    private Date modified;

    private int coins;
    private String bgmiUsername;
    private List<Bgmi> bgmiData;

}
