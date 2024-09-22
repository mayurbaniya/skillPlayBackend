package com.gamingarena.utils;

import com.gamingarena.entities.Users;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class HelperMethods {

    @Autowired
    private UserRepository userRepository;


    public RespStatusEnum validateAdmin(Users users){

        Optional<Users> byId = userRepository.findById(users.getId());

        if(!byId.isEmpty()){

            Users user = byId.get();

            if(user.getStatus() == 2){
                return RespStatusEnum.ADMIN;
            }
            else if(user.getStatus() == 1){
                return RespStatusEnum.USER;
            }
            else if(user.getStatus() == 0){
                return RespStatusEnum.DELETED;
            }
            else if(user.getStatus() == 99){
                return RespStatusEnum.BANNED;
            }
            else {
                return RespStatusEnum.UNKNOWN;
            }
        }else{
            return RespStatusEnum.USER_NOT_FOUND;
        }
    }

    public RespStatusEnum validateCreator(Users creator) {
        if (creator == null) {
            return RespStatusEnum.USER_NOT_FOUND;
        }

        switch (creator.getStatus()) {
            case 999:
                return RespStatusEnum.CREATOR;
            case 2:
                return RespStatusEnum.ADMIN;
            case 1:
                return RespStatusEnum.USER;
            case 0:
                return RespStatusEnum.DELETED;
            case 99:
                return RespStatusEnum.BANNED;
            default:
                return RespStatusEnum.UNKNOWN;
        }
    }


}
