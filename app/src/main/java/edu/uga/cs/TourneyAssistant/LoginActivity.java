package edu.uga.cs.TourneyAssistant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";
    private FirebaseAuth firebase;
    EditText mEmail;
    EditText mPassword;
    Button login;
    Button signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
            Log.d(TAG, currentUser.toString());
            startActivity(intent);
        }

        firebase = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        login.setOnClickListener(new LoginClickListener());
        //send the user to the sign up activity if they click here.
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start sign up activity screen
                Intent intent = new Intent(v.getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    public class LoginClickListener implements View.OnClickListener {
        @Override
        /**
         *  On click handler for button to sign in
         */
        public void onClick(View v) {
                String user = mEmail.getText().toString();
                String pass = mPassword.getText().toString();
                signIn(user, pass);
        }

        /**
         * Tries to authenticate user info with firebase
         *
         * @param email     user email
         * @param password  user password
         */
        private void signIn(String email, String password) {
            Log.d(TAG, "signIn:" + email);
            if (!validateForm()) {
                return;
            }

            // [START sign_in_with_email]
            // this is the firebase method for authenticating user with email and password
            firebase.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = firebase.getCurrentUser();
                                //make intent to move to the user view once they login successfully.
                                Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
                                Log.d(TAG, user.getDisplayName());
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            // [END sign_in_with_email]
        }

        /**
         * validates that email and or password are not empty
         * from the firebase resources
         * @return
         */
        private boolean validateForm() {
            boolean valid = true;

            String email = mEmail.getText().toString();
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Required.");
                valid = false;
            } else {
                mEmail.setError(null);
            }

            String password = mPassword.getText().toString();
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Required.");
                valid = false;
            } else {
                mPassword.setError(null);
            }

            return valid;
        }
    }
}
