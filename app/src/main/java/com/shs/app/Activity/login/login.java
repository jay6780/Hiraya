package com.shs.app.Activity.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shs.app.Activity.Admin.Adminsettings.Admin;
import com.shs.app.Activity.Student.StudentSettings.Student;
import com.shs.app.Activity.Student.StudentSettings.signup;
import com.shs.app.R;

public class login extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private AppCompatButton loginButton;
    private TextView forgotPasswordButton;
    private  Button register;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        register = findViewById(R.id.register);
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.plogin_password);
        loginButton = findViewById(R.id.btn_login);
        forgotPasswordButton = findViewById(R.id.txtforgot);
        firebaseAuth = FirebaseAuth.getInstance();
        changeStatusBarColor(getResources().getColor(R.color.beige));
            mDatabase = FirebaseDatabase.getInstance().getReference();
            //  create.setOnClickListener(new View.OnClickListener() {
            //   @Override
            //  public void onClick(View v) {
            // showLoginPrompt();
            //  }
            //   });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });

            forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotYourPassword();
                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent register = new Intent(getApplicationContext(), signup.class);
                    startActivity(register);
                    overridePendingTransition(0,0);
                    finish();
                }
            });
        }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }



    private void forgotYourPassword() {
        LayoutInflater li = LayoutInflater.from(login.this);
        View promptsView = li.inflate(R.layout.passforgot, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(login.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userMail = (EditText) promptsView.findViewById(R.id.forgot_pass);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = userMail.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "Password reset email sent to " + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        userMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                final Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                String mailId = userMail.getText().toString().trim();
                if (mailId.isEmpty()) {
                    okButton.setEnabled(false);
                } else {
                    okButton.setEnabled(true);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            String userId = currentUser.getUid();
            DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Student").child(userId);
            DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(userId);

            // Create and show the progress dialog
            ProgressDialog progressDialog = ProgressDialog.show(login.this, "", "Loading...", true);

            ValueEventListener studentListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Dismiss the progress dialog
                    progressDialog.dismiss();

                    if (dataSnapshot.exists()) {
                        // User is a student, navigate to StudentPageActivity
                        startActivity(new Intent(login.this, Student.class));
                        finish();
                    } else {
                        // User is not a student, check if user is an admin
                        ValueEventListener adminListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // Dismiss the progress dialog
                                progressDialog.dismiss();

                                if (dataSnapshot.exists()) {
                                    // User is an admin, navigate to AdminPageActivity
                                    startActivity(new Intent(login.this, Admin.class));
                                    finish();
                                } else {
                                    // User is neither a student nor an admin
                                    Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error if necessary
                            }
                        };

                        adminRef.addListenerForSingleValueEvent(adminListener);
                    }
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if necessary
                }
            };

            studentRef.addListenerForSingleValueEvent(studentListener);
        }
    }


    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please enter your email");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Please enter your password");
            passwordEditText.requestFocus();
            return;
        }

        // Show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(login.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            // Hide progress dialog
            progressDialog.dismiss();

            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    if (user.isEmailVerified()) {
                        String userId = user.getUid();
                        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference().child("Student").child(userId);
                        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference().child("ADMIN").child(userId);

                        ValueEventListener studentListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // User is a student, navigate to StudentPageActivity
                                    startActivity(new Intent(login.this, Student.class));
                                    finish();
                                } else {
                                    // User is not a student, check if user is an admin
                                    ValueEventListener adminListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // User is an admin, navigate to AdminPageActivity
                                                startActivity(new Intent(login.this, Admin.class));
                                                finish();
                                            } else {
                                                // User is neither a student nor an admin
                                                Toast.makeText(login.this, "User not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Handle database error if necessary
                                        }
                                    };

                                    adminRef.addListenerForSingleValueEvent(adminListener);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle database error if necessary
                            }
                        };

                        studentRef.addListenerForSingleValueEvent(studentListener);
                    } else {
                        Toast.makeText(login.this, "Please verify your email before logging in "+email, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User is null, handle the case if needed
                }
            } else {
                Toast.makeText(login.this, "Login failed. Please check your credentials "+email, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}






