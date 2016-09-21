package hu.penzestamas.colourmemory.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * A simple {@link android.support.v7.widget.RecyclerView.ItemDecoration} class for providing space between grid elements.
 */
public class GridSpaceDecorator extends RecyclerView.ItemDecoration {
    private int mSpace;

    /**
     * Converts to provided space from dp to pixels then sets it to the object.
     */
    public GridSpaceDecorator(Context context, int space) {
        this.mSpace = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, space, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = mSpace;
        outRect.bottom = mSpace;
        outRect.left = mSpace;
        outRect.right = mSpace;
    }
}
