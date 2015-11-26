package sc.sn.sdcardreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;

import java.text.DateFormat;
import java.util.Date;

import sc.sn.sdcardreader.BuildConfig;
import sc.sn.sdcardreader.R;

/**
 * Global preferences.
 *
 * @author S. Grimault
 */
public class SettingsFragment
        extends PreferenceFragmentCompat {

    private static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle bundle,
                                    String s) {
        // load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onViewCreated(View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view,
                            savedInstanceState);

        updateAboutAppVersionPreference(getPreferenceScreen());
    }

    private void updateAboutAppVersionPreference(@NonNull final PreferenceScreen preferenceScreen) {
        final String preferenceKey = preferenceScreen.getContext()
                                                     .getString(R.string.preference_category_about_app_version_key);
        final Preference preference = preferenceScreen.findPreference(preferenceKey);

        if (preference == null) {
            Log.w(TAG,
                  "updateAboutAppVersionPreference: no Preference found for key '" + preferenceKey + "'");

            return;
        }

        preference.setSummary(preferenceScreen.getContext()
                                              .getString(R.string.app_version,
                                                         BuildConfig.VERSION_NAME,
                                                         BuildConfig.VERSION_CODE,
                                                         DateFormat.getDateTimeInstance()
                                                                   .format(new Date(Long.valueOf(BuildConfig.BUILD_DATE)))));
    }
}
