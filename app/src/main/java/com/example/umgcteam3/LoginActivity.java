/*
This class is an activity
 */
package com.example.umgcteam3;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button createAccountButton;
    TextView mCreateBtn,forgotTextLink;
    FirebaseAuth fAuth;
    public static String userID;
    private static Workout UpperBodyWorkout;
    private static Workout LowerBodyWorkout;
    private static Workout AbdominalWorkout;
    AlertDialog.Builder dialogBuilder;
    static BackgroundStatisticsWorker backgroundStatisticsWorker = new BackgroundStatisticsWorker();

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        createAccountButton = findViewById(R.id.createAccount);
        mCreateBtn = findViewById(R.id.forgotPassword);
        forgotTextLink = findViewById(R.id.forgotPassword);

        if(fAuth.getCurrentUser() != null){
            userID = fAuth.getCurrentUser().getUid();
            backgroundStatisticsWorker.doInBackground();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password Must be >= 6 Characters");
                    return;
                }
                // authenticate the user
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            userID = fAuth.getCurrentUser().getUid();
                            Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                            dialogBuilder.setMessage("Please input the correct password or select the forgotten password link to have it reset.")
                                    .setCancelable(false)
                                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toast.makeText(getApplicationContext(),"Try again",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            AlertDialog alert = dialogBuilder.create();
                            alert.setTitle("Incorrect Password");
                            alert.show();
                            System.out.println("Wrong password...");
                        }
                    }
                });
            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }

    public static Workout getUpperBodyWorkout() {
        return UpperBodyWorkout;
    }

    public static void setUpperBodyWorkout(Workout upperBodyWorkout) {
        UpperBodyWorkout = upperBodyWorkout;
    }

    public static Workout getLowerBodyWorkout() {
        return LowerBodyWorkout;
    }

    public static void setLowerBodyWorkout(Workout lowerBodyWorkout) {
        LowerBodyWorkout = lowerBodyWorkout;
    }

    public static Workout getAbdominalWorkout() {
        return AbdominalWorkout;
    }

    public static void setAbdominalWorkout(Workout abdominalWorkout) {
        AbdominalWorkout = abdominalWorkout;
    }
    public void registerForAccount(View view) {
        startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
    }



}