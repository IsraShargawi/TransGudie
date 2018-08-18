package com.sourcey.materiallogindemo.Chat;

public class Message {
    private String text;
    private MemberData data;
    private boolean belongsToCurrentUser;

    public Message(String text, MemberData data, boolean belongsToCurrentUser) {
        this.text = text;
        this.data = data;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public MemberData getData() {
        return data;
    }
    public void setData(MemberData data) {
        this.data = data ;
    }


    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }
    public void setCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser ;
    }

}
