package com.sourcey.materiallogindemo.Posts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sourcey.materiallogindemo.Chat.ChatMainActivity;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Controller.SessionManager;
import com.sourcey.materiallogindemo.db.PostDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    SessionManager sessionManager ;
    static public List<NewsModel> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    public AppCompatButton add,chat ;
    private TextView usergreet ;
    static public NewsAdapter mAdapter;
    PostDbHelper posthelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        final String name = user.get(SessionManager.NAME);
        final String pass = user.get(SessionManager.PASS);
        posthelper = new PostDbHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        add = (AppCompatButton) findViewById(R.id.addPost);
        chat = (AppCompatButton) findViewById(R.id.chatroom);
        add.setText("Create Post");
        chat.setText("Chat Rooms");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headingToCreate();
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatMainActivity.class);
                startActivity(intent);
            }
        });

        mAdapter = new NewsAdapter(newsList,this);
        //Set Layout to recyclerview
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        //Set divider
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(mLayoutManager);
        //Give animation
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        posthelper.getPostsData();
    }

    public void headingToCreate(){
        Intent intent = new Intent(this,CreatePost.class);
        startActivity(intent);
    }
}
