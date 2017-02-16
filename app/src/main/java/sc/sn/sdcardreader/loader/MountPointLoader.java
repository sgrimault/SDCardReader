package sc.sn.sdcardreader.loader;

import android.content.Context;

import java.util.List;

import sc.sn.android.commons.loader.AbstractAsyncTaskLoader;
import sc.sn.android.commons.model.MountPoint;
import sc.sn.android.commons.util.MountPointUtils;

/**
 * Default {@code Loader} about {@link sc.sn.android.commons.model.MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointLoader
        extends AbstractAsyncTaskLoader<List<MountPoint>> {

    public MountPointLoader(Context context) {
        super(context);
    }

    @Override
    public List<MountPoint> loadInBackground() {
        return MountPointUtils.getMountPoints(getContext());
    }
}
