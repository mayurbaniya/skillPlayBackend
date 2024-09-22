package com.gamingarena.payload;

import org.springframework.stereotype.Component;

@Component
public class EmailTemplates {

    public final static String subject = "Your One-Time Password (OTP) for Secure Access";
    public final static String greetingSubject = "Welcome to GamingArena!";

    public final static String resetPassSubjecct = "Action Required: Reset Your Password with One-Time Passcode (OTP)";


    public static String setGreetingMailBody(String user){

        return "Dear "+user+",\n\n" +
                "Welcome to GamingArena! We are thrilled to have you on board.\n\n" +
                "Thank you for creating an account with us. We are committed to providing you with the best service possible. Here are a few things you can do to get started:\n\n" +
                "1. Explore our features and services.\n" +
                "2. Customize your profile and preferences.\n" +
                "3. Reach out to our support team if you have any questions or need assistance.\n\n" +
                "We hope you enjoy using our platform. If you have any feedback or suggestions, we would love to hear from you.\n\n" +
                "Thank you for choosing [Your Company/Service Name].\n\n" +
                "Best regards,\n" +
                "The [Your Company/Service Name] Team";
    }




    public static String setResetPassMailBody(String user, String otp){
        return "Hello "+ user +",\n" +
                "\n" +
                "To initiate the password reset process for your account, please find your one-time passcode (OTP) below:\n" +
                "\n" +
                " "+otp+" \n" +
                "\n" +
                "Please use this OTP to proceed with resetting your password. \n" +
                "\n" +
                "If you haven't requested this password reset, kindly ignore this message. Rest assured, your account remains secure.\n" +
                "\n" +
                "Best regards,\n" +
                "~GamingArena Team";
    }

    public static String setVerifyOtpMailBody(String otp){

        return "Dear Valued User,\n\n" +
                "We have generated a One-Time Password (OTP) for you: " + otp + ". Please use this code to complete your authentication process.\n\n" +
                "If you did not request an OTP, please disregard this email. For security reasons, do not share your OTP with anyone.\n\n" +
                "Thank you for choosing our service.\n\n" +
                "Best regards,\n" +
                "The GamingArena Team";
    }

}
