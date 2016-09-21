package hu.penzestamas.colourmemory.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.adapters.HighScoreAdapter;
import hu.penzestamas.colourmemory.utils.StoredInfo;

/**
 * A simple Activity responsible for  displaying High Scores using RecyclerView.
 */
public class HighScoreActivity extends AppCompatActivity {

    private RecyclerView mHsListView;
    private HighScoreAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        mHsListView = (RecyclerView) findViewById(R.id.hs_recyclerView);
        mAdapter = new HighScoreAdapter(this.getApplicationContext(), StoredInfo.getHighScores(this.getApplicationContext()));
        mHsListView.setAdapter(mAdapter);
    }
}
