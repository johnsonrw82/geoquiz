package com.jrw82.android.geoquiz;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GeoQuizActivity extends Activity {
    private static final String TAG = "GeoQuizActivity";
    private static final String KEY_INDEX = "questionIndex";
    private static final String CHEATER_INDEX = "isCheaterIndex";
    private static final String CHEATER_BANK_INDEX = "cheaterBankIndex";


    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private Button mCheatButton;
    private TextView mQuestionTextView;

    private TrueFalse[] mQuestionBank = new TrueFalse[] {
            new TrueFalse(R.string.question_africa, false),
            new TrueFalse(R.string.question_oceans, true),
            new TrueFalse(R.string.question_mideast, false),
            new TrueFalse(R.string.question_americas, true),
            new TrueFalse(R.string.question_asia, true)
    };

    private boolean[] mCheaterBank = new boolean[] {
            false,
            false,
            false,
            false,
            false
    };

    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @TargetApi(11)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_quiz);

        // assign the index, if it exists
        if (savedInstanceState != null ) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX,0);
            mIsCheater = savedInstanceState.getBoolean(CHEATER_INDEX, false);
            mCheaterBank = savedInstanceState.getBooleanArray(CHEATER_BANK_INDEX);
        }

        // action bar (run on Honeycomb or later)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar actionBar = getActionBar();
            actionBar.setSubtitle(R.string.action_bar_subtitle);
        }

        mTrueButton = (Button)findViewById(R.id.true_button);
        mFalseButton = (Button)findViewById(R.id.false_button);

        mNextButton = (Button)findViewById(R.id.next_button);
        mPreviousButton = (Button)findViewById(R.id.previous_button);

        mCheatButton = (Button)findViewById(R.id.cheatButton);

        mQuestionTextView = (TextView)findViewById(R.id.question_text_view);
        updateQuestionText();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        if ( mPreviousButton != null ) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    previousQuestion();
                }
            });
        }

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextQuestion();
            }
        });

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start cheat activity
                Intent i = new Intent(GeoQuizActivity.this, CheatActivity.class);
                i.putExtra(CheatActivity.EXTRA_ANSWER_IS_TRUE, mQuestionBank[mCurrentIndex].isTrueQuestion());
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState(Bundle) called");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEATER_INDEX, mIsCheater);
        savedInstanceState.putBooleanArray(CHEATER_BANK_INDEX, mCheaterBank);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(int, int, Intent) called");
        if ( data != null) {
            mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
            mCheaterBank[mCurrentIndex] = mIsCheater;  // set the corresponding cheat state for this question.
        }

    }

    private void previousQuestion() {
        indexDown();
        updateQuestionText();
    }

    private void nextQuestion() {
        indexUp();
        updateQuestionText();
    }

    private void indexUp() {
        mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
    }

    private void indexDown() {
        mCurrentIndex = (mCurrentIndex - 1 < 0) ? mQuestionBank.length-1 : mCurrentIndex - 1;
    }

    private void updateQuestionText() {
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
        mIsCheater = mCheaterBank[mCurrentIndex];  // set current cheater state
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isTrueQuestion();

        int messageResId;

        // check if the person is a cheater or not
        if ( mIsCheater ) {
            messageResId = R.string.judgment_toast;
        }
        else {
            // user pressed the correct answer
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.toast_correct;
            } else {
                messageResId = R.string.toast_incorrect;
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

