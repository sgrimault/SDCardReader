package sc.sn.sdcardreader.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.util.PreferenceUtils;

/**
 * Global preferences.
 *
 * @author S. Grimault
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragmentActivity
        extends AppCompatActivity {

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

            PreferenceUtils.updatePreferences(getPreferenceScreen());
        }
    }
}
