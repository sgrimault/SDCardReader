package sc.sn.sdcardreader.loader;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sc.sn.android.commons.loader.AbstractAsyncTaskLoader;

/**
 * Default {@code Loader} about {@code File}s.
 *
 * S. Grimault
 */
public class FileLoader
        extends AbstractAsyncTaskLoader<List<File>> {

    private final File mCurrentPath;

    /**
     * Default File Comparator (sort folders first then regular files).
     */
    private static final Comparator<File> DEFAULT_FILE_COMPARATOR = new Comparator<File>() {

        @Override
        public int compare(
                File lhs,
                File rhs) {

            if (lhs.isDirectory() && !rhs.isDirectory()) {
                return -1;
            }

            if (!lhs.isDirectory() && rhs.isDirectory()) {
                return 1;
            }

            // default comparison
            return lhs.compareTo(rhs);
        }
    };

    public FileLoader(Context context, File pCurrentPath) {
        super(context);

        this.mCurrentPath = pCurrentPath;
    }

    @Override
    public List<File> loadInBackground() {
        final List<File> files = new ArrayList<>();

        if (this.mCurrentPath.exists() && this.mCurrentPath.canRead() && this.mCurrentPath.isDirectory()) {
            Collections.addAll(
                    files,
                    this.mCurrentPath.listFiles()
            );
            Collections.sort(
                    files,
                    DEFAULT_FILE_COMPARATOR
            );
        }

        return files;
    }
}
