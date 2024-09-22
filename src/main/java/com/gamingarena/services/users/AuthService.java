package com.gamingarena.services.users;

import com.gamingarena.dto.BgmiDto;
import com.gamingarena.dto.UserDto;
import com.gamingarena.entities.Users;
import com.gamingarena.payload.*;
import com.gamingarena.repositories.UserRepository;
import com.gamingarena.services.activities.UserActivityService;
import com.gamingarena.services.mail.EmailService;
import com.gamingarena.utils.Commons;
import com.gamingarena.utils.CustomMappingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    // map to store otp
    private Map<String,String> otpMap = new HashMap<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomMappingService customMappingService;

//:::::::::::::::::::::::::::: generate otp :::::::::::::::::::::::::::::::::::: //

    private void generateOtp(int length, String receiverEmail){
        String numbers = "0123456789";
        Random random = new Random();
        StringBuilder otp = new StringBuilder();
        for(int i = 0; i< length; i++){
            int index = random.nextInt(numbers.length());
            otp.append(numbers.charAt(index));}
        System.out.println("Generated OTP: "+ otp.toString());

        otpMap.put(receiverEmail, otp.toString() );

        System.out.println("Map:: => " + otpMap);
    }

    // ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::://

    public RespStatusEnum validateUser(Users user){

        Optional<Users> byUsername = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));

        Optional<Users> byEmail = Optional.ofNullable(userRepository.findByEmail(user.getEmail()));

        if(byUsername.isPresent()){
            return RespStatusEnum.USERNAME_TAKEN;
        } else if (byEmail.isPresent()){
            return RespStatusEnum.ALREADY_EXIST;
        }else{
            return RespStatusEnum.SUCCESS;
        }

    }


    public RespStatusEnum sendOTP(
            String email
    ){
        generateOtp(4, email);
        return emailService.sendEmail(email, EmailTemplates.subject , EmailTemplates.setVerifyOtpMailBody(otpMap.get(email)));
    }



    public RespStatusEnum verifyOTP(String otp, String email){

        String storedOTP = otpMap.get(email);

       if(storedOTP == null){
           return RespStatusEnum.ERROR;
       } else if (storedOTP.equals(otp)) {
           return RespStatusEnum.SUCCESS;
       }else{
           return RespStatusEnum.INVALID_OTP;
       }

    }


    public UserDto saveUser(Users users) {
        // all users will start with 0 coins
        users.setCoins(0);
        try {
            // Save the user to the repository
            Users user = userRepository.save(users);

            userActivityService.accountCreated(
                    ActivityType.ACCOUNT_CREATED,
                    Commons.accountCreated,
                    new Date(),user,
                    "",
                    user.getCoins());

            UserDto userDto = modelMapper.map(user, UserDto.class);



            // Remove OTP and send email
            otpMap.remove(user.getEmail());
            emailService.sendEmail(user.getEmail(), EmailTemplates.greetingSubject, EmailTemplates.setGreetingMailBody(user.getFirstName()));

            // Return the UserDto
            return userDto;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public GlobalResponse loginUser(String email, String password) {
        Users user = userRepository.findByEmail(email);
        if (user != null) {
            if(user.getStatus() == 99){
                return GlobalResponse.builder()
                        .msg("You're banned, from this platform ")
                        .status(RespStatusEnum.BANNED)
                        .build();
            }
            if(user.getStatus() == 0){
                return GlobalResponse.builder()
                        .msg("Account Deleted")
                        .status(RespStatusEnum.DELETED)
                        .build();
            }
            if (user.getPassword().equals(password) && user.getStatus() == 2){

                List<BgmiDto> bgmiDTOs = user.getBgmiData().stream()
                        .map(bgmi -> modelMapper.map(bgmi , BgmiDto.class))
                        .collect(Collectors.toList());

                UserDto userDto = modelMapper.map(user, UserDto.class);
                userDto.setBgmiData(bgmiDTOs);


                return GlobalResponse.builder()
                        .msg("ADMIN LOGGED IN " + user.getFirstName())
                        .data(userDto)
                        .status(RespStatusEnum.SUCCESS)
                        .build();
            }

            if (user.getPassword().equals(password) && user.getStatus() == 1) {


                UserDto userDTO = customMappingService.mapToUserDto(user);

                return GlobalResponse.builder()
                        .msg("Welcome back " + user.getFirstName())
                        .data(userDTO)
                        .status(RespStatusEnum.SUCCESS)
                        .build();
            } else {
                return GlobalResponse.builder()
                        .msg("Wrong Password! please try again or click on 'Forgot Password' ")
                        .status(RespStatusEnum.INVALID_PASSWORD)
                        .build();
            }
        } else {
            return GlobalResponse.builder()
                    .msg("User not found, please create a new Account")
                    .status(RespStatusEnum.USER_NOT_FOUND)
                    .build();
        }
    }



    public RespStatusEnum sendResetPassEmail(String email){
        Users user = userRepository.findByEmail(email);
        if(user != null){
            generateOtp(4, email);
           return emailService.sendEmail(email, EmailTemplates.resetPassSubjecct , EmailTemplates.setResetPassMailBody(user.getFirstName(), otpMap.get(user.getEmail())));
        }else {
            return RespStatusEnum.USER_NOT_FOUND;
        }
    }


    public GlobalResponse changePassword(
            String email,
            String otp,
            String password
    ){

        String storedOTP =( otpMap.get(email) != null ? otpMap.get(email) : "");
        Users user = userRepository.findByEmail(email);

       if(storedOTP.equals(otp)){

           try{


               user.setPassword(password);
               user.setModified(new Date());
               userRepository.save(user);

               UserDto userdto = customMappingService.mapToUserDto(user);

               userActivityService.
                       accountUpdated(
                               ActivityType.UPDATE_PROFILE,
                               "USER CHANGED PASSWORD",
                                new Date(),
                                user,
                               user.getEmail()+" changed password",
                                user.getCoins() );

               otpMap.remove(user.getEmail());

               return GlobalResponse.builder()
                       .msg("Password Updated")
                       .data(userdto)
                       .status(RespStatusEnum.SUCCESS)
                       .build();

           }catch (Exception e){

               return  GlobalResponse.builder()
                       .msg("Something Wen't wrong, server side exception occured ")
                       .status(RespStatusEnum.ERROR)
                       .build();

           }

       }else {


           userActivityService.otherActivity(
                   ActivityType.SUSPECIOUS,
                   "USER TRIED TO CHANGE PASSWORD IN A SUSPECIOUS WAY ",
                   new Date(),
                   user,
                   "",
                   user.getCoins()
           );

           return GlobalResponse.builder()
                   .msg("Oops! you cannot change your password like this..")
                   .status(RespStatusEnum.FAILED)
                   .build();
       }
    }






}
