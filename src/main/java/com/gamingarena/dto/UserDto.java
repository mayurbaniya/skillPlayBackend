package com.gamingarena.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {
    private  long id;
    private  String firstName;
    private  String lastName;
    private  String username;
    private  String email;
    private   int status; // 1 active, 0 deleted, 99 banned

    //         String password,

    private   Date created;
    private   Date modifie;
    private    int coins;
    private  String bgmiUsername;

    private     List<BgmiDto> bgmiData;
}
