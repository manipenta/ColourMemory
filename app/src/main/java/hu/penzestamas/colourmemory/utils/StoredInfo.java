package hu.penzestamas.colourmemory.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.penzestamas.colourmemory.models.HighScoreItem;

/**
 * Wrapper class using {@link SharedPreferences} to store {@link HighScoreItem} items in device storage.
 */
public class StoredInfo {

    private static final String PREF_NAME = "colour.memory.pref";
    private static final String HIGH_SCORES_KEY = "high.scores.key";
    private static final String VIBRATE_KEY = "vibrate.key";
    private static final String SOUND_KEY = "sound.key";

    private static SharedPreferences sharedPreferences;

    public static final int HIGH_SCORE_COUNT = 20;

    protected StoredInfo() {

    }


    /**
     * Returns the singleton {@link SharedPreferences} object.
     *
     * @param context Provided Context to get SharedPreferences.
     * @return the SharedPreferences object.
     */
    public static SharedPreferences getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * Reads the String containing the high score list and converts to a {@link HighScoreItem} list using {@link Gson}
     *
     * @param context Context for the getInstance method.
     * @return The {@link HighScoreItem} list.
     */
    public static List<HighScoreItem> getHighScores(Context context) {
        Gson gson = new Gson();
        String temp = getInstance(context).getString(HIGH_SCORES_KEY, null);
        if (temp == null) {
            return new ArrayList<>();
        } else {
            Type collectionType = new TypeToken<List<HighScoreItem>>() {
            }.getType();
            return gson.fromJson(temp, collectionType);
        }
    }


    /**
     * Saves new {@link HighScoreItem} to SharedPreferences.
     * Reads the high score list using getHighScores() method, then adds the new {@link HighScoreItem}
     * then sorts the list.
     * Checks if the size of the list is bigger than the maximum high score item.
     * Converts list to string and stores it to {@link SharedPreferences}
     *
     * @param context Context for the getInstance method.
     * @param item    The item to save.
     */
    public static void saveNewHighScoreItem(Context context, HighScoreItem item) {
        List<HighScoreItem> tempList = getHighScores(context);

        tempList.add(item);

        if (tempList.size() > 1) {

            Collections.sort(tempList, new Comparator<HighScoreItem>() {
                @Override
                public int compare(HighScoreItem o1, HighScoreItem o2) {
                    return o2.getScore() - o1.getScore();
                }
            });
            if (tempList.size() > HIGH_SCORE_COUNT) {
                tempList.remove(HIGH_SCORE_COUNT);
            }
        }
        Gson gson = new Gson();
        getInstance(context).edit().putString(HIGH_SCORES_KEY, gson.toJson(tempList)).commit();
    }

    /**
     * Checks is the provided score is a high score.
     *
     * @param context Context for the getInstance method.
     * @param score   the provided score
     * @return true if score is high score otherwise false.
     */
    public static boolean isHighScore(Context context, int score) {
        List<HighScoreItem> tempList = getHighScores(context);

        return tempList.size() < HIGH_SCORE_COUNT || tempList.get(tempList.size() - 1).getScore() < score;
    }

    public static boolean isVibrateEnabled(Context context) {
        return getInstance(context).getBoolean(VIBRATE_KEY, true);
    }

    public static boolean isSoundEnabled(Context context) {
        return getInstance(context).getBoolean(SOUND_KEY, true);
    }

    public static void setVibrateEnabled(Context context, boolean enabled) {
        getInstance(context).edit().putBoolean(VIBRATE_KEY, enabled).commit();
    }

    public static void setSoundEnabled(Context context, boolean enabled) {
        getInstance(context).edit().putBoolean(SOUND_KEY, enabled).commit();
    }


}
