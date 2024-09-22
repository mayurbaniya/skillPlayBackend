package com.gamingarena.controllers.users;

import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.payload.GlobalResponse;
import com.gamingarena.payload.RespStatusEnum;
import com.gamingarena.services.activities.UserActivityService;
import com.gamingarena.services.users.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserActivityService userActivityService;

    private Map<String, Users> usersMap = new HashMap<>();

    @GetMapping("/test")
    public String test(){
        return "Test Success..";
    }

    @PostMapping("/create")
    public ResponseEntity<GlobalResponse> checkEmailAndUsername(
            @RequestBody Users user
            ){
        user.setStatus(1);
        user.setCreated(new Date());
//        user.setCoins(0);
        usersMap.put(user.getEmail(), user);
        // validate user
        RespStatusEnum status = authService.validateUser(user);

        if(status.equals(RespStatusEnum.USERNAME_TAKEN)){
            return ResponseEntity.ok(
                   GlobalResponse.builder()
                           .msg("the username :"+ user.getUsername() + ", is already in use")
                           .status(status)
                           .build()
            );
        } else if (status.equals(RespStatusEnum.ALREADY_EXIST)) {
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("the email address : "+ user.getEmail() + ",  is already in use")
                            .status(status)
                            .build()
            );
        } else if (status.equals(RespStatusEnum.SUCCESS)) {

            RespStatusEnum response = authService.sendOTP(user.getEmail());

            if(response.equals(RespStatusEnum.SUCCESS)){
                return ResponseEntity.ok(
                        GlobalResponse.builder()
                                .msg("OTP has been sent to :"+ user.getEmail())
                                .status(status)
                                .data(user.getEmail())
                                .build()
                );
            }else {
                return ResponseEntity.ok(
                        GlobalResponse.builder()
                                .msg("Failed to sent OTP, something went wrong on server side")
                                .err("INTERNAL SERVER ERROR")
                                .status(status)
                                .build()
                );
            }
        }else {
            return ResponseEntity.internalServerError().build();
        }

    }

    @PostMapping("/otp")
    public ResponseEntity<GlobalResponse> verifyOTP(
            @RequestBody Map<String, String> map
    ){

        RespStatusEnum status = authService.verifyOTP(map.get("otp"), map.get("email"));

        if(status.equals(RespStatusEnum.SUCCESS)){


            UserDto user = authService.saveUser(usersMap.get(map.get("email")));

            if(user != null){

                usersMap.remove(user.getEmail());

                return ResponseEntity.ok(
                        GlobalResponse.builder()
                                .msg("User Created Successfully")
                                .data(user)
                                .status(status)
                                .build()
                );
            }else{
                return ResponseEntity.ok(
                        GlobalResponse.builder()
                                .msg("oops!! something went wrong.")
                                .status(RespStatusEnum.FAILED)
                                .build()
                );
            }
        }else if(status.equals(RespStatusEnum.INVALID_OTP)){
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Incorrect OTP, please try again")
                            .status(status)
                            .build()
            );

        }
        else {
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Something went wrong while verifying OTP")
                            .status(status)
                            .build()
            );
        }
    }


    @PostMapping("/login")
    public ResponseEntity<GlobalResponse> loginUser(
            @RequestBody Map<String, String> map
    ){
        System.err.println("email : " + map.get("email"));
        System.err.println("pass : " + map.get("password"));
        GlobalResponse user = authService.loginUser(map.get("email"), map.get("password"));

        return ResponseEntity.ok(user);

    }

    @GetMapping("/reset/{email}")
    public ResponseEntity<GlobalResponse> resetPassword(@PathVariable("email") String email){

        RespStatusEnum status = authService.sendResetPassEmail(email);

        if(status.equals(RespStatusEnum.SUCCESS)){

            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("OTP has been sent to "+ email + "please verify it to reset your password")
                            .status(status)
                            .build()
            );
        }
        else if(status.equals(RespStatusEnum.USER_NOT_FOUND)){
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Please check your email again, we don't have any Account linked with this email ID")
                            .status(status)
                            .build()
            );
        }
        else {
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Something Went Wrong, server side error. please try again later")
                            .status(status)
                            .build()
            );
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<GlobalResponse> verify(
            @RequestBody Map<String, String> map
    ){
        RespStatusEnum status = authService.verifyOTP(map.get("otp"), map.get("email"));

        if(status.equals(RespStatusEnum.SUCCESS)){
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("OTP verified, You can reset your password now")
                            .data(map)
                            .status(status)
                            .build()
            );
        } else if (status.equals(RespStatusEnum.INVALID_OTP)) {
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Incorrect OTP, please try again")
                            .status(status)
                            .build()
            );
        }else {
            return ResponseEntity.ok(
                    GlobalResponse.builder()
                            .msg("Something Went Wrong, while verifying OTP")
                            .status(status)
                            .build()
            );
        }
    }

    @PostMapping("/changepass")
    public ResponseEntity<GlobalResponse> changePassword(
            @RequestBody Map<String, String> map
    ){
        GlobalResponse resp = authService.changePassword(
                map.get("email"),
                map.get("otp"),
                map.get("password"));

        return ResponseEntity.ok(resp);
    }


}
