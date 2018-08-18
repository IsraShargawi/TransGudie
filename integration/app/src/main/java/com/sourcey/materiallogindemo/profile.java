package com.sourcey.materiallogindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {
    AppCompatButton btn ;
    SessionManager sessionManager;
    CircleImageView img,transImg;
    ImageButton imgBtn,imgBtn2;
    EditText ename ;
    TextView name;
    private Bitmap bitmap;
    private int REQUEST_CODE =  1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        final String username = user.get(SessionManager.NAME);
        final String password = user.get(SessionManager.PASS);


        btn = (AppCompatButton) findViewById(R.id.button);
        transImg = (CircleImageView) findViewById(R.id.trans_profile_img);
        img = (CircleImageView) findViewById(R.id.profile_image);
        // ename = (EditText) findViewById(R.id.Eusername);
        name = (TextView) findViewById(R.id.username);
        //imgBtn = (ImageButton)findViewById(R.id.edit);
        imgBtn2 = (ImageButton)findViewById(R.id.imageButton);

        name.setText(username);

       // ename.setVisibility(View.GONE);
        name.setVisibility(View.VISIBLE);

        imgBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadToServer(username,password);
            }
        });
        /*imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              ename.setVisibility(View.VISIBLE);
              name.setVisibility(View.GONE);
            }
        });*/
    }
    protected  void uploadImg(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent ,"Select Picture"),REQUEST_CODE);
    }
    protected void loadToServer(final String username , final String password){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.URL+"/upload.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(profile.this,"Done",Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(profile.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams=new HashMap<>();

                prams.put("image",imgTostring(bitmap));
                prams.put("username",username);
                prams.put("password",password);
                return prams;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private String imgTostring(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);

    }
    private void getSessionName(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, Config.URL+"/upload.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(profile.this,"Done",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(profile.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> prams=new HashMap<>();
                prams.put("name",name.getText().toString());
                prams.put("image",imgTostring(bitmap));
                Toast.makeText(profile.this,imgTostring(bitmap),Toast.LENGTH_LONG).show();

                return prams;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);

    }
    @Override
    protected void onActivityResult(int requestcode , int resultcode , Intent data){

        super.onActivityResult(requestcode,resultcode,data);
        if(requestcode == REQUEST_CODE && resultcode == RESULT_OK && data.getData() != null){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                img.setImageBitmap(bitmap);
                transImg.setImageBitmap(bitmap);
                transImg.setAlpha(0.5f);
                name.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
/**

 public class MainActivity extends ActionBarActivity {
 public final static String KEY_EXTRA_CONTACT_ID = "KEY_EXTRA_CONTACT_ID";

 private ListView listView;
 ExampleDBHelper dbHelper;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);

 Button button = (Button) findViewById(R.id.addNew);
 button.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View view) {
 Intent intent = new Intent(MainActivity.this, CreateOrEditActivity.class);
 intent.putExtra(KEY_EXTRA_CONTACT_ID, 0);
 startActivity(intent);
 }
 });

 dbHelper = new ExampleDBHelper(this);

 final Cursor cursor = dbHelper.getAllPersons();
 String [] columns = new String[] {
 ExampleDBHelper.PERSON_COLUMN_ID,
 ExampleDBHelper.PERSON_COLUMN_NAME
 };
 int [] widgets = new int[] {
 R.id.personID,
 R.id.personName
 };*/
/**
 Cursor rs = dbHelper.getPerson(personID);
 rs.moveToFirst();
 String personName = rs.getString(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_NAME));
 String personGender = rs.getString(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_GENDER));
 int personAge = rs.getInt(rs.getColumnIndex(ExampleDBHelper.PERSON_COLUMN_AGE));
 if (!rs.isClosed()) {
 rs.close();
 }
 */