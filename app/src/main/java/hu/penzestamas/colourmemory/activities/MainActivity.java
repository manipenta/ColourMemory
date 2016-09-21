package hu.penzestamas.colourmemory.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.adapters.CardAdapter;
import hu.penzestamas.colourmemory.fragments.GameFinishedDialog;
import hu.penzestamas.colourmemory.models.CardModel;
import hu.penzestamas.colourmemory.utils.GameCardView;
import hu.penzestamas.colourmemory.utils.GridSpaceDecorator;
import hu.penzestamas.colourmemory.utils.StoredInfo;

/**
 * Main Activity for the Application responsible for displaying the Game board, and handling point calculation and maintaining the state of the cards.
 */
public class MainActivity extends AppCompatActivity implements CardAdapter.CardsFlippedListener, GameFinishedDialog.OnNewGameStartedListener {

    private static final String TOTAL_POINTS = "total.points";
    private static final String CARD_NUMBER = "card.number";
    private static final String CARD_LIST = "card.list";

    private RecyclerView mCardGrid;
    private CardAdapter mAdapter;
    private int mTotalPoint = 0;
    private int mCardNum = 16;
    private GameFinishedDialog mDialog;
    private ArrayList<CardModel> mCardList;
    private Vibrator mVibrator;

    private TextView mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);

        mTitle = (TextView) toolbar.findViewById(R.id.activity_title);
        mCardGrid = (RecyclerView) findViewById(R.id.card_recycler_view);
        mCardGrid.setHasFixedSize(true);
        mCardGrid.addItemDecoration(new GridSpaceDecorator(this, getResources().getInteger(R.integer.decoration_space)));

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (savedInstanceState != null) {
            restoreGame(savedInstanceState);
        } else {
            initializeGame();
        }


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * Saves the current point, the number of cards on board and the card list.
     *
     * @param outState the Bundle the data are saved into.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TOTAL_POINTS, mTotalPoint);
        outState.putInt(CARD_NUMBER, mCardNum);
        outState.putParcelableArrayList(CARD_LIST, mCardList);
    }

    /**
     * Restore game after activity is recreated due to configuration changes etc.
     *
     * @param bundle The bundle containing restorable data.
     */
    public void restoreGame(Bundle bundle) {
        mTotalPoint = bundle.getInt(TOTAL_POINTS, 0);
        mCardNum = bundle.getInt(CARD_NUMBER, 16);
        mTitle.setText(Integer.toString(mTotalPoint));
        mCardList = bundle.getParcelableArrayList(CARD_LIST);
        mAdapter = new CardAdapter(this, R.layout.card_item_layout, mCardList, this);
        mCardGrid.setAdapter(mAdapter);


    }

    /**
     * Initialize game board at the beginning of the game.
     */
    public void initializeGame() {

        mTotalPoint = 0;
        mCardNum = 16;
        mTitle.setText(Integer.toString(mTotalPoint));

        mCardList = new ArrayList<>();
        mCardList.add(new CardModel(R.drawable.colour1, true, true));
        mCardList.add(new CardModel(R.drawable.colour1, true, true));
        mCardList.add(new CardModel(R.drawable.colour2, true, true));
        mCardList.add(new CardModel(R.drawable.colour2, true, true));
        mCardList.add(new CardModel(R.drawable.colour3, true, true));
        mCardList.add(new CardModel(R.drawable.colour3, true, true));
        mCardList.add(new CardModel(R.drawable.colour4, true, true));
        mCardList.add(new CardModel(R.drawable.colour4, true, true));
        mCardList.add(new CardModel(R.drawable.colour5, true, true));
        mCardList.add(new CardModel(R.drawable.colour5, true, true));
        mCardList.add(new CardModel(R.drawable.colour6, true, true));
        mCardList.add(new CardModel(R.drawable.colour6, true, true));
        mCardList.add(new CardModel(R.drawable.colour7, true, true));
        mCardList.add(new CardModel(R.drawable.colour7, true, true));
        mCardList.add(new CardModel(R.drawable.colour8, true, true));
        mCardList.add(new CardModel(R.drawable.colour8, true, true));

        Collections.shuffle(mCardList);
        mAdapter = new CardAdapter(this, R.layout.card_item_layout, mCardList, this);

        mCardGrid.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.action_highscore) {
            startActivity(new Intent(this, HighScoreActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Plays a sound.
     *
     * @param sound the resource identifier for the sound to play.
     */
    public void playSound(int sound) {
        MediaPlayer mPlayer = MediaPlayer.create(this.getApplicationContext(), sound);
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
    }


    /**
     * Checks if the flipped two card are the same and updates total point accordingly.
     * Flips back cards if its not a match and make them disappear if it is.
     * Creates{@link GameFinishedDialog} if the game ended.
     *
     * @param first  The first flipped Card.
     * @param second The second flipped Card.
     */
    @Override
    public void onCardsFlipped(final GameCardView first, final GameCardView second) {
        if (first.getModel().getDrawable() == second.getModel().getDrawable()) {
            mTotalPoint += 5;
            mCardNum -= 2;
            if (StoredInfo.isVibrateEnabled(this.getApplicationContext())) mVibrator.vibrate(500);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //first.setVisibility(false);
                    // second.setVisibility(false);
                    mAdapter.disappearItem(first.getModel());
                    mAdapter.disappearItem(second.getModel());
                    if (StoredInfo.isSoundEnabled(MainActivity.this.getApplicationContext())) {
                        playSound(R.raw.disappear);
                    }
                    if (mCardNum <= 0) {
                        if (StoredInfo.isSoundEnabled(MainActivity.this.getApplicationContext())) {
                            playSound(R.raw.tada);
                        }
                        mDialog = GameFinishedDialog.newInstance(mTotalPoint);
                        mDialog.show(MainActivity.this.getSupportFragmentManager(), "Dialog");
                    }
                }
            }, 200);


        } else {
            mTotalPoint -= 1;
            if (StoredInfo.isVibrateEnabled(this.getApplicationContext())) mVibrator.vibrate(200);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (StoredInfo.isSoundEnabled(MainActivity.this.getApplicationContext())) {
                        playSound(R.raw.card);
                    }
                    first.flipBackCard();
                    second.flipBackCard();
                }
            }, 200);

        }
        mTitle.setText(Integer.toString(mTotalPoint));
    }

    /**
     * Initializes Game board for a new game.
     */
    @Override
    public void onStartNewGame() {
        initializeGame();
    }

    @Override
    public void onExitGame() {
        this.finish();
    }


}
