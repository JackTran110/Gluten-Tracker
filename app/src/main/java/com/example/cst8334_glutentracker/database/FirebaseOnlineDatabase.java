package com.example.cst8334_glutentracker.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.example.cst8334_glutentracker.activity.LoginActivity;
import com.example.cst8334_glutentracker.activity.MainMenuActivity;
import com.example.cst8334_glutentracker.activity.RegisterActivity;
import com.example.cst8334_glutentracker.entity.User;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import android.content.Intent;

public class FirebaseOnlineDatabase extends AsyncTask<String, String, List<User>> {

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private final static String TABLE_NAME = "users";
    private final static String USER_NAME = "userName";
    private final static String LOGIN_NAME = "loginName";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "email";
    private static String firstArgument;
    private static String secondArgument;
    private static String thirdArgument;
    @SuppressLint("StaticFieldLeak")
    private Activity fromActivity;

    public FirebaseOnlineDatabase(Activity fromActivity){
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.fromActivity = fromActivity;
    }

    @Override
    protected List<User> doInBackground(String... userInfo) {
        firstArgument = userInfo[0];
        secondArgument = userInfo[1];
        switch(firstArgument){
            case LoginActivity.LOGIN:{
                thirdArgument = userInfo[2];
                return get(LOGIN_NAME, secondArgument);
            }
            case LoginActivity.CHECK_REGISTER:{
                return get(LOGIN_NAME, secondArgument);
            }
            default: return null;
        }
    }

    @Override
    protected void onPostExecute(List<User> u) {
        switch (firstArgument){
            case LoginActivity.LOGIN:{
                if(u.size() == 1 && u.get(0).getPassword().equals(thirdArgument) && fromActivity != null){
                    fromActivity.startActivity(new Intent(fromActivity.getBaseContext(), MainMenuActivity.class));
                }
                break;
            }
            case LoginActivity.CHECK_REGISTER:{
                if(fromActivity != null) {
                    RegisterActivity activity = (RegisterActivity) fromActivity;
                    activity.checkLoginAccount(u.size()==1);
                    activity.updateSignUpForm();
                    if (activity.getErrorMap().isEmpty()) {
                        if(registerNewUser(User.getInstance()
                                .setLoginName(activity.getLoginNameValue())
                                .setPassword(activity.getPasswordValue())
                                .setUserName(activity.getUserNameValue())
                                .setEmail(activity.getEmailValue()))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("Sign up successfully!")
                                    .setMessage("You have successfully signed up your account. Account information:\n\n" +
                                            "Login name: " + activity.getLoginNameValue() +
                                            "\nUser name: " + activity.getUserNameValue() +
                                            "\nEmail: " + activity.getEmailValue())
                                    .setPositiveButton("OK", (DialogInterface dialog, int which) ->{
                                        Intent user = new Intent();
                                        user.putExtra(RegisterActivity.KEY_LOGIN_NAME, User.getInstance().getLoginName());
                                        user.putExtra(RegisterActivity.KEY_PASSWORD, User.getInstance().getPassword());
                                        user.putExtra(RegisterActivity.KEY_USER_NAME, User.getInstance().getUserName());
                                        user.putExtra(RegisterActivity.KEY_EMAIL, User.getInstance().getEmail());
                                        activity.setResult(LoginActivity.RESULT_CODE_REGISTER, user);
                                        activity.finish();
                                    });
                            builder.create().show();
                        }
                    }
                }
                break;
            }
        }
    }

    public boolean registerNewUser(User user){
        AtomicBoolean isSuccess = new AtomicBoolean(false);
        Map<String, String> userMap = new HashMap<>();
        userMap.put(USER_NAME, user.getUserName());
        userMap.put(LOGIN_NAME, user.getLoginName());
        userMap.put(PASSWORD, user.getPassword());
        userMap.put(EMAIL, user.getEmail());

        db.collection(TABLE_NAME).add(userMap)
                .addOnSuccessListener(documentReference -> isSuccess.set(true))
                .addOnFailureListener(e -> isSuccess.set(false))
                .addOnCompleteListener(task -> isSuccess.set(true));
        return true;
    }

    private List<User> get(String clause, String value){
        List<User> users = new ArrayList<>();
        try {
            Tasks.await(db.collection(TABLE_NAME).whereEqualTo(clause, value).get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful() && task.getResult() != null){

                            for(QueryDocumentSnapshot document: task.getResult()){
                                users.add(User.getInstance()
                                        .setUserName(document.getString(USER_NAME))
                                        .setLoginName(document.getString(LOGIN_NAME))
                                        .setEmail(document.getString(EMAIL))
                                        .setPassword(document.getString(PASSWORD)));
                            }
                        }
                    }));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }
}
