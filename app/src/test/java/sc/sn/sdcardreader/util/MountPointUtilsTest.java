package sc.sn.sdcardreader.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;

import java.util.List;

import sc.sn.sdcardreader.CustomRobolectricRunner;
import sc.sn.sdcardreader.model.MountPoint;

/**
 * Unit tests about {@link MountPointUtils}.
 *
 * @author S. Grimault
 */
@RunWith(CustomRobolectricRunner.class)
public class MountPointUtilsTest {

    @Test
    public void testGetInternalStorage() {
        final MountPoint mountPoint = MountPointUtils.getInternalStorage();

        Assert.assertNotNull(mountPoint);
        Assert.assertEquals(MountPoint.StorageType.INTERNAL, mountPoint.getStorageType());
    }

    @Test
    public void testGetMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = MountPointUtils.getMountPointsFromSystemEnv();

        Assert.assertNotNull(mountPoints);
        Assert.assertEquals(1, mountPoints.size());

        final MountPoint mountPoint = mountPoints.get(0);
        Assert.assertEquals(MountPoint.StorageType.INTERNAL, mountPoint.getStorageType());
    }

    @Test
    public void testFormatStorageSize() {
        long storageInB = 128l;
        final String storageInBFormatted = MountPointUtils.formatStorageSize(Robolectric.application,
                                                                             storageInB);
        Assert.assertNotNull(storageInB);
        Assert.assertEquals("128 B", storageInBFormatted);

        long storageInKb = 1024l;
        final String storageInkbFormatted = MountPointUtils.formatStorageSize(Robolectric.application,
                                                                        storageInKb);

        Assert.assertNotNull(storageInKb);
        Assert.assertEquals("1,0 kB", storageInkbFormatted);

        long storageInMb = storageInKb * 1024;
        final String storageInMbFormatted = MountPointUtils.formatStorageSize(Robolectric.application,
                                                                              storageInMb);

        Assert.assertNotNull(storageInMb);
        Assert.assertEquals("1,0 MB", storageInMbFormatted);

        long storageInGb = storageInMb * 1024;
        final String storageInGbFormatted = MountPointUtils.formatStorageSize(Robolectric.application,
                                                                              storageInGb);

        Assert.assertNotNull(storageInGb);
        Assert.assertEquals("1,0 GB", storageInGbFormatted);
    }
}