package sc.sn.sdcardreader.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
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
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragmentActivity
        extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                                .add(
                                        android.R.id.content,
                                        new SettingsFragment(),
                                        SettingsFragment.class.getName()
                                )
                                .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Global preferences.
     *
     * @author S. Grimault
     */
    public static class SettingsFragment
            extends PreferenceFragment {

        private static final String KEY_PREFERENCE_ABOUT_APP_VERSION = "app_version";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onViewCreated(View view,
                                  Bundle savedInstanceState) {
            super.onViewCreated(
                    view,
                    savedInstanceState
            );

            getPreferenceScreen().findPreference(KEY_PREFERENCE_ABOUT_APP_VERSION)
                                 .setSummary(
                                         getString(
                                                 R.string.app_version,
                                                 BuildConfig.VERSION_NAME,
                                                 BuildConfig.VERSION_CODE,
                                                 DateFormat.getDateTimeInstance()
                                                           .format(
                                                                   new Date(Long.valueOf(BuildConfig.BUILD_DATE))
                                                           )
                                         )
                                 );
        }
    }
}
