package sc.sn.sdcardreader.util;

import android.preference.PreferenceScreen;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.util.Date;

import sc.sn.sdcardreader.BuildConfig;
import sc.sn.sdcardreader.R;

/**
 * Class helper about application {@code Preference}.
 *
 * @author S. Grimault
 */
public class PreferenceUtils {

    /**
     * Find and updates all {@code Preference}s found for a given {@code PreferenceScreen}.
     *
     * @param preferenceScreen the {@code PreferenceScreen} to use to find all {@code Preference}s
     */
    public static void updatePreferences(@NonNull final PreferenceScreen preferenceScreen) {
        preferenceScreen.findPreference(preferenceScreen.getContext()
                                                        .getString(R.string.preference_category_about_app_version_key))
                        .setSummary(preferenceScreen.getContext()
                                                    .getString(R.string.app_version,
                                                               BuildConfig.VERSION_NAME,
                                                               BuildConfig.VERSION_CODE,
                                                               DateFormat.getDateTimeInstance()
                                                                         .format(new Date(Long.valueOf(BuildConfig.BUILD_DATE)))));
    }
}
