package edu.uga.cs.TourneyAssistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 *  Activity used to create a tournament
 */
public class CreateTournamentActivity extends AppCompatActivity {

    private static final String TAG = "CreateTournament";

    private final Calendar calendar = Calendar.getInstance();
    private Button create, addGames, cancel, addUser;
    private TableLayout table, usersTable;
    private int numberOfTeams = 2;
    private EditText tName,startDate, endDate, address, userEmail;
    private Spinner numTeams;
    private FirebaseAuth firebase;
    private ArrayList<Game> games;
    private ArrayList<Users> users;

    //@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_create_tournament);
        games = new ArrayList<>();
        table = findViewById(R.id.gamesTable);

        users = new ArrayList<>();
        usersTable = findViewById(R.id.usersTable);

        tName = findViewById(R.id.tName);
        address = findViewById(R.id.address);

        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        setUpDates();

        numTeams = findViewById(R.id.spinner);
        setUpSpinner();

        userEmail = findViewById(R.id.insertEmail);
        addUser = findViewById(R.id.addUser);
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( TextUtils.isEmpty(userEmail.getText())){
                    userEmail.setError("Enter a user email");
                }
                else if(users.size() == 0) {
                    addUserByEmail();
                }
                else if(!(isDuplicate(userEmail.getText().toString()))) {
                    userEmail.setError("Already Entered");
                }
                else {
                    addUserByEmail();
                }
            }
        });

        addGames = findViewById(R.id.addGames);
        addGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isComplete()) {
                    if(numberOfTeams == games.size()) {
                        Toast.makeText(getApplicationContext(),"MAX NUMBER OF TEAMS",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        setGames();
                    }
                }
            }
        });

        create = findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isComplete()){
                    if(numberOfTeams < games.size()) {
                        Toast.makeText(getApplicationContext(),"Please add more games!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        createTournament();
                    }
                }
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(savedInstanceState != null) {
            tName.setText(savedInstanceState.getString("T_NAME"));
            address.setText(savedInstanceState.getString("ADDY"));
            startDate.setText(savedInstanceState.getString("S_DATE"));
            endDate.setText(savedInstanceState.getString("E_DATE"));
            numberOfTeams = savedInstanceState.getInt("NUM", 0);
            games = savedInstanceState.getParcelableArrayList("GAMES");
            users = savedInstanceState.getParcelableArrayList("USERS");

            printGames();
            printUsers();
            int index = 0;
            if (numberOfTeams == 2) {
                index = 0;
            }
            else if(numberOfTeams == 4) {
                index = 1;
            }
            else {
                index = 2;
            }
            numTeams.setSelection(index);
        }
        else {
        }

    }

    private boolean isDuplicate(String email) {
        boolean d = false;
        for(int i = 0; i < users.size(); i++) {
            if(users.get(i).getEmail().equals(email)) {
                d = true;
            }
        }
            return d;
    }
    
    /**
     * Creates and adds tournament to database
     */
    private void createTournament(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("tournaments");
        DatabaseReference mesRef = database.getReference("messages");
        String msgId = mesRef.push().getKey();

        String name = tName.getText().toString();
        String addy = address.getText().toString();
        //storing dates as a string, already in date format dd-mm-yyyy
        String sDate = startDate.getText().toString();
        String eDate = endDate.getText().toString();
        String id = ref.push().getKey();

        DatabaseReference gameRef = database.getReference("games");
        String gameTournamentId = gameRef.push().getKey();
        for(int i = 0; i < games.size(); i++) {
            String gameId = gameRef.child(gameTournamentId).push().getKey();
            games.get(i).setId(gameId);
            gameRef.child(gameTournamentId).child(gameId).setValue(games.get(i));
        }

        //Storing tournament into database
        Tournament tournament = new Tournament(id, firebase.getCurrentUser().getUid(), name, addy, sDate, eDate, msgId, gameTournamentId);
        ref.child(id).setValue(tournament);

        DatabaseReference newRef = database.getReference("tournamentGroup");
        for(int i = 0; i < users.size();i++) {
            String newId = newRef.push().getKey();
            TournamentUser tu = new TournamentUser(newId, users.get(i).getUserId(), id);
            newRef.child(newId).setValue(tu);
        }

        Intent intent = new Intent(CreateTournamentActivity.this, ItemListActivity.class);
        startActivity(intent);
    }
    /**
     * Checks that all fields are filled
     * @return
     */
    private boolean isComplete() {
        boolean complete = true;
        if( TextUtils.isEmpty(tName.getText())){
            tName.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(address.getText())){
            address.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(startDate.getText())){
            startDate.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(endDate.getText())){
            endDate.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(tName.getText())){
            tName.setError("This field can not be blank");
            complete = false;
        }
        return complete;
    }

    /**
     * Print all games in the list to the table layout
     */
    protected void printGames(){
        for(int i = 0; i < games.size(); i++) {
            addTableRow(games.get(i));
        }
    }

    /**
     * Print all users in the list to the table layout
     */
    protected void printUsers(){
        for(int i = 0; i < users.size(); i++) {
            addTableRow(users.get(i));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("T_NAME", tName.getText().toString());
        outState.putString("ADDY", address.getText().toString());
        outState.putString("S_DATE", startDate.getText().toString());
        outState.putString("E_DATE", endDate.getText().toString());
        outState.putInt("NUM", numberOfTeams);
        outState.putParcelableArrayList("GAMES", games );
        outState.putParcelableArrayList("USERS", users );

        super.onSaveInstanceState(outState);
    }

    /**
     * Used by fragment to add game
     * @param t1
     * @param t2
     * @param day
     * @param time
     * @param field
     */
    public void addGame(String t1, String t2, int day, String time, String field) {
        String date;
        if(day == 1) {
            date = startDate.getText().toString();
        }
        else {
            date = endDate.getText().toString();
        }

        Game game = new Game(date, time, Integer.parseInt(field), t1, t2);
        games.add(game);

        //display game in table
        addTableRow(game);
    }

    /**
     * Set up games
     */
    private void setGames() {
        AddGameFragment dialogFragment = new AddGameFragment();

        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);

        dialogFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        dialogFragment.show(ft, "dialog");
    }

    /**
     * adds a user to the user table
     * @param user
     */
    private void addTableRow(Users user) {
        //add game to table
        //param for textview
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        //Converting dp
        int dpValue = 5; // margin in dps
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int)(dpValue * d);
        params.setMargins(margin, margin, margin, margin);

        TextView name = new TextView(this);
        name.setText(user.getName());
        name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        name.setLayoutParams(params);

        TextView email = new TextView(this);
        email.setText(user.getEmail());
        email.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        email.setLayoutParams(params);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        row.addView(name);
        row.addView(email);

        usersTable.addView(row);
    }

    /**
     * Add game to table
     * @param game
     */
    private void addTableRow(Game game) {
        //add game to table
        //param for textview
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        //Converting dp
        int dpValue = 5; // margin in dps
        float d = this.getResources().getDisplayMetrics().density;
        int margin = (int)(dpValue * d);
        params.setMargins(margin, margin, margin, margin);

        TextView team1 = new TextView(this);
        team1.setText(game.getTeam1());
        team1.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        team1.setLayoutParams(params);

        TextView team2 = new TextView(this);
        team2.setText(game.getTeam2());
        team2.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        team2.setLayoutParams(params);

        TextView date = new TextView(this);
        date.setText(game.getDate());
        date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        date.setLayoutParams(params);

        TextView time = new TextView(this);
        time.setText(game.getTime());
        time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        time.setLayoutParams(params);

        TextView field = new TextView(this);
        field.setText("Field " +game.getField());
        field.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        field.setLayoutParams(params);

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        row.addView(team1);
        row.addView(team2);
        row.addView(date);
        row.addView(time);
        row.addView(field);
        table.addView(row);
    }

    /**
     * Sets up the date values
     */
    private void setUpDates() {
        //Date picker for the start date and puts end date the day after
        final DatePickerDialog.OnDateSetListener sDate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDate.setText(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                endDate.setText(sdf.format(calendar.getTime()));
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateTournamentActivity.this, sDate, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private void addUserByEmail() {
        String email = userEmail.getText().toString();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
        Query query = ref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // There may be multiple users with the email address, so we need to loop over the matches
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    Log.d(TAG,userSnapshot.getKey());
                    Users user = userSnapshot.getValue(Users.class);
                    if(user.getEmail().equals(userEmail.getText().toString())) {
                        users.add(user);
                        addTableRow(user);
                    }
                }
                userEmail.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "find email address:onCancelled", databaseError.toException());
                //TODO ask if td wants to send an invitation to the email
                userEmail.setError("Email not found!");
                // ...
            }
        });
    }

    /**
     * Sets up spinner that determines num of teams allowed
     */
    private void setUpSpinner() {
        ArrayList<Integer> arrayList = new ArrayList<>();
        arrayList.add(2);
        arrayList.add(4);
        arrayList.add(8);

        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numTeams.setAdapter(arrayAdapter);
        numTeams.setSelection(0);
        numTeams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String numString = parent.getItemAtPosition(position).toString();
                numberOfTeams = Integer.parseInt(numString);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                numberOfTeams = 0;
            }
        });
    }
}