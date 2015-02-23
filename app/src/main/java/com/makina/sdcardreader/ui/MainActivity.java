package com.makina.sdcardreader.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.makina.sdcardreader.R;
import com.makina.sdcardreader.model.MountPoint;

/**
 * this is the main {@code Activity} of this application.
 *  
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class MainActivity extends ActionBarActivity implements MountPointListFragment.OnMountPointListFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, MountPointListFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onMountPointSelected(MountPoint mountPoint) {
        
    }
}
