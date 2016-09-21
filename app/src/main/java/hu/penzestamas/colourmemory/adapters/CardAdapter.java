package hu.penzestamas.colourmemory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import hu.penzestamas.colourmemory.models.CardModel;
import hu.penzestamas.colourmemory.utils.GameCardView;

/**
 * Adapter for displaying a single CardModel instance.
 * Activities that contains this adapter, must implement
 * {@link CardsFlippedListener} interface.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<CardModel> mCardList;
    private int mRowResource;
    private Context mContext;
    private GameCardView mFirstCard = null;
    private CardsFlippedListener mListener;


    public CardAdapter(Context context, int rowresource, List<CardModel> mCardList, CardsFlippedListener listener) {
        this.mCardList = mCardList;
        this.mRowResource = rowresource;
        this.mContext = context;
        this.mListener = listener;

    }

    /**
     * Creates the ViewHolder for one grid element.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(mRowResource, parent, false);

        return new ViewHolder(view);
    }

    /**
     * Sets ViewHolder's views.
     * Sets {@link CardModel} model for the {@link GameCardView} view.
     * Registers for {@link GameCardView}'s {@link hu.penzestamas.colourmemory.utils.GameCardView.CardFlippedListener}
     * and calls onCardsFlipped on every second card flipped.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mItem = mCardList.get(position);
        holder.mCardView.setModel(holder.mItem);

        holder.mCardView.setOnCardFlippedListener(new GameCardView.CardFlippedListener() {

            @Override
            public void onCardFlipped(GameCardView view) {
                if (mFirstCard != null) {
                    mListener.onCardsFlipped(mFirstCard, view);
                    mFirstCard = null;

                } else {
                    mFirstCard = view;
                }
            }
        });

    }

    /**
     * Updates adapter at the provided position.
     *
     * @param position The position that needs to be updated.
     */
    public void update(int position) {
        notifyItemChanged(position);
    }

    /**
     * Make the provided model's view disappear.
     * Notifies adapter by calling update.
     *
     * @param model the model to make invisible
     */
    public void disappearItem(CardModel model) {
        model.setVisible(false);
        model.setFlipped(true);
        update(mCardList.indexOf(model));
    }

    /**
     * Deletes all items from adapter.
     */
    public void clear() {
        mCardList.clear();
        notifyDataSetChanged();
    }

    /**
     * Adds a list of new items to adapter.
     *
     * @param list List of items that need to be added.
     */
    public void addAll(List<CardModel> list) {
        mCardList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Returns provided {@link CardModel}'s position in the adapter.
     *
     * @param model The model.
     * @return The position.
     */
    public int getPosition(CardModel model) {
        return mCardList.indexOf(model);
    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }

    /**
     * Listener interface that notifies activity when two cards flipped.
     */
    public interface CardsFlippedListener {
        void onCardsFlipped(GameCardView first, GameCardView second);
    }

    /**
     * Static ViewHolder for the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardModel mItem;
        private GameCardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (GameCardView) itemView;

        }
    }
}
