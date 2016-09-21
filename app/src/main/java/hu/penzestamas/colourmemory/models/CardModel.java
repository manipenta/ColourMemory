package hu.penzestamas.colourmemory.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * POJO for a card item.
 * Implements {@link Parcelable} interface so it can be passed between activities.
 */
public class CardModel implements Parcelable {

    private int drawable;

    private boolean isFlipped;

    private boolean isVisible;

    private CardModel(Parcel in) {
        this.drawable = in.readInt();
        this.isFlipped = in.readInt() != 0;
        this.isVisible = in.readInt() != 0;
    }

    public CardModel() {
    }

    public CardModel(int drawable, boolean isFlipped, boolean isVisible) {
        this.drawable = drawable;
        this.isFlipped = isFlipped;
        this.isVisible = isVisible;
    }


    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isFlipped() {
        return isFlipped;
    }

    public void setFlipped(boolean flipped) {
        isFlipped = flipped;
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(drawable);
        dest.writeInt(isFlipped ? 1 : 0);
        dest.writeInt(isVisible ? 1 : 0);

    }

    //Creator for Parcelable interface
    public static final Parcelable.Creator<CardModel> CREATOR = new Parcelable.Creator<CardModel>() {

        @Override
        public CardModel createFromParcel(Parcel source) {
            return new CardModel(source);
        }

        @Override
        public CardModel[] newArray(int size) {
            return new CardModel[size];
        }
    };
}
