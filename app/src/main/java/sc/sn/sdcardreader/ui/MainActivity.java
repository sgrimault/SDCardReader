package sc.sn.sdcardreader.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import java.io.File;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.model.MountPoint;
import sc.sn.sdcardreader.ui.fragment.FileRecyclerViewFragment;
import sc.sn.sdcardreader.ui.fragment.MountPointRecyclerViewFragment;

/**
 * This is the main {@code Activity} of this application.
 *
 * @author S. Grimault
 */
public class MainActivity
        extends ActionBarActivity
        implements MountPointRecyclerViewFragment.OnMountPointRecyclerViewFragmentListener,
                   FileRecyclerViewFragment.OnFileRecyclerViewFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.container,
                                            MountPointRecyclerViewFragment.newInstance())
                                       .commit();
        }
    }

    @Override
    public void onMountPointSelected(MountPoint mountPoint) {
        getSupportFragmentManager().beginTransaction()
                // apply slide in / slide out animation effect between fragments
                .setCustomAnimations(
                        R.anim.fragment_enter_slide_left,
                        R.anim.fragment_exit_slide_left,
                        R.anim.fragment_pop_enter_slide_right,
                        R.anim.fragment_pop_exit_slide_right)
                .replace(R.id.container,
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
                    .setCustomAnimations(
                            R.anim.fragment_enter_slide_left,
                            R.anim.fragment_exit_slide_left,
                            R.anim.fragment_pop_enter_slide_right,
                            R.anim.fragment_pop_exit_slide_right)
                    .replace(R.id.container,
                             FileRecyclerViewFragment.newInstance(file),
                             FileRecyclerViewFragment.class.getName())
                    .addToBackStack(null)
                    .commit();
        }
        else {
            Toast.makeText(this,
                           file.getPath(),
                           Toast.LENGTH_SHORT
            ).show();
        }
    }
}
