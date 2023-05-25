package com.example.foodplanner.authenticationandaccount.presentation.signup.presenter;

import java.util.regex.Pattern;

public class SignUpPresenter implements SignUpPresenterInterface {



    @Override
    public boolean isValidEmail(String email) {
        String emailRegex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }

    @Override
    public boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_^&+=])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    @Override
    public boolean isValidUsername(String username) {
        return  username.length() > 8 ;
    }
}
