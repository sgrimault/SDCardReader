package sc.sn.sdcardreader.ui.adapter;

import android.os.Environment;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.util.ArrayList;
import java.util.List;

import sc.sn.sdcardreader.CustomRobolectricRunner;
import sc.sn.sdcardreader.model.MountPoint;

/**
 * Unit tests about {@link sc.sn.sdcardreader.ui.adapter.MountPointAdapter}.
 *
 * @author S. Grimault
 */
@RunWith(CustomRobolectricRunner.class)
public class MountPointAdapterTest {

    MountPointAdapter mountPointAdapter;

    @Before
    public void setUp() throws Exception {
        mountPointAdapter = new MountPointAdapter(Robolectric.application, null);
    }

    @Test
    public void testAddAll() {
        Assert.assertEquals(0, mountPointAdapter.getItemCount());

        mountPointAdapter.addAll(buildMountPoints());

        Assert.assertEquals(2, mountPointAdapter.getItemCount());
    }

    @Test
    public void testGetItem() {
        final List<MountPoint> mountPoints = buildMountPoints();

        mountPointAdapter.addAll(mountPoints);

        Assert.assertEquals(mountPoints.get(0), mountPointAdapter.getItem(0));
        Assert.assertEquals(mountPoints.get(1), mountPointAdapter.getItem(1));
    }

    @Test
    public void testClear() {
        mountPointAdapter.addAll(buildMountPoints());
        mountPointAdapter.clear();

        Assert.assertEquals(0, mountPointAdapter.getItemCount());
    }

    private List<MountPoint> buildMountPoints() {
        final List<MountPoint> mountPoints = new ArrayList<>();
        mountPoints.add(new MountPoint("/mnt/sdcard", Environment.MEDIA_MOUNTED, MountPoint.StorageType.INTERNAL));
        mountPoints.add(new MountPoint("/mnt/extSdCard", Environment.MEDIA_MOUNTED, MountPoint.StorageType.EXTERNAL));

        return mountPoints;
    }
}