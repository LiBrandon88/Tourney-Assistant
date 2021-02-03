package edu.uga.cs.TourneyAssistant;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a user entity
 */
public class Users implements Parcelable {
    private String userId;
    private String name;
    private String email;
    private boolean isTd;

    public Users() {

    }

    /**
     * Constructer
     * @param userId
     * @param name
     * @param email
     */
    public Users(String userId, String name, String email, boolean td) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        isTd = td;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Boolean isTd() {
        return isTd;
    }
    public void setTd(boolean td) {
        isTd = td;
    }

    protected Users(Parcel in) {
        this.userId = in.readString();
        this.name = in.readString();
        this.email =  in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        public Users[] newArray(int size) {
            return new Users[size];
        }
    };
}
