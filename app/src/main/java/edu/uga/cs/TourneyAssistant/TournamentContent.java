package edu.uga.cs.TourneyAssistant;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;


public class TournamentContent {
    private static final String TAG = "TournamentContent";

    /**
     * An array of sample (Tournament) items.
     */
    public static final List<TournamentItem> ITEMS = new ArrayList<TournamentItem>();

    /**
     * A map of sample (Tournament) items, by ID.
     */
    public static final Map<String, TournamentItem> ITEM_MAP = new HashMap<String, TournamentItem>();
    //TODO only show tournaments that hasnt passed
    static {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tournaments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ITEM_MAP.clear();
                ITEMS.clear();
                for (DataSnapshot tSnapshot: dataSnapshot.getChildren()) {
                    Tournament tournament = tSnapshot.getValue(Tournament.class);
                    TournamentItem item = createItem(tournament);
                    Log.d(TAG,tournament.name);
                    addItem(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    private static void addItem(TournamentItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static TournamentItem createItem(Tournament tournament) {
        return new TournamentItem(tournament.name, tournament.name, makeDetails(), tournament.msgId, tournament.gameTournamentId);
    }

    private static String makeDetails() {
        StringBuilder builder = new StringBuilder();
        builder.append("Loading tournament list..............");
        return builder.toString();
    }

    /**
     * A Tournament item representing a piece of content.
     */
    public static class TournamentItem {
        public final String id;
        public final String msgId;
        public final String gameTournamentId;
        public final String content;
        public final String details;

        public TournamentItem(String id, String content, String details, String msgId, String gameTournamentId) {
            this.msgId = msgId;
            this.gameTournamentId = gameTournamentId;
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
