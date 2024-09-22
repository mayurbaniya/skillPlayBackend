package com.gamingarena.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BgmiDto {

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
    private List<UserDto> participants;
    private UserDto winner;
    private UserDto second;
    private UserDto third;


}
