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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.sourcey.materiallogindemo.db.Contract.Entry.CHAT_TABLE;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_CHAT_ID;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_MESSAGE;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_ROOM_NAME;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_SENDER_AVATAR;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_SENDER_ID;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_SENDER_NAME;


public class ChatHelper extends SQLiteOpenHelper {
    Context context;

    public ChatHelper(final Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
         this.context = context;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ CHAT_TABLE +"( " +
                Contract.Entry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT ," +
                COL_CHAT_ID +" INTEGER  ," +
                COL_ROOM_NAME +" TEXT ," +
                COL_MESSAGE +" TEXT ,\n" +
                COL_SENDER_ID +" INTEGER ,  " +
                COL_SENDER_AVATAR +" TEXT ," +
                COL_SENDER_NAME +" TEXT " +
                ");";
        db.execSQL(createTable);
    }
    public boolean insertChatData(int chatId ,int userId, String msg , String roomname ,String username, String avatar) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CHAT_ID , chatId);
        contentValues.put(COL_SENDER_ID , userId);
        contentValues.put(COL_MESSAGE, msg);
        contentValues.put(COL_ROOM_NAME, roomname);
        contentValues.put(COL_SENDER_NAME, username);
        contentValues.put(COL_SENDER_AVATAR, avatar);
        long check = db.insert(CHAT_TABLE, null, contentValues);
        if(check == -1){
            return false;
        }else
        return true;

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CHAT_TABLE);
        onCreate(db);
    }
    public void refeshing(){
        //for deleting all last data
        delete();
        String url = Config.URL + "/selectMessages.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    String roomname = "" ,msg = "",username= "", avatar="";
                    int chatId = 0,userId = 0 ;
                    JSONObject jsonObject1 = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        chatId = jsonObject1.getInt("chatID");
                        roomname = jsonObject1.getString("roomname");
                        userId = jsonObject1.getInt("userID");
                        msg = jsonObject1.getString("msgtxt");
                        avatar = jsonObject1.getString("avatar");
                        username = jsonObject1.getString("username");
                        boolean isInserted = insertChatData(chatId,userId,msg,roomname,username,avatar);
                       /*if(isInserted){
                            Toast.makeText(context, "yeah it works but why  "+msg, Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(context, "sorry try agiain", Toast.LENGTH_LONG).show();
                        }*/
                    }
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
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_TABLE);
        onCreate(db);
    }

}
