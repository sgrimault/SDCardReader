package sc.sn.sdcardreader.loader;

import android.content.Context;

import java.util.List;

import sc.sn.sdcardreader.model.MountPoint;
import sc.sn.sdcardreader.util.MountPointUtils;

/**
 * Default {@code Loader} about {@link sc.sn.sdcardreader.model.MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointLoader extends AbstractAsyncTaskLoader<List<MountPoint>> {

    public MountPointLoader(Context context) {
        super(context);
    }

    @Override
    public List<MountPoint> loadInBackground() {
        return MountPointUtils.getMountPoints();
    }
}
