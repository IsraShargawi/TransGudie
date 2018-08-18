package com.sourcey.materiallogindemo.Posts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.Controller.Config;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Controller.SessionManager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreatePost extends AppCompatActivity{
    SessionManager sessionManager;
    AppCompatButton btn ;
    EditText txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        final String name = user.get(SessionManager.NAME);
        final String pass = user.get(SessionManager.PASS);

        btn = (AppCompatButton) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt = (EditText) findViewById(R.id.editText);
                if(txt.getText().toString().equals("")){
                    Toast.makeText(getBaseContext(), "u have to write something ,idiot", Toast.LENGTH_LONG).show();
                }else{
                    StringRequest request = new StringRequest(Request.Method.POST, Config.URL+"/CreatePost.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), PostActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> prm = new HashMap<>();
                            prm.put("username",name);
                            prm.put("password",pass);
                            prm.put("posttext",txt.getText().toString());
                            long messageTime = new Date().getTime();
                            String time = (String) DateFormat.format("dd-MM-yyyy (HH:mm:ss)",messageTime);
                            prm.put("time",time);
                            return prm;
                        }
                    };
                    Volley.newRequestQueue(CreatePost.this).add(request);
                }

            }
        });
    }
}
