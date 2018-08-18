package com.sourcey.materiallogindemo.db;

import android.provider.BaseColumns;

public class Contract {
    public static final String DB_NAME = "com.sourcey.materiallogindemo.db";
    public static final int DB_VERSION = 1;

    public class Entry implements BaseColumns {
        public static final String PROFILE_TABLE = "profile";

        public static final String COL_NAME_TITLE = "username";
        public static final String COL_IMG_TITLE = "userimg";



        public static final String POST_TABLE = "post";

        public static final String COL_POST_ID = "postid";
        public static final String COL_USERNAME = "postername";
        public static final String COL_POSTIMG = "posterimg";
        public static final String COL_POSTDES = "description";
        public static final String COL_POSTLIKES = "likes";
        public static final String COL_POSTDATE = "date";
        public static final String COL_IS_LIKED = "currentlikes";

        public static final String CHATROOM_TABLE = "ChatRoom";

        public static final String COL_ID = "id";
        public static final String COL_ROOM = "roomname";
        public static final String COL_LAT1 = "lat1";
        public static final String COL_LAN1 = "lan1";
        public static final String COL_LAT2 = "lat2";
        public static final String COL_LAN2 = "lan2";

        public static final String CHAT_TABLE = "Chat";

        public static final String COL_CHAT_ID = "chatid";
        public static final String COL_SENDER_NAME = "username";
        public static final String COL_MESSAGE = "msgtext";
        public static final String COL_ROOM_NAME = "roomnum";
        public static final String COL_SENDER_ID = "userid";
        public static final String COL_SENDER_AVATAR = "avatar";


    }
}
