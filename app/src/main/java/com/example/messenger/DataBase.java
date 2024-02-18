package com.example.messenger;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseKt;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DataBase {
    public  static FirebaseDatabase database = FirebaseDatabase.getInstance("https://messenger-a34d8-default-rtdb.firebaseio.com/");

}
