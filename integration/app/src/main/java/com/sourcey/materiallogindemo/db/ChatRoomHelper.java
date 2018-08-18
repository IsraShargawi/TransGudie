package com.sourcey.materiallogindemo.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.sourcey.materiallogindemo.db.Contract.Entry.CHATROOM_TABLE;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_ID;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_LAN2;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_LAT1;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_LAT2;
import static com.sourcey.materiallogindemo.db.Contract.Entry.COL_ROOM;


public class ChatRoomHelper extends SQLiteOpenHelper {

    public ChatRoomHelper(Context context) {
        super(context, Contract.DB_NAME, null, Contract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ CHATROOM_TABLE +"(" +
                 Contract.Entry.COL_ID +"integer PRIMARY KEY AUTOINCREMENT," +
                  COL_ROOM +" text," +
                  COL_LAT1 +" float,\n" +
                  COL_LAT1 +" float," +
                  COL_LAT2 +"float," +
                  COL_LAN2 +" float" +
                ");";



        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CHATROOM_TABLE);
        onCreate(db);
    }


    public Cursor getAllPersons() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + CHATROOM_TABLE, null );
        return res;
    }
}

