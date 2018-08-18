package com.sourcey.materiallogindemo.Chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Controller.SessionManager;
import com.sourcey.materiallogindemo.db.ChatHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    List<Message> oldMessages;
    boolean onFirstCreate = true;
    Context context;
    ChatHelper mHelper;
    ListView mListView;
    View view ;
    ViewGroup viewGroup;
    ArrayAdapter<Message> mAdapter;
    SessionManager sessionManager ;
    String name = "",pass ="";

    public MessageAdapter(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
        HashMap<String,String> user = sessionManager.getUserDetail();
        this.name = user.get(SessionManager.NAME);
        this.pass = user.get(SessionManager.PASS);
        oldMessages = MainActivity.oldMessages;
        display(view,viewGroup);
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

   @Override
    public View getView(int i, View view, ViewGroup viewGroupLocal){
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);
        if (message.isBelongsToCurrentUser()) {
            view = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.messageBody.setText(message.getText());
                    } else {
            view = messageInflater.inflate(R.layout.their_message, null);
            holder.avatar = (View) view.findViewById(R.id.avatar);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.messageBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.name.setText(message.getData().getName());
            holder.messageBody.setText(message.getText());
            GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
            drawable.setColor(Color.parseColor(message.getData().getColor()));
        }
        return view;
    }


    public void display( View view, ViewGroup viewGroup){
        if(oldMessages.isEmpty()){
            Toast.makeText(context,"empty", Toast.LENGTH_LONG).show();
        }
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        for(int i = 0 ; i < oldMessages.size() ;i++){
            Message message = oldMessages.get(i);

            if (message.isBelongsToCurrentUser()) {
                view = messageInflater.inflate(R.layout.my_message, null);
                holder.messageBody = (TextView) view.findViewById(R.id.message_body);
                view.setTag(holder);
                holder.messageBody.setText(message.getText());
            } else {
                view = messageInflater.inflate(R.layout.their_message, null);
                holder.avatar = (View) view.findViewById(R.id.avatar);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.messageBody = (TextView) view.findViewById(R.id.message_body);
                view.setTag(holder);
                holder.name.setText(message.getData().getName());
                holder.messageBody.setText(message.getText());
                GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
                drawable.setColor(Color.parseColor("black"));
             }
        }
    }

}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView messageBody;
}

