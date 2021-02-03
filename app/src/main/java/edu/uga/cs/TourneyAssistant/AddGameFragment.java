package edu.uga.cs.TourneyAssistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Android fragment that allows users to add games
 */
public class AddGameFragment extends DialogFragment {

    int day = 0;
    private EditText fieldNum, team1, team2, time;
    private RadioGroup dayRadio;
    private Button addGame;
    private final Calendar calender = Calendar.getInstance();


    public static AddGameFragment newInstance() {

        Bundle args = new Bundle();

        AddGameFragment fragment = new AddGameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_game, container);
    }

    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayRadio = getView().findViewById(R.id.radioDay);
        dayRadio.check(R.id.day1);
        dayRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.day1:
                        day = 1;
                        break;
                    case R.id.day2:
                        day = 2;
                        break;
                }
            }
        });

        team1 = getView().findViewById(R.id.team1);
        team2 = getView().findViewById(R.id.team2);
        fieldNum = getView().findViewById(R.id.fieldNum);

        time = getView().findViewById(R.id.time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        addGame = getView().findViewById(R.id.addGame);
        addGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isComplete()) {
                    addAGame();
                }
                //else do nothing and display must fill out somethig... check day and edittexts
            }
        });
    }

    private boolean isComplete() {
        boolean complete = true;
        if( TextUtils.isEmpty(team1.getText())){
            team1.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(team2.getText())){
            team2.setError( "This field can not be blank" );
            complete = false;
        }
        if( TextUtils.isEmpty(time.getText())){
            time.setError("This field can not be blank");
            complete = false;
        }
        if( TextUtils.isEmpty(fieldNum.getText())){
            fieldNum.setError("This field can not be blank");
            complete = false;
        }
        return complete;
    }
    public void showTimePicker() {
        int hour = calender.get(Calendar.HOUR_OF_DAY);
        int minute = calender.get(Calendar.MINUTE);


        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    calender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calender.set(Calendar.MINUTE, minute);
                    time.setText(view.getHour() + ":" + view.getMinute());
                }
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
        timePickerDialog.setTitle("Choose hour:");

        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        timePickerDialog.show();

    }

    private void addAGame() {
        Activity activity = getActivity();

        if(activity instanceof CreateTournamentActivity) {
            ((CreateTournamentActivity) activity).addGame(team1.getText().toString(), team2.getText().toString(),
                   day, time.getText().toString(), fieldNum.getText().toString());
        }
        this.dismiss();
     }
}





