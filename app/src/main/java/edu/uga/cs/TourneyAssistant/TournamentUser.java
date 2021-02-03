package edu.uga.cs.TourneyAssistant;

/**
 *
 */
public class TournamentUser {
    String id;
    String tournamentId;
    String userId;

    public TournamentUser(){}

    public TournamentUser(String id, String tournamentId, String userId) {
        this.id = id;
        this.tournamentId = tournamentId;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public String getUserId() {
        return userId;
    }
}
