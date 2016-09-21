package hu.penzestamas.colourmemory.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.models.HighScoreItem;
import hu.penzestamas.colourmemory.utils.StoredInfo;

/**
 * DialogFragment subclass, responsible for notifying the user when the game ends.
 * Activities that contain this fragment must implement the
 * {@link OnNewGameStartedListener} interface
 * to handle interaction events.
 * Use the {@link GameFinishedDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFinishedDialog extends DialogFragment {

    private static final String ARG_SCORE = "score";

    private int mScore;

    private EditText mNameText;
    private TextView mFinishedText;

    private boolean mIsHighScore = false;

    private OnNewGameStartedListener mListener;

    public GameFinishedDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param score Total score achieved by the user.
     * @return A new instance of fragment GameFinishedDialog.
     */

    public static GameFinishedDialog newInstance(int score) {
        GameFinishedDialog fragment = new GameFinishedDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_SCORE, score);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScore = getArguments().getInt(ARG_SCORE, 0);
            mIsHighScore = StoredInfo.isHighScore(getContext().getApplicationContext(), mScore);
        }
    }


    /**
     * Sets Dialog to appear without title.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_finished_dialog, container, false);

        Button mSaveButton = (Button) view.findViewById(R.id.dialog_button_exit);
        Button mPlayButton = (Button) view.findViewById(R.id.dialog_button_newgame);
        mNameText = (EditText) view.findViewById(R.id.dialog_edit_name);
        mFinishedText = (TextView) view.findViewById(R.id.dialog_text_finished);

        if (mIsHighScore) {
            mFinishedText.setText(getContext().getResources().getString(R.string.text_high_score, mScore));
        } else {
            mFinishedText.setText(getContext().getResources().getString(R.string.text_no_high, mScore));
        }


        mSaveButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks for correct input, then saves the high score using {@link StoredInfo}, then exits the application.
             */
            @Override
            public void onClick(View v) {
                if (mIsHighScore) {
                    if (mNameText.getText().toString().matches("[a-zA-Z0-9. ]*") && !mNameText.getText().toString().equals("")) {
                        StoredInfo.saveNewHighScoreItem(GameFinishedDialog.this.getContext().getApplicationContext(), new HighScoreItem(mNameText.getText().toString(), mScore));
                        GameFinishedDialog.this.dismiss();
                        mListener.onExitGame();
                    } else {
                        mNameText.setError("Name must not contain special characters!");
                    }
                }


            }
        });

        /**
         * Checks for correct input, then saves the high score using {@link StoredInfo}, then notifies containing activity to start a new game.
         */
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsHighScore) {
                    if (mNameText.getText().toString().matches("[a-zA-Z0-9. ]*") && !mNameText.getText().toString().equals("")) {
                        StoredInfo.saveNewHighScoreItem(GameFinishedDialog.this.getContext().getApplicationContext(), new HighScoreItem(mNameText.getText().toString(), mScore));
                        mListener.onStartNewGame();
                        GameFinishedDialog.this.dismiss();
                    } else {
                        mNameText.setError("Name must not contain special characters!");
                    }
                }


            }
        });


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnNewGameStartedListener) {
            mListener = (OnNewGameStartedListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity.
     */
    public interface OnNewGameStartedListener {
        void onStartNewGame();

        void onExitGame();
    }
}
