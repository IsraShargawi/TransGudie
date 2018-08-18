package com.sourcey.materiallogindemo.Chat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.pushbots.push.Pushbots;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;
import com.sourcey.materiallogindemo.Controller.Config;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Controller.SessionManager;
import com.sourcey.materiallogindemo.db.ChatHelper;
import com.sourcey.materiallogindemo.db.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RoomListener {

    // replace this with a real channelID from Scaledrone dashboard
    private String channelID = "OjTiB2tdFOAL3nDY";
    private String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    SessionManager sessionManager ;
    MemberData data;
    static List<Message> oldMessages = new ArrayList<Message>();
    String name = "" , pass ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_messages);
        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetail();
        this.name = user.get(SessionManager.NAME);
        this.pass = user.get(SessionManager.PASS);
        editText = (EditText) findViewById(R.id.editText);
        getMessages();
        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        data = new MemberData(this.name, getRandomColor());
        scaledrone = new Scaledrone(channelID, data);

        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                scaledrone.subscribe(roomName, MainActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });
        //Pushbots.sharedInstance().registerForRemoteNotifications();
    }


    public void sendMessage(View view) {
        String message = editText.getText().toString();
        insertMessages(ChatMainActivity.ROOM_NAME,message,name,pass);

        if (message.length() > 0) {
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final MemberData data = mapper.treeToValue(member.getClientData(), MemberData.class);
            boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
            final Message message = new Message(json.asText(), data, belongsToCurrentUser);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //track this session
                    messageAdapter.add(message);
                    messagesView.setSelection(messagesView.getCount() - 1);
                }
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }



    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }
    public void insertMessages(final String roomName , final String msg , final String name , final String pass) {
        StringRequest request = new StringRequest(Request.Method.POST, Config.URL+"/insertMessages.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> prm = new HashMap<>();
                prm.put("roomname",roomName);
                prm.put("msg",msg);
                prm.put("name",name);
                prm.put("pass",pass);
                prm.put("avatar",data.getColor());
                return prm;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

       public void getMessages(){
           ChatHelper chathelper = new ChatHelper(MainActivity.this);
           SQLiteDatabase db = chathelper.getReadableDatabase();
           String cols [] =  new String[]{Contract.Entry._ID,Contract.Entry.COL_CHAT_ID,Contract.Entry.COL_SENDER_NAME,Contract.Entry.COL_MESSAGE,Contract.Entry.COL_ROOM_NAME,Contract.Entry.COL_SENDER_AVATAR};
           Cursor cursor = db.query(Contract.Entry.CHAT_TABLE,cols,
                   Contract.Entry.COL_ROOM_NAME +"=?", new String[] { ChatMainActivity.ROOM_NAME }, null, null, null);

           while (cursor.moveToNext()) {
               int senderIndex = cursor.getColumnIndex(Contract.Entry.COL_SENDER_NAME);
               int senderID = cursor.getInt(senderIndex);
               int senderNameIndex = cursor.getColumnIndex(Contract.Entry.COL_SENDER_NAME);
               String senderName = cursor.getString(senderNameIndex);
               int msgIndex = cursor.getColumnIndex(Contract.Entry.COL_MESSAGE);
               String msgText = cursor.getString(msgIndex);
               int avatarIndex = cursor.getColumnIndex(Contract.Entry.COL_SENDER_AVATAR);
               String avatarText = cursor.getString(avatarIndex);
               MemberData member = new MemberData(senderName,avatarText);
               Message message;
               if(senderName.equals(this.name)){
                   message = new Message(msgText,member,true);
               }else {
                   message = new Message(msgText, member, false);
               }
               oldMessages.add(message);
           }


       /*     StringRequest request = new StringRequest(Request.Method.POST, Config.URL+"/checkUser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    int currentSenderId = 0;
                    JSONObject jsonObject1 = null;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        currentSenderId = jsonObject1.getInt("userID");
                      }


                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
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
                return prm;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(request);*/
    }

}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
