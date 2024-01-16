package com.shs.app.Activity.Admin.quizManager.practicalResearch2;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shs.app.Activity.Admin.quizManager.PE.p_eSubject;
import com.shs.app.Class.QuizClass.Question10;
import com.shs.app.R;

public class pr2Fill extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; show dialog to confirm going back to the menu
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to go back to the menu?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), pr2Quiz.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private EditText mQuestionTextEditText;
    private EditText mAnswerTextEditText;
    private Button mSaveQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pr2_fill);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        // Get the home button drawable
        getSupportActionBar().setTitle("Fill and the blanks");
        Drawable homeButton = getResources().getDrawable(R.drawable.ic_home);
        homeButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(homeButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableString text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#7D0A0A")));

        mQuestionTextEditText = findViewById(R.id.et_question);
        mAnswerTextEditText = findViewById(R.id.et_correct_answer);
        mSaveQuestionButton = findViewById(R.id.btn_save_question);
        mSaveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get entered data
                String question = mQuestionTextEditText.getText().toString();
                String answer = mAnswerTextEditText.getText().toString();

                if (TextUtils.isEmpty(question) || TextUtils.isEmpty(answer)) {
                    // Show error message to the user
                    Toast.makeText(getApplicationContext(), "Please fill in the question and the correct answer.", Toast.LENGTH_SHORT).show();
                } else {
                    // Store entered data in Firebase Realtime Database
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference questionsRef = database.child("Practical_Research2");
                    String questionId = questionsRef.push().getKey();
                    Question10 newQuestion = new Question10(question, answer);
                    questionsRef.child(questionId).setValue(newQuestion);

                    // Clear entered data
                    mQuestionTextEditText.setText("");
                    mAnswerTextEditText.setText("");

                    // Show success message to the user
                    Toast.makeText(getApplicationContext(), "Question saved successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),pr2Quiz.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}