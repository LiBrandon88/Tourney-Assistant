package edu.uga.cs.TourneyAssistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.uga.cs.TourneyAssistant.Models.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUp";
    private FirebaseAuth firebase;
    private DatabaseReference mDatabase;
    Button bSignUp;
    EditText mEmail;
    EditText mPassword;
    EditText mName;
    CheckBox cTd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //TODO add validation function because all fields are required to submit.
        bSignUp = findViewById(R.id.button2);
        mEmail = findViewById(R.id.newEmail);
        mPassword = findViewById(R.id.newPassword);
        mName = findViewById(R.id.editText3);
        cTd = findViewById(R.id.td);
        bSignUp.setOnClickListener(new View.OnClickListener() {
            /**
             * button handler
             * @param v
             */
            @Override
            public void onClick(View v) {
                firebase = FirebaseAuth.getInstance();
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                final boolean td = cTd.isChecked();
                //create the user in firebase
                firebase.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            /**
                             * Firebase call
                             * @param task
                             */
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = firebase.getCurrentUser();
                                    //below, setting the display name to name + | + td (boolean value). so we can easily see if they're a TD.
                                    //might even add the team name here, but we will split the display name by "|" and only take the name when we
                                    //dislay to the user later.
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name + "," + td)
                                            .build();

                                    //add the display name
                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User profile updated.");
                                                    }
                                                }
                                            });

                                    writeNewUser(user.getUid(), name, email, td);
                                    firebase.signOut();
                                    //now to to the login activity so you can login
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    navigateUpTo(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                // ...
                            }
                        });
            }
        });
    }

    private void writeNewUser(String id, String name, String email, boolean td){
        User user = new User(name, email, td);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("users").child(id).setValue(user);
    }
}
