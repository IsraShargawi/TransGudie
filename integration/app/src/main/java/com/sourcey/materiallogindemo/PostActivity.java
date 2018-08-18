package com.sourcey.materiallogindemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.Chat.ChatMainActivity;
import com.sourcey.materiallogindemo.NewsAdapter;
import com.sourcey.materiallogindemo.NewsDetailActivity;
import com.sourcey.materiallogindemo.NewsModel;
import com.sourcey.materiallogindemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    SessionManager sessionManager ;
    private List<NewsModel> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    public AppCompatButton add,chat ;
    private TextView usergreet ;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        final String name = user.get(SessionManager.NAME);
        final String pass = user.get(SessionManager.PASS);

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
        setNewsData();

    }
    private void setNewsData() {
        String url = Config.URL + "/displayPost.php";
        RequestQueue requestQueue = Volley.newRequestQueue(PostActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    String date = "", posttext = "", username = "", imgsrc = "";
                    int id = 0, likenum = 0;
                    JSONObject jsonObject1 = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        id = jsonObject1.getInt("postID");
                        posttext = jsonObject1.getString("posttext");
                        username = jsonObject1.getString("username");
                        likenum = jsonObject1.getInt("likenum");
                        date = jsonObject1.getString("pDate");
                        imgsrc = jsonObject1.getString("imgsrc");
                        NewsModel news = new NewsModel(id, posttext, date, likenum, imgsrc, username);
                        newsList.add(news);
                    }
                    mAdapter.notifyDataSetChanged();
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt("id", id);
                    editor.putString("posttext", posttext);
                    editor.putString("username", username);
                    editor.putInt("likenum", likenum);
                    editor.putString("pDate", date);
                    editor.putString("imgsrc", imgsrc);
                    editor.commit();


                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_LONG).show();
                Log.i("HiteshURLerror", "" + error);
            }
        });

        Volley.newRequestQueue(PostActivity.this).add(stringRequest);
    }
    public void headingToCreate(){
        Intent intent = new Intent(this,CreatePost.class);
        startActivity(intent);
    }
}
