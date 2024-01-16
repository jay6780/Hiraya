package com.shs.app.Activity.Admin.quizManager.Reseach_project;

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
import com.shs.app.Activity.Admin.quizManager.generalChemistryQuiz.generalchemistry2_quiz;
import com.shs.app.Class.QuizClass.Question2;
import com.shs.app.R;

import java.util.List;

public class R_project_choice_update extends AppCompatActivity {

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
                        Intent intent = new Intent(getApplicationContext(), Research_project_quiz.class);
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
        setContentView(R.layout.activity_rproject_choice_update);
        changeStatusBarColor(getResources().getColor(R.color.maroon));
        // Get the home button drawable
        getSupportActionBar().setTitle("Update Multiple choice");
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

        String question = getIntent().getStringExtra("question");
        String questionId = getIntent().getStringExtra("questionId");
        String answer = getIntent().getStringExtra("answer");
        String option1 = getIntent().getStringExtra("option1");
        String option2 = getIntent().getStringExtra("option2");
        String option3 = getIntent().getStringExtra("option3");

        // Set the retrieved data to the respective EditText fields
        mQuestionTextEditText.setText(question);
        mOption1EditText.setText(option1);
        mOption2EditText.setText(option2);
        mOption3EditText.setText(option3);

        // Check the appropriate radio button based on the selected option
        int selectedOptionId = -1;
        String selectedOption = getIntent().getStringExtra("selectedOption");
        if (selectedOption != null) {
            if (selectedOption.equals(option1)) {
                selectedOptionId = R.id.rb_option1;
            } else if (selectedOption.equals(option2)) {
                selectedOptionId = R.id.rb_option2;
            } else if (selectedOption.equals(option3)) {
                selectedOptionId = R.id.rb_option3;
            }
        }

        if (selectedOptionId != -1) {
            mOptionsRadioGroup.check(selectedOptionId);
        }


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
                    DatabaseReference questionsRef = database.child("Research_project").child(questionId); // Use question ID as key

                    Question2 newQuestion = new Question2(question, option1, option2, option3, selectedOption, answer);
                    questionsRef.setValue(newQuestion);

                    // Clear entered data
                    mQuestionTextEditText.setText("");
                    mOption1EditText.setText("");
                    mOption2EditText.setText("");
                    mOption3EditText.setText("");
                    mOptionsRadioGroup.clearCheck();

                    // Show success message to user
                    Toast.makeText(getApplicationContext(), "Question saved successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate back to the quiz_content activity
                    Intent intent = new Intent(getApplicationContext(), Research_project_quiz.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish(); // Close the current activity to avoid going back to it on back press
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
        Intent intent = new Intent(getApplicationContext(), Research_project_quiz.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }
}