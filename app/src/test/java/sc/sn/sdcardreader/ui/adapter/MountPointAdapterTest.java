package sc.sn.sdcardreader.ui.adapter;

import android.os.Environment;
import android.support.v7.widget.RecyclerView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

    private MountPointAdapter mountPointAdapter;
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Before
    public void setUp() throws Exception {
        mountPointAdapter = Mockito.spy(
                new MountPointAdapter(
                        Robolectric.application,
                        null
                )
        );
        adapterDataObserver = Mockito.mock(RecyclerView.AdapterDataObserver.class);
        mountPointAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Test
    public void testClear() {
        final List<MountPoint> mountPoints = buildMountPoints();

        mountPointAdapter.addAll(mountPoints);
        mountPointAdapter.clear();

        Mockito.verify(
                mountPointAdapter,
                Mockito.times(1)
        ).notifyItemRangeRemoved(
                0,
                mountPoints.size()
        );
        Mockito.verify(
                adapterDataObserver,
                Mockito.times(1)
        ).onItemRangeRemoved(
                0,
                mountPoints.size()
        );

        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );
    }

    @Test
    public void testInsert() {
        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );

        final MountPoint mountPoint = buildMountPoint();
        mountPointAdapter.insert(
                mountPoint,
                0
        );

        Mockito.verify(
                mountPointAdapter,
                Mockito.times(1)
        ).notifyItemInserted(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.times(1)
        ).onItemRangeInserted(
                0,
                1
        );
        Mockito.verify(
                mountPointAdapter,
                Mockito.never()
        ).notifyDataSetChanged();

        Assert.assertEquals(
                1,
                mountPointAdapter.getItemCount()
        );

        mountPointAdapter.insert(
                mountPoint,
                2
        );
        Mockito.verify(
                mountPointAdapter,
                Mockito.atLeastOnce()
        ).add(mountPoint);

        mountPointAdapter.insert(
                mountPoint,
                -1
        );
        Mockito.verify(
                mountPointAdapter,
                Mockito.atLeastOnce()
        ).add(mountPoint);
    }

    @Test
    public void testAdd() {
        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );

        final MountPoint mountPoint = buildMountPoint();
        mountPointAdapter.add(mountPoint);

        Mockito.verify(
                mountPointAdapter,
                Mockito.times(1)
        ).notifyItemInserted(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.times(1)
        ).onItemRangeInserted(
                0,
                1
        );
        Mockito.verify(
                mountPointAdapter,
                Mockito.never()
        ).notifyDataSetChanged();

        Assert.assertEquals(
                1,
                mountPointAdapter.getItemCount()
        );
    }

    @Test
    public void testAddAll() {
        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );

        final List<MountPoint> mountPoints = buildMountPoints();
        mountPointAdapter.addAll(mountPoints);

        Mockito.verify(
                mountPointAdapter,
                Mockito.times(1)
        ).notifyItemRangeInserted(
                0,
                mountPoints.size()
        );
        Mockito.verify(
                adapterDataObserver,
                Mockito.times(1)
        ).onItemRangeInserted(
                0,
                mountPoints.size()
        );
        Mockito.verify(
                mountPointAdapter,
                Mockito.never()
        ).notifyDataSetChanged();

        Assert.assertEquals(
                2,
                mountPointAdapter.getItemCount()
        );
    }

    @Test
    public void testGetItem() {
        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );

        final MountPoint mountPoint = mountPointAdapter.getItem(0);
        Assert.assertNull(mountPoint);

        final List<MountPoint> mountPoints = buildMountPoints();

        mountPointAdapter.addAll(mountPoints);
        Assert.assertEquals(
                mountPoints.get(0),
                mountPointAdapter.getItem(0)
        );
        Assert.assertEquals(
                mountPoints.get(1),
                mountPointAdapter.getItem(1)
        );
    }

    @Test
    public void testRemove() {
        Assert.assertEquals(
                0,
                mountPointAdapter.getItemCount()
        );

        final MountPoint mountPoint = buildMountPoint();

        mountPointAdapter.remove(0);
        Mockito.verify(
                mountPointAdapter,
                Mockito.never()
        ).notifyItemRemoved(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.never()
        ).onItemRangeRemoved(
                0,
                1
        );

        mountPointAdapter.remove(mountPoint);
        Mockito.verify(
                mountPointAdapter,
                Mockito.never()
        ).notifyItemRemoved(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.never()
        ).onItemRangeRemoved(
                0,
                1
        );

        mountPointAdapter.add(mountPoint);
        mountPointAdapter.remove(0);
        Mockito.verify(
                mountPointAdapter,
                Mockito.atLeastOnce()
        ).notifyItemRemoved(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.atLeastOnce()
        ).onItemRangeRemoved(
                0,
                1
        );

        mountPointAdapter.add(mountPoint);
        mountPointAdapter.remove(mountPoint);
        Mockito.verify(
                mountPointAdapter,
                Mockito.atLeastOnce()
        ).notifyItemRemoved(0);
        Mockito.verify(
                adapterDataObserver,
                Mockito.atLeastOnce()
        ).onItemRangeRemoved(
                0,
                1
        );
    }

    private MountPoint buildMountPoint() {
        return new MountPoint(
                "/mnt/sdcard",
                Environment.MEDIA_MOUNTED,
                MountPoint.StorageType.INTERNAL
        );
    }

    private List<MountPoint> buildMountPoints() {
        final List<MountPoint> mountPoints = new ArrayList<>();
        mountPoints.add(buildMountPoint());
        mountPoints.add(
                new MountPoint(
                        "/mnt/extSdCard",
                        Environment.MEDIA_MOUNTED,
                        MountPoint.StorageType.EXTERNAL
                )
        );

        return mountPoints;
    }
}