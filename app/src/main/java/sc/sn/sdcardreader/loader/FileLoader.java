package sc.sn.sdcardreader.loader;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default {@code Loader} about {@code File}s.
 *
 * S. Grimault
 */
public class FileLoader extends AbstractAsyncTaskLoader<List<File>> {

    private final File mCurrentPath;

    public FileLoader(Context context, File pCurrentPath) {
        super(context);

        this.mCurrentPath = pCurrentPath;
    }

    @Override
    public List<File> loadInBackground() {
        final List<File> files = new ArrayList<>();

        if (this.mCurrentPath.exists() && this.mCurrentPath.canRead() && this.mCurrentPath.isDirectory()) {
            Collections.addAll(files, this.mCurrentPath.listFiles());
        }

        return files;
    }
}
