package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEvent;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView messages_recycler;
    public ArrayList<Message> messages = new ArrayList<>();
    public  ArrayList<String> keys = new ArrayList<>();
    public String currentKey = "";
    EditText editText;
    MessagesAdapter adapter;

    CardView delete_menu;
    Button delete, no;

    Button send_button;
    DatabaseReference ref;
    Button edit_btn, want_to_delete, get_back_btn;
    CardView interaction_menu;
    Animation alpha_in, alpha_out;


public static MainActivity mainActivity;


    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ref = DataBase.database.getReference("Chat/Messages");

        mainActivity = this;
        messages.clear();

        // Инициализация RecyclerView из макета activity_main.xml
        messages_recycler = findViewById(R.id.messages_recycler);
        // Инициализация адаптера и связывание его с RecyclerView
        adapter = new MessagesAdapter(this, messages);
        messages_recycler.setAdapter(adapter);

        alpha_in = AnimationUtils.loadAnimation(this,R.anim.msg_anim_faded);

        alpha_out = AnimationUtils.loadAnimation(this,R.anim.anim_out);


        // Установка менеджера компоновки для RecyclerView (линейный вертикальный)
        messages_recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        delete_menu = findViewById(R.id.remove_menu);
        delete = findViewById(R.id.delete_button);
        interaction_menu = findViewById(R.id.interaction_menu);
        no = findViewById(R.id.no_button);
        get_back_btn = findViewById(R.id.get_back_btn);
        want_to_delete = findViewById(R.id.want_to_delete);
        edit_btn = findViewById(R.id.edit_btn);



        no.setOnClickListener(new View.OnClickListener() { // отменяем удаление
            @Override
            public void onClick(View v) {
                currentKey = "";
                removeMenuGone();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMessage();
                currentKey = "";
                removeMenuGone();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messages.get(keys.indexOf(currentKey)).text;
                editText.setText(text);

            }
        });
        want_to_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionMenuNOTVisible();
                removeMenuVisible();


            }
        });
        get_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactionMenuNOTVisible();
            }
        });


        // Инициализация кнопки send_button из макета activity_main.xml
        send_button = findViewById(R.id.button_to_send);
        // Установка слушателя для кнопки send_button
        send_button.setOnClickListener(new View.OnClickListener() {

            @Override
            // Вызов метода send() при нажатии на кнопку
            public void onClick(View view) {
                if(currentKey.equals("")){
                    send();
                }else{
                    editMessage();
                }
            }
        });
        ChildEventListener listener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("text").exists()){
                    Message message = snapshot.getValue(Message.class);
                    messages.add(message);

                    String key = snapshot.getKey();
                    keys.add(key);

                    adapter.notifyItemInserted(messages.size()-1);
                    messages_recycler.smoothScrollToPosition(messages.size()-1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.child("text").exists()){

                    String key = snapshot.getKey();
                    String text = snapshot.child("text").getValue(String.class);
                    messages.get(keys.indexOf(key)).text = text ;

                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { // удаление сообщения с экрана
                String key = snapshot.getKey();
                int position = keys.indexOf(key);
                messages.remove(position);
                keys.remove(position);
                adapter.notifyItemRemoved(position);


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        ref.addChildEventListener(listener);


        editText = findViewById(R.id.editText);

    }

    private void editMessage() {
        DatabaseReference msgRef = ref.child(currentKey);
        msgRef.child("text").setValue(editText.getText().toString());
        currentKey = "";
        editText.setText("");
        interactionMenuNOTVisible();

    }

    @SuppressLint("NotifyDataSetChanged")
    // Метод для отправки сообщения
    private void send() {
        // Получение текста из EditText
        String messageText = editText.getText().toString();
        if (!messageText.isEmpty()) {
            // Проверяем, что сообщение не пустое
            String username = UserInfo.username;
            String text  = editText.getText().toString();


            Message message = new Message(username,text,AuthActivity.authActivity.prefs.getString("Profile picture", ""));

            DatabaseReference msgRef = ref.push();// путь к сообщению
            msgRef.setValue(message);




            // Очистка текста в EditText
            editText.setText("");

        }else{
            Toast.makeText(this,"Введите текст...", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteMessage(){
        DatabaseReference msgRef = ref.child(currentKey);
        msgRef.removeValue();

    }

    public  void removeMenuVisible(){
        delete_menu.setVisibility(View.VISIBLE);
        delete_menu.startAnimation(alpha_in);
    }
    public  void removeMenuGone(){
        delete_menu.setVisibility(View.GONE);
        delete_menu.startAnimation(alpha_out);
    }

    public  void interactionMenuVisible(){
        interaction_menu.setVisibility(View.VISIBLE);
        interaction_menu.startAnimation(alpha_in);
    }
    public void interactionMenuNOTVisible(){
        interaction_menu.startAnimation(alpha_out);
        interaction_menu.setVisibility(View.GONE);

    }


}