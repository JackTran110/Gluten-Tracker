package com.example.cst8334_glutentracker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.ValidateMessageFragment;
import com.example.cst8334_glutentracker.database.FirebaseOnlineDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText loginName;
    private EditText password;
    private EditText rePassword;
    private EditText userName;
    private EditText email;

    private static String loginNameValue;
    private static String passwordValue;
    private static String rePasswordValue;
    private static String userNameValue;
    private static String emailValue;

    private ValidateMessageFragment errorMessageLoginName = new ValidateMessageFragment();
    private ValidateMessageFragment errorMessagePassword = new ValidateMessageFragment();
    private ValidateMessageFragment errorMessageRePassword = new ValidateMessageFragment();
    private ValidateMessageFragment errorMessageUserName = new ValidateMessageFragment();
    private ValidateMessageFragment errorMessageEmail = new ValidateMessageFragment();

    private static Map<String, Integer> frameMap = new HashMap<>();
    private static Map<String, ValidateMessageFragment> fragmentMap = new HashMap<>();

    private static final String ERROR_LOGIN_NAME_EMPTY
            = "Please enter login name!";
    private static final String ERROR_LOGIN_NAME_RANGE
            = "Your login name can only has from 1 to 20 characters!";
    private static final String ERROR_LOGIN_NAME_CONTAIN_INVALID_VALUES
            = "Your login name contains invalid character. Please try again!";
    private static final String ERROR_LOGIN_NAME_ALREADY_EXIST
            = " already exists. Please try again!";
    private static final String ERROR_PASSWORD_EMPTY
            = "Please enter password!";
    private static final String ERROR_INVALID_PASSWORD
            = "Password must contain at least 8 characters include upper case letter, lower case letter and number!";
    private static final String ERROR_PASSWORD_DOES_NOT_MATCH
            = "The passwords you entered didn't match! Please try again";
    private static final String ERROR_USER_NAME_EMPTY
            = "Please enter user name!";
    private static final String ERROR_USER_NAME_CONTAIN_INVALID_VALUES
            = "Your user name cannot contain characters: \\ / : ? * \" ' < > | { } ( ) [ ]";
    private static final String ERROR_EMAIL_EMPTY
            = "Please enter your email!";
    private static final String ERROR_INVALID_EMAIL
            = "Please enter a valid email address!";

    public static final String KEY_LOGIN_NAME = "Login name";
    public static final String KEY_PASSWORD = "Password";
    public static final String KEY_RE_PASSWORD = "Re-Password";
    public static final String KEY_USER_NAME = "User name";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_ERROR_MESSAGE = "Error message";

    private static final String PATTERN_LOGIN_NAME_RANGE_CHECK = ".{1,20}";
    private static final String PATTERN_LOGIN_NAME_INVALID = "[a-zA-Z0-9_\\-]*";
    private static final String PATTERN_PASSWORD_INVALID = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(.{8,}))";
    private static final String PATTERN_USER_NAME_INVALID = "([^\\\\/:?*\"'<>|{}()\\[\\]]*)";
    private static final String PATTERN_EMAIL_INVALID = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

    private static Map<String, List<String>> errorMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginName = findViewById(R.id.sign_up_login_name);
        password = findViewById(R.id.sign_up_password);
        rePassword = findViewById(R.id.sign_up_re_password);
        userName = findViewById(R.id.sign_up_user_name);
        email = findViewById(R.id.sign_up_email);
        Button submit = findViewById(R.id.submit_btn);
        Button cancel = findViewById(R.id.cancel_btn);
        errorMap = new HashMap<>();

        frameMap.put(KEY_LOGIN_NAME, R.id.login_name_error);
        frameMap.put(KEY_PASSWORD, R.id.password_error);
        frameMap.put(KEY_RE_PASSWORD, R.id.re_password_error);
        frameMap.put(KEY_USER_NAME, R.id.user_name_error);
        frameMap.put(KEY_EMAIL, R.id.email_error);

        fragmentMap.put(KEY_LOGIN_NAME, errorMessageLoginName);
        fragmentMap.put(KEY_PASSWORD, errorMessagePassword);
        fragmentMap.put(KEY_RE_PASSWORD, errorMessageRePassword);
        fragmentMap.put(KEY_USER_NAME, errorMessageUserName);
        fragmentMap.put(KEY_EMAIL, errorMessageEmail);

        submit.setOnClickListener((View v) ->{
            getInput();
            userValidator();
            if(Objects.requireNonNull(errorMap.get(KEY_LOGIN_NAME)).isEmpty()){
                FirebaseOnlineDatabase db = new FirebaseOnlineDatabase(RegisterActivity.this);
                db.execute(LoginActivity.CHECK_REGISTER, loginNameValue);
            }else updateSignUpForm();
        });

        cancel.setOnClickListener((View v) -> finish());
    }

    private void getInput(){
        loginNameValue = loginName.getText().toString();
        passwordValue = password.getText().toString();
        rePasswordValue = rePassword.getText().toString();
        userNameValue = userName.getText().toString();
        emailValue = email.getText().toString();
    }

    private void userValidator(){
        List<String> errors = new ArrayList<>();
        Pattern pattern;
        Matcher matcher;

        if(loginNameValue.isEmpty()) {
            errors.add(ERROR_LOGIN_NAME_EMPTY);
        }else {
            pattern = Pattern.compile(PATTERN_LOGIN_NAME_RANGE_CHECK);
            matcher = pattern.matcher(loginNameValue);
            if(!matcher.matches()) errors.add(ERROR_LOGIN_NAME_RANGE);
            pattern = Pattern.compile(PATTERN_LOGIN_NAME_INVALID);
            matcher = pattern.matcher(loginNameValue);
            if(!matcher.matches()) errors.add(ERROR_LOGIN_NAME_CONTAIN_INVALID_VALUES);
        }
        errorMap.put(KEY_LOGIN_NAME, errors);
        errors = new ArrayList<>();

        if(passwordValue.isEmpty()) {
            errors.add(ERROR_PASSWORD_EMPTY);
        }else {
            pattern = Pattern.compile(PATTERN_PASSWORD_INVALID);
            matcher = pattern.matcher(passwordValue);
            if(!matcher.matches()) errors.add(ERROR_INVALID_PASSWORD);
        }
        errorMap.put(KEY_PASSWORD, errors);
        errors = new ArrayList<>();

        if(rePasswordValue.isEmpty()) {
            errors.add(ERROR_PASSWORD_EMPTY);
        }else {
            if (!rePasswordValue.equals(passwordValue)) errors.add(ERROR_PASSWORD_DOES_NOT_MATCH);
        }
        errorMap.put(KEY_RE_PASSWORD, errors);
        errors = new ArrayList<>();

        if(userNameValue.isEmpty()){
            errors.add(ERROR_USER_NAME_EMPTY);
        }else {
            pattern = Pattern.compile(PATTERN_USER_NAME_INVALID);
            matcher = pattern.matcher(userNameValue);
            if(!matcher.matches()) errors.add(ERROR_USER_NAME_CONTAIN_INVALID_VALUES);
        }
        errorMap.put(KEY_USER_NAME, errors);
        errors = new ArrayList<>();

        if(emailValue.isEmpty()){
            errors.add(ERROR_EMAIL_EMPTY);
        }else {
            pattern = Pattern.compile(PATTERN_EMAIL_INVALID);
            matcher = pattern.matcher(emailValue);
            if(!matcher.matches()) errors.add(ERROR_INVALID_EMAIL);
        }
        errorMap.put(KEY_EMAIL, errors);
    }

    public void checkLoginAccount(boolean isLoginNameAlreadyExist){
        List<String> errors = new ArrayList<>();
        if(isLoginNameAlreadyExist){
            errors.add(loginNameValue + ERROR_LOGIN_NAME_ALREADY_EXIST);
            errorMap.put(KEY_LOGIN_NAME, errors);
        }
    }

    public void updateSignUpForm(){
        List<String> errors;
        StringBuilder updatedText;
        Bundle errorMessages;

        for(String key: errorMap.keySet()){

            errorMessages = new Bundle();
            errors = errorMap.get(key);
            updatedText = new StringBuilder();

            if(errors!=null && !errors.isEmpty()) {
                for (String error : errors) {
                    updatedText.append(error).append("\n");
                }
                fragmentMap.put(key, new ValidateMessageFragment());
                errorMessages.putString(KEY_ERROR_MESSAGE, updatedText.toString());
                fragmentMap.get(key).setArguments(errorMessages);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameMap.get(key), fragmentMap.get(key))
                        .commit();
            }else {
                getSupportFragmentManager().beginTransaction().remove(fragmentMap.get(key)).commit();
            }
        }
    }

    public String getLoginNameValue(){
        return loginNameValue;
    }

    public String getPasswordValue(){
        return passwordValue;
    }

    public String getUserNameValue(){
        return userNameValue;
    }

    public String getEmailValue(){
        return emailValue;
    }

    public Map<String, List<String>> getErrorMap(){
        List<String> emptyValues = new ArrayList<>();
        for(String key: errorMap.keySet()){
            if (errorMap.get(key).isEmpty()) emptyValues.add(key);
        }
        for (String key: emptyValues){
            errorMap.remove(key);
        }
        return errorMap;
    }
}