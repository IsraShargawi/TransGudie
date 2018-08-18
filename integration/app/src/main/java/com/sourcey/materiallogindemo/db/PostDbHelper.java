package com.sourcey.materiallogindemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sourcey.materiallogindemo.Controller.Config;
import com.sourcey.materiallogindemo.Posts.NewsModel;
import com.sourcey.materiallogindemo.Posts.PostActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_IS_LIKED;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_POSTDATE;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_POSTDES;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_POSTIMG;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_POSTLIKES;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_POST_ID;

import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_USERNAME;
import static com.sourcey.materiallogindemo.db.Contract.Entry.POST_TABLE;



public class PostDbHelper extends SQLiteOpenHelper {
    Context context;
    public PostDbHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ POST_TABLE +"( " +
                Contract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                COL_POST_ID +" INTEGER  ," +
                COL_USERNAME + " TEXT , " +
                COL_POSTIMG + " TEXT  , " +
                COL_POSTDES + " TEXT , " +
                COL_POSTLIKES + " INTEGER , " +
                COL_IS_LIKED + " INTEGER ," +
                COL_POSTDATE + " TEXT " +
                ");";
        db.execSQL(createTable);
    }

    public boolean insertPost(int id , String postDesc , String username, String img, String date ,int likes,int isLiked) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_POST_ID , id);
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_POSTIMG, img);
        contentValues.put(COL_POSTDES, postDesc);
        contentValues.put(COL_POSTLIKES, likes);
        contentValues.put(COL_POSTDATE, date);
        contentValues.put(COL_IS_LIKED,isLiked);
        long check = db.insert(POST_TABLE, null, contentValues);
        if(check == -1){
            return false;
        }else
            return true;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        onCreate(db);
    }
    public void getPostsData() {
        delete();
        String url = Config.URL + "/displayPost.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                        boolean check =  insertPost(id,posttext,username,imgsrc,date,likenum,0);
                        /*if(check){
                            Toast.makeText(context, "yeeeeeeeeeeeeeeeeeeah", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context, "noooooooooooooo", Toast.LENGTH_SHORT).show();
                        }*/
                        NewsModel news = new NewsModel(id, posttext, date, likenum, imgsrc, username);
                        PostActivity.newsList.add(news);
                    }
                    PostActivity.mAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                Log.i("HiteshURLerror", "" + error);
            }
        });

        Volley.newRequestQueue(context).add(stringRequest);
    }

    public void delete() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + POST_TABLE);
        onCreate(db);
    }

}
