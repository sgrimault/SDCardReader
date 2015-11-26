package sc.sn.sdcardreader.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import sc.sn.sdcardreader.ui.fragment.SettingsFragment;

/**
 * Global preferences.
 *
 * @author S. Grimault
 * @see sc.sn.sdcardreader.ui.fragment.SettingsFragment
 */
public class SettingsActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                                   .replace(android.R.id.content,
                                            new SettingsFragment())
                                   .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
