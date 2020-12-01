package com.example.cst8334_glutentracker.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cst8334_glutentracker.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OnlineDatabase {
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private final static String ERROR_TAG = "OnlineDatabase";

    public OnlineDatabase(){
        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    public boolean createNewAccount(User user){
        try {
            databaseReference.child("users").child(Long.toString(user.getUserID())).setValue(user);
            return true;
        }catch (Exception e){
            Log.e(ERROR_TAG, "Unable to create new account!", e);
            return false;
        }
    }

//    public User findUserAccount(String loginName){
//        final User user;
//
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//
//        return user;
//    }
}
