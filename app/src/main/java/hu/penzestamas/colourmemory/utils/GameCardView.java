package hu.penzestamas.colourmemory.utils;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.models.CardModel;

/**
 * Compound view representing a single card view.
 * Handles card animations as well.
 */
public class GameCardView extends FrameLayout {

    private View mFrontView;
    private View mBackView;
    private ImageView mFrontImage;
    private Context mContext;
    private boolean mIsAnimating = false;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private CardFlippedListener mListener;
    private CardModel mModel;
    private boolean isUpFlip = true;
    private MediaPlayer mPlayer;


    public GameCardView(Context context) {
        super(context);
        this.mContext = context;
        initializeViews(context);
    }

    public GameCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initializeViews(context);
    }

    public GameCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initializeViews(context);
    }

    /**
     * Iflates view for GameCardView
     *
     * @param context provided {@link Context} for {@link LayoutInflater}
     */
    private void initializeViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.cardview_layout, this);

    }

    /**
     * Sets Views for the card after inflate is complete.
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mFrontView = findViewById(R.id.card_front);
        mBackView = findViewById(R.id.card_back);
        mFrontImage = (ImageView) findViewById(R.id.card_image_front);

        float scale = mContext.getResources().getDisplayMetrics().density * 8000;
        mBackView.setCameraDistance(scale);
        mFrontView.setCameraDistance(scale);

        // MediaPlayer object to play sounds
        mPlayer = MediaPlayer.create(mContext.getApplicationContext(), R.raw.card);

        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(mContext.getApplicationContext(), R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(mContext.getApplicationContext(), R.animator.in_animation);

        //notifies View if the animation ended. Then notifies adapter, that card is flipped.
        mSetLeftIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (isUpFlip && mListener != null) {
                    mListener.onCardFlipped(GameCardView.this);
                }
                mIsAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mIsAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        // Flips card if CardView is touched.
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mModel != null && mModel.isFlipped()) {
                    flipCard();
                }
            }
        });
    }


    /**
     * Animates and flips card using {@link AnimatorSet}.
     * Plays sound for cardflipping using {@link MediaPlayer}.
     */
    public void flipCard() {
        if (!mIsAnimating) {
            isUpFlip = true;
            if (StoredInfo.isSoundEnabled(mContext.getApplicationContext())) {
                mPlayer.start();
            }

            mSetRightOut.setTarget(mBackView);
            mSetLeftIn.setTarget(mFrontView);
            mSetRightOut.start();
            mSetLeftIn.start();
            mModel.setFlipped(false);
        }

        //  mListener.onCardFlipped(this);


    }

    /**
     * Animates and flips card backward to initial state.
     */
    public void flipBackCard() {
        if (!mIsAnimating) {
            isUpFlip = false;
            mSetRightOut.setTarget(mFrontView);
            mSetLeftIn.setTarget(mBackView);
            mSetRightOut.start();
            mSetLeftIn.start();
            mModel.setFlipped(true);
        }

    }


    /**
     * Sets the {@link CardFlippedListener} listener.
     *
     * @param listener The provided listener.
     */
    public void setOnCardFlippedListener(CardFlippedListener listener) {
        this.mListener = listener;
    }

    public void setFrontResource(int resource) {
        mFrontImage.setImageResource(resource);
    }

    public CardModel getModel() {
        return mModel;
    }


    /**
     * Sets the CardModel for this GameCardView, then sets the visibility and resorce for the view.
     *
     * @param model The provided model.
     */
    public void setModel(CardModel model) {
        this.mModel = model;
        setFrontResource(model.getDrawable());
        setVisibility(model.isVisible() ? VISIBLE : INVISIBLE);
        if (!mModel.isFlipped()) {
            mModel.setFlipped(true);
            flipCard();
        }
    }

    public interface CardFlippedListener {
        void onCardFlipped(GameCardView view);
    }
}
