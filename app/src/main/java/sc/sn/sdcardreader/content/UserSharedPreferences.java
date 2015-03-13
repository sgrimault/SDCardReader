package sc.sn.sdcardreader.content;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * User {@code SharedPreferences}.
 *
 * @author S. Grimault
 */
public class UserSharedPreferences {

    private static final String USER_SHARED_PREFERENCES = "USER_SHARED_PREFERENCES";
    private static final String KEY_RECYCLER_VIEW_MODE = "KEY_RECYCLER_VIEW_MODE";

    private final SharedPreferences mSharedPreferences;

    /**
     * The default constructor.
     *
     * @param context the current context to use
     */
    public UserSharedPreferences(final Context context) {
        mSharedPreferences = context.getSharedPreferences(
                USER_SHARED_PREFERENCES,
                Context.MODE_PRIVATE
        );
    }

    @NonNull
    public RecyclerViewMode getRecyclerViewMode() {
        return RecyclerViewMode.valueOf(
                mSharedPreferences.getString(
                        KEY_RECYCLER_VIEW_MODE,
                        RecyclerViewMode.LIST.name()
                )
        );
    }

    public void setRecyclerViewMode(@NonNull final RecyclerViewMode recyclerViewMode) {
        mSharedPreferences.edit()
                          .putString(
                                  KEY_RECYCLER_VIEW_MODE,
                                  recyclerViewMode.name()
                          )
                          .apply();
    }

    /**
     * Gets the display mode used by {@code RecyclerView}.
     *
     * @author S. Grimault
     */
    public static enum RecyclerViewMode {

        /**
         * Standard vertically scrolling list.
         */
        LIST,

        /**
         * A uniform grid.
         */
        GRID
    }
}
