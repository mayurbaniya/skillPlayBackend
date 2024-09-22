package com.gamingarena.utils;

public class Commons {

     String setResetPassBody(String user, String otp){
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
                "[Your Company Name] Team";
    }

     static final String subject = "Your One-Time Password (OTP) for Secure Access";
     static final String resetPassSubject = "Action Required: Reset Your Password with One-Time Passcode (OTP)";

     static final String body = "\n" +
            "Dear User,\n\n" +
            "Your one-time password (OTP) is provided above. Please use this code to complete your authentication. " +
            "If you did not request this, please ignore this email.\n\n" +
            "Thank you.";

     public static final String userJoined = "USER JOINED A MATCH";
    public static final String accountCreated = "ACCOUNT CREATED";
}
