package com.jrw82.android.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Ryan on 3/9/2015.
 */
public class CheatActivity extends Activity {
    private static final String TAG = "CheatActivity";
    public static final String EXTRA_ANSWER_IS_TRUE = "com.jrw82.android.geoquiz.answer_is_true";
    public static final String EXTRA_ANSWER_SHOWN = "com.jrw82.android.geoquiz.answer_shown";
    private static final String ANSWER_SHOWN_INDEX = "answerShownIndex";

    private boolean mAnswerIsTrue;
    private boolean mAnswerIsShown = false;

    private Button mShowAnswer;
    private TextView mAnswerTextView;


    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        mAnswerIsShown = isAnswerShown;
        data.putExtra(EXTRA_ANSWER_SHOWN, mAnswerIsShown);
        setResult(RESULT_OK, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState(Bundle) called");
        savedInstanceState.putBoolean(ANSWER_SHOWN_INDEX, mAnswerIsShown);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_cheat);

        if ( savedInstanceState != null) {
            mAnswerIsShown = savedInstanceState.getBoolean(ANSWER_SHOWN_INDEX, false);
        }

        // get the information on the question answer from the intent
        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
        mAnswerTextView = (TextView) findViewById(R.id.answerTextView);

        mShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleShowAnswerClicked();
            }
        });

        // if the answer was shown previously, then pretend the user clicked the button
        if ( mAnswerIsShown ) {
            handleShowAnswerClicked();
        }
    }

    // handle user pressing the show answer button
    private void handleShowAnswerClicked() {
        showAnswer();
        setAnswerShownResult(true);
    }

    // show the answer text
    private void showAnswer() {
        int textId = mAnswerIsTrue ? R.string.true_button : R.string.false_button;
        mAnswerTextView.setText(textId);
    }

}
