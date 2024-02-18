package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthActivity extends AppCompatActivity {
    EditText username, profile_pic;
    Button toChat_button;
    public SharedPreferences prefs;
    public static AuthActivity authActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        username = findViewById(R.id.username);
        authActivity = this;
        toChat_button = findViewById(R.id.tochat_button);
        profile_pic = findViewById(R.id.profile_pic);
        prefs = getSharedPreferences("User", Context.MODE_PRIVATE);
       username.setText(prefs.getString("Username",""));
       profile_pic.setText(prefs.getString("Profile picture",""));


        toChat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChat();
            }
        });
    }
    @SuppressLint("CommitPrefEdits")
    private void toChat(){
        if(username.getText().toString().isEmpty()){
            Toast.makeText(this,"Введите имя пользователя",Toast.LENGTH_SHORT).show();
            return;
        }
        UserInfo.username = username.getText().toString();
        prefs.edit().putString("Username",UserInfo.username).apply();
        prefs.edit().putString("Profile pucture", profile_pic.getText().toString()).apply();
        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
        startActivity(intent);


    }
}