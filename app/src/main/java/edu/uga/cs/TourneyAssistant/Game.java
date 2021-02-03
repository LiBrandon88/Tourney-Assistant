package edu.uga.cs.TourneyAssistant;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Game entity class
 */
public class Game implements Parcelable {
    String id;
    String date;
    String time;
    int field;
    String team1;
    String team2;

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public Game() {

    }

    public Game(String id, String date, String time, int field, String team1, String team2) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.field = field;
        this.team1 = team1;
        this.team2 = team2;
    }

    public Game(String date, String time, int field, String team1, String team2) {
        this.id = null;
        this.date = date;
        this.time = time;
        this.field = field;
        this.team1 = team1;
        this.team2 = team2;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getField() {
        return field;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    // Parcelling part
    public Game(Parcel in){
        this.team1 = in.readString();
        this.team2 = in.readString();
        this.date =  in.readString();
        this.time =  in.readString();
        this.field = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.team1);
        dest.writeString(this.team2);
        dest.writeString(this.date);
        dest.writeString(this.time);
        dest.writeInt(this.field);
    }

    @Override
    public String toString() {
        String game = team1 + '\t' + '\t' + team2 + '\t'+ '\t' + date + '\t'+ '\t' + time + '\t'+ '\t' + field + '\t'+ '\t';
        return  game;
    }
}
