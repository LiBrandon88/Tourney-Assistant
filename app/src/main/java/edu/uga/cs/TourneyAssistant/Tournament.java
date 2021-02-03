package edu.uga.cs.TourneyAssistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tournament entity class
 */
public class Tournament {
    String id;
    String userId;
    String name;
    String address;
    String startDate;
    String endDate;
    String msgId;
    String gameTournamentId;

    public Tournament() {

    }

    /**
     * Constructer
     * @param id
     * @param userId
     * @param name
     * @param address
     * @param startDate
     * @param endDate
     */
    public Tournament(String id, String userId, String name, String address, String startDate, String endDate, String msgIdk, String gameTournamentId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.msgId = msgIdk;
        this.gameTournamentId = gameTournamentId;
    }

    public String getId() {
        return id;
    }

    public  String getUserId() {return userId;}

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getMsgId() {return msgId;}

    public String getGameTournamentId() {return gameTournamentId;}

    public void setName(String name) {
        this.name = name;
    }

}
