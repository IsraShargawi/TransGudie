package com.sourcey.materiallogindemo.Chat;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.Controller.Config;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.db.ChatHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatMainActivity extends AppCompatActivity {
    public static String ROOM_NAME = "";
    private List<CRNModel> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CRNAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        ChatHelper chathelper = new ChatHelper(this);
        chathelper.refeshing();
        mAdapter = new CRNAdapter(newsList, new CRNAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CRNModel item) {
                //Click on item will open NewsDetail screen
                ROOM_NAME = item.getTitle();
                startActivity(new Intent(ChatMainActivity.this,MainActivity.class));
            }
        });
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
        //get data for database ...using volley like display post
        String url = Config.URL + "/displayAreas.php";
        RequestQueue requestQueue = Volley.newRequestQueue(ChatMainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    String areaName = "";
                    int roomID = 0;
                    JSONObject jsonObject1 = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        roomID = jsonObject1.getInt("areaID");
                        areaName = jsonObject1.getString("areaname");

                        CRNModel news = new CRNModel(areaName);
                        newsList.add(news);
                    }
                    mAdapter.notifyDataSetChanged();
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putInt("areaID", roomID);
                    editor.putString("areaname", areaName);
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
        Volley.newRequestQueue(ChatMainActivity.this).add(stringRequest);
    }
}
