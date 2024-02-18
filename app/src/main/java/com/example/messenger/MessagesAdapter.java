package com.example.messenger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// класс адаптера для RecyclerView, который настраивает данные для отображения в списке сообщений.
public class MessagesAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    ArrayList<Message> messages ; //Это список сообщений, которые будут отображаться в RecyclerView.
    Context context; // Этот параметр предоставляет доступ к ресурсам и службам приложения
    LayoutInflater inflater; // Инфлятор, используемый для создания макета из XML-файла.
    int senderColor = Color.parseColor("#CCCCFF"); //  розовый цвет
    int otherColor = Color.parseColor("#F0C3EC"); // фиолетовый цвет




    //Конструктор MessagesAdapter инициализирует поля адаптера,
    // принимая в качестве аргументов контекст и список сообщений.
    public MessagesAdapter(Context context, ArrayList<Message> messages){
        this.messages = MainActivity.mainActivity.messages;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    //вызывается,когда RecyclerView создает новый объект ViewHolder в тот момент,
    // когда ему необходим новый для отображения элемент.
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Создает новый объект View из макета message_item.xml, который представляет отдельное сообщение в RecyclerView
        View view = inflater.inflate(R.layout.message_item, parent, false);
        //Возвращает новый экземпляр MessageViewHolder, который будет использоваться для хранения этого представления.
        return new MessageViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    //вызывается, когда новый объект ViewHolder должен быть привязан к RecyclerView.
    public void onBindViewHolder(@NonNull MessageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //Получает объект Message из списка сообщений по указанной позиции.
        String msgText = messages.get(position).text;
        //Устанавливает текст в TextView (message_text), отображая отправителя и текст сообщения.
        String msgSender = messages.get(position).sender;
        holder.message_text.setText(msgSender + ": " + msgText);


if (!messages.get(position).profile_pic_URL.equals("")){
    Picasso.with(context).load(messages.get(position).profile_pic_URL).into(holder.inChat_profile_pic);
} else {
    Picasso.with(context).load("https://pngimg.com/d/ghost_PNG81.png").into(holder.inChat_profile_pic);
}
        if (msgSender.equals(UserInfo.username)) {
            holder.message_text.setGravity(Gravity.RIGHT);
            holder.card.setCardBackgroundColor(senderColor);
        } else {
            holder.message_text.setGravity(Gravity.LEFT);
            holder.card.setCardBackgroundColor(otherColor);
        }

        holder.card.startAnimation(AnimationUtils.loadAnimation(context, R.anim.msg_anim_faded));
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(UserInfo.username.equals(msgSender)){

                    MainActivity.mainActivity.currentKey = MainActivity.mainActivity.keys.get(position);
                    MainActivity.mainActivity.interactionMenuVisible();
                }
                return true;

            }
        });
    }

    //возвращает общее количество элементов в списке сообщений.
    // Это нужно для того, чтобы RecyclerView знал, сколько элементов ему нужно отобразить.
    @Override
    public int getItemCount() {
        return messages.size();
    }
}


//Этот класс представляет ViewHolder для отдельного элемента сообщения в RecyclerView:
class MessageViewHolder extends RecyclerView.ViewHolder{

    TextView message_text; //Поле для отображения текста сообщения.

CardView card;
ImageView inChat_profile_pic;
    public MessageViewHolder(@NonNull View itemView) {

        super(itemView);
        message_text = itemView.findViewById(R.id.message_text);
        card = itemView.findViewById((R.id.msg_card));
        inChat_profile_pic = itemView.findViewById(R.id.inChat_profile_pic);

    }
}