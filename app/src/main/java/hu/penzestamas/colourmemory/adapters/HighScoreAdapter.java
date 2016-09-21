package hu.penzestamas.colourmemory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hu.penzestamas.colourmemory.R;
import hu.penzestamas.colourmemory.models.HighScoreItem;

/**
 * Adapter for displaying a single {@link HighScoreItem} instance.
 */
public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreAdapter.ViewHolder> {

    private List<HighScoreItem> mHsList;
    private Context mContext;

    public HighScoreAdapter(Context mContext, List<HighScoreItem> mHsList) {
        this.mHsList = mHsList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.high_score_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HighScoreItem current = mHsList.get(position);

        holder.mRankText.setText(Integer.toString(position + 1));
        holder.mNameText.setText(current.getName());
        holder.mScoreText.setText(Integer.toString(current.getScore()));

    }

    @Override
    public int getItemCount() {
        return mHsList.size();
    }


    /**
     * Static ViewHolder for the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mRankText;
        private TextView mNameText;
        private TextView mScoreText;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mRankText = (TextView) itemView.findViewById(R.id.hs_rank_text);
            mNameText = (TextView) itemView.findViewById(R.id.hs_name_text);
            mScoreText = (TextView) itemView.findViewById(R.id.hs_score_text);

        }
    }
}
