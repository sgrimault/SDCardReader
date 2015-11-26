package sc.sn.sdcardreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import sc.sn.android.commons.model.MountPoint;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.ui.fragment.FileRecyclerViewFragment;
import sc.sn.sdcardreader.ui.fragment.MountPointRecyclerViewFragment;

/**
 * This is the main {@code Activity} of this application.
 *
 * @author S. Grimault
 */
public class MainActivity
        extends AppCompatActivity
        implements MountPointRecyclerViewFragment.OnMountPointRecyclerViewFragmentListener,
                   FileRecyclerViewFragment.OnFileRecyclerViewFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_single);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .add(android.R.id.content,
                                            MountPointRecyclerViewFragment.newInstance())
                                       .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings,
                                  menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings:
                startActivity(new Intent(this,
                                         SettingsActivity.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMountPointSelected(MountPoint mountPoint) {
        getSupportFragmentManager().beginTransaction()
                // apply slide in / slide out animation effect between fragments
                .setCustomAnimations(R.anim.fragment_enter_slide_left,
                                     R.anim.fragment_exit_slide_left,
                                     R.anim.fragment_pop_enter_slide_right,
                                     R.anim.fragment_pop_exit_slide_right)
                .replace(android.R.id.content,
                         FileRecyclerViewFragment.newInstance(new File(mountPoint.getMountPath())),
                         FileRecyclerViewFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFileSelected(File file) {
        if (file.isDirectory()) {
            getSupportFragmentManager().beginTransaction()
                    // apply slide in / slide out animation effect between fragments
                    .setCustomAnimations(R.anim.fragment_enter_slide_left,
                                         R.anim.fragment_exit_slide_left,
                                         R.anim.fragment_pop_enter_slide_right,
                                         R.anim.fragment_pop_exit_slide_right)
                    .replace(android.R.id.content,
                             FileRecyclerViewFragment.newInstance(file),
                             FileRecyclerViewFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
        else {
            Toast.makeText(this,
                           file.getPath(),
                           Toast.LENGTH_SHORT)
                 .show();
        }
    }
}

