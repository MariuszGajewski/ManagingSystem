package com.MG.ManagingSystem.models;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.util.Random;

public class Utils {
    public String passwordRandomizer(){
        Random random=new Random();
        StringBuilder randomPassword = new StringBuilder();

        while(randomPassword.length() < 10){
            char randomChar =(char)(random.nextInt(126 - 33 ) + 33);
            if(randomChar ==34) {
                continue;
            }
            randomPassword.append(randomChar);
        }
        if(!randomPassword.toString().matches("^(?=\\S*[a-z])(?=\\S*[A-Z])(?=\\S*[!@#$&*_?%^\"(){}\\[\\]<>|/\\-+=\\';:,\\.])(?=\\S*[0-9]).{10,}$")){
            passwordRandomizer();
        }
        System.out.println("haslo poprawne");
        return randomPassword.toString();
    }
    public String hashPassword(String password) {
        ShaPasswordEncoder encoder=new ShaPasswordEncoder();
        return encoder.encodePassword(password,"");
    }
}