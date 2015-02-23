package com.makina.sdcardreader.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.makina.sdcardreader.model.MountPoint;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class helper about {@code File} and {@link com.makina.sdcardreader.model.MountPoint}.
 *
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class FileUtils {

    /**
     * Retrieves a {@code List} of {@link com.makina.sdcardreader.model.MountPoint}s from {@code System}
     * environment.
     *
     * @return a {@code List} of available {@link com.makina.sdcardreader.model.MountPoint}s
     */
    @NonNull
    public static List<MountPoint> getMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        final String externalStorage = System.getenv("EXTERNAL_STORAGE");

        if (!TextUtils.isEmpty(externalStorage)) {
            mountPoints.add(
                    new MountPoint(
                            externalStorage,
                            Environment.getExternalStorageState(),
                            MountPoint.StorageType.INTERNAL
                    )
            );
        }

        final String secondaryStorage = System.getenv("SECONDARY_STORAGE");

        if (!TextUtils.isEmpty(secondaryStorage)) {
            final String[] paths = secondaryStorage.split(":");
            boolean firstSecondaryStorage = true;

            for (String path : paths) {
                final File file = new File(path);
                String storageState = Environment.MEDIA_UNMOUNTED;

                if (file.isDirectory()) {

                    if (file.canWrite()) {
                        storageState = Environment.MEDIA_MOUNTED;
                    }
                    else if (file.canRead()) {
                        storageState = Environment.MEDIA_MOUNTED_READ_ONLY;
                    }

                    mountPoints.add(
                            new MountPoint(
                                    path,
                                    storageState,
                                    (firstSecondaryStorage) ? MountPoint.StorageType.EXTERNAL : MountPoint.StorageType.USB
                            )
                    );
                    firstSecondaryStorage = false;
                }
            }
        }

        return mountPoints;
    }
}
