package com.shs.app.Activity.Admin.quizManager.generalChemistryQuiz;

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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shs.app.Activity.Admin.quizManager.PE.p_eSubject;
import com.shs.app.Class.QuizClass.Question2;
import com.shs.app.R;

import java.util.List;

public class generalchemistry2_choiceadd extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; show dialog to confirm going back to menu
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to go back to the menu?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), generalchemistry2_quiz.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
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

    private EditText mTimeEditText; // Added EditText for time input
    private EditText mQuestionTextEditText;
    private EditText mOption1EditText;
    private EditText mOption2EditText;
    private EditText mOption3EditText;
    private RadioGroup mOptionsRadioGroup;
    private EditText mAnswerTextEditText;
    private Button mSaveQuestionButton;

    private List<Question2> mQuestionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generalchemistry2_choiceadd);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        // Get the home button drawable
        getSupportActionBar().setTitle("Multiple choice");
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
        mOption1EditText = findViewById(R.id.et_option1);
        mOption2EditText = findViewById(R.id.et_option2);
        mOption3EditText = findViewById(R.id.et_option3);
        mOptionsRadioGroup = findViewById(R.id.rg_options);
        mSaveQuestionButton = findViewById(R.id.btn_save_question);
        mSaveQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get entered data
                String question = mQuestionTextEditText.getText().toString();
                String option1 = mOption1EditText.getText().toString();
                String option2 = mOption2EditText.getText().toString();
                String option3 = mOption3EditText.getText().toString();

                // Find selected radio button
                int selectedOptionId = mOptionsRadioGroup.getCheckedRadioButtonId();

                if (TextUtils.isEmpty(question) || TextUtils.isEmpty(option1) || TextUtils.isEmpty(option2) || TextUtils.isEmpty(option3)
                        || selectedOptionId == -1) {
                    // Show error message to user
                    Toast.makeText(getApplicationContext(), "Please fill in all fields and select an answer option.", Toast.LENGTH_SHORT).show();
                } else {
                    String selectedOption = "";
                    String answer = "";
                    if (selectedOptionId == R.id.rb_option1) {
                        selectedOption = option1;
                        answer = option1;
                    } else if (selectedOptionId == R.id.rb_option2) {
                        selectedOption = option2;
                        answer = option2;
                    } else if (selectedOptionId == R.id.rb_option3) {
                        selectedOption = option3;
                        answer = option3;
                    }

                    // Store entered data in Firebase Realtime Database
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference questionsRef = database.child("generalchemistry2");
                    String questionId = questionsRef.push().getKey();
                    Question2 newQuestion = new Question2(question, option1, option2, option3, selectedOption, answer);
                    questionsRef.child(questionId).setValue(newQuestion);

                    // Clear entered data
                    mQuestionTextEditText.setText("");
                    mOption1EditText.setText("");
                    mOption2EditText.setText("");
                    mOption3EditText.setText("");
                    mOptionsRadioGroup.clearCheck();

                    // Show success message to user
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
        Intent intent = new Intent(getApplicationContext(), generalchemistry2_quiz.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        super.onBackPressed();
    }


}