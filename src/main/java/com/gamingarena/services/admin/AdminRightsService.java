package com.gamingarena.services.admin;

import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.entities.games.Bgmi;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.BgmiRepository;
import com.gamingarena.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminRightsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BgmiRepository bgmiRepository;

    @Autowired
    private ModelMapper modelMapper;

    //::::::::::::::: update user coins :::::::::::::::::::::: //

    public GlobalResponse manageUserCoins(
            long userId, int coins
    ){

        Optional<Users> user = userRepository.findById(userId);

        if(user.isPresent()){

            try {

                Users u = user.get();
                u.setCoins(coins);
                Users savedUser = userRepository.save(u);

                UserDto userDTO = modelMapper.map(savedUser, UserDto.class);

                System.out.println(userDTO);

                return GlobalResponse.builder()
                        .msg("user coins updated: "+ savedUser.getCoins())
                        .status(RespStatusEnum.SUCCESS)
                        .data(userDTO)
                        .build();


            }catch (Exception e){

                    e.printStackTrace();
                return GlobalResponse.builder()
                        .msg("Failed to update user coins, exception has occured")
                        .status(RespStatusEnum.ERROR)
                        .data(e.getMessage())
                        .build();
            }

        }else {
            return GlobalResponse.builder()
                    .msg("User not found")
                    .status(RespStatusEnum.USER_NOT_FOUND)
                    .build();
        }
    }


    //::::::::::::::::::::::: set user status ::::::::::::::::::::::::::::::::::;

    public GlobalResponse editUserStatus(
            long userId, int status
    ){
        Optional<Users> user = userRepository.findById(userId);

        if(user.isPresent()){

            try {

                Users u = user.get();
                u.setStatus(status);
                Users savedUser = userRepository.save(u);

                return GlobalResponse.builder()
                        .msg("user status updated: "+ savedUser.getStatus())
                        .status(RespStatusEnum.SUCCESS)
                        .data(savedUser.getStatus())
                        .build();


            }catch (Exception e){
                return GlobalResponse.builder()
                        .msg("Failed to update user coins, exception has occured")
                        .status(RespStatusEnum.ERROR)
                        .data(e.getMessage())
                        .build();
            }

        }else {
            return GlobalResponse.builder()
                    .msg("User not found")
                    .status(RespStatusEnum.USER_NOT_FOUND)
                    .build();
        }
    }

    //::::::::::::::::::::::: update match status :::::::::::::::::::::::: //

    public GlobalResponse editMatchStatus(
            long matchId, int status
    ){
        Optional<Bgmi> match = bgmiRepository.findById(matchId);

        if(match.isPresent()){

            try {

                Bgmi b = match.get();
                b.setStatus(status);
                Bgmi savedMatch = bgmiRepository.save(b);

                return GlobalResponse.builder()
                        .msg("Match status updated: "+ savedMatch.getStatus())
                        .status(RespStatusEnum.SUCCESS)
                        .data(savedMatch.getStatus())
                        .build();


            }catch (Exception e){
                return GlobalResponse.builder()
                        .msg("Failed to update user coins, exception has occured")
                        .status(RespStatusEnum.ERROR)
                        .data(e.getMessage())
                        .build();
            }

        }else {
            return GlobalResponse.builder()
                    .msg("User not found")
                    .status(RespStatusEnum.MATCH_NOT_FOUND)
                    .build();
        }
    }



}
