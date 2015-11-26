package sc.sn.android.commons.util;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import sc.sn.android.commons.BuildConfig;
import sc.sn.android.commons.model.MountPoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests about {@link MountPointUtils}.
 *
 * @author S. Grimault
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class,
        sdk = Build.VERSION_CODES.JELLY_BEAN)
public class MountPointUtilsTest {

    @Test
    public void testGetInternalStorage() {
        final MountPoint mountPoint = MountPointUtils.getInternalStorage();

        assertNotNull(mountPoint);
        assertEquals(MountPoint.StorageType.INTERNAL,
                     mountPoint.getStorageType());
    }

    @Test
    public void testGetMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = MountPointUtils.getMountPointsFromSystemEnv();

        assertNotNull(mountPoints);
        assertEquals(1,
                     mountPoints.size());

        final MountPoint mountPoint = mountPoints.get(0);
        assertEquals(MountPoint.StorageType.INTERNAL,
                     mountPoint.getStorageType());
    }

    @Test
    public void testFormatStorageSize() {
        long storageInB = 128l;
        final String storageInBFormatted = MountPointUtils.formatStorageSize(RuntimeEnvironment.application,
                                                                             storageInB);
        assertNotNull(storageInB);
        assertEquals("128 B",
                     storageInBFormatted);

        long storageInKb = 1024l;
        final String storageInKbFormatted = MountPointUtils.formatStorageSize(RuntimeEnvironment.application,
                                                                              storageInKb);

        assertNotNull(storageInKb);
        assertEquals("1,0 kB",
                     storageInKbFormatted);

        long storageInMb = storageInKb * 1024;
        final String storageInMbFormatted = MountPointUtils.formatStorageSize(RuntimeEnvironment.application,
                                                                              storageInMb);

        assertNotNull(storageInMb);
        assertEquals("1,0 MB",
                     storageInMbFormatted);

        long storageInGb = storageInMb * 1024;
        final String storageInGbFormatted = MountPointUtils.formatStorageSize(RuntimeEnvironment.application,
                                                                              storageInGb);

        assertNotNull(storageInGb);
        assertEquals("1,0 GB",
                     storageInGbFormatted);
    }
}