package edu.uga.cs.TourneyAssistant;

import android.os.Parcel;
import android.os.Parcelable;

public class Message implements Parcelable {
    private String text;
    private String userId;
    private String id;
    private String time;
    private String name;

    /**
     * default constructor
     */
    public Message() {

    }

    /**
     *
     * @param id
     * @param txt
     * @param time
     */
    public Message(String id, String userId, String name, String txt, String time){
        text = txt;
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Message(Parcel in) {
        id = in.readString();
        userId = in.readString();
        name = in.readString();
        time = in.readString();
        text = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(time);
        dest.writeString(text);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }
        public Message[] newArray(int size) {return new Message[size];}
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {return id;}
    public String getName() {return name;}
}
