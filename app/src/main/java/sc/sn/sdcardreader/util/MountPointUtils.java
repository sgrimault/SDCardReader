package sc.sn.sdcardreader.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.model.MountPoint;

/**
 * Class helper about {@code File} and {@link sc.sn.sdcardreader.model.MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointUtils {

    /**
     * Return the primary external storage as {@link sc.sn.sdcardreader.model.MountPoint}.
     *
     * @return the primary external storage
     */
    @NonNull
    public static MountPoint getInternalStorage() {
        final String externalStorage = System.getenv("EXTERNAL_STORAGE");

        if (TextUtils.isEmpty(externalStorage)) {
            return new MountPoint(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                  Environment.getExternalStorageState(),
                                  MountPoint.StorageType.INTERNAL);
        }
        else {
            return new MountPoint(externalStorage,
                           Environment.getExternalStorageState(),
                           MountPoint.StorageType.INTERNAL);
        }
    }

    /**
     * Return the secondary external storage as {@link sc.sn.sdcardreader.model.MountPoint} if found.
     *
     * @return the secondary external storage or {@code null} if not found
     */
    @Nullable
    public static MountPoint getExternalStorage() {
        // try to found the secondary external storage using System environment
        final List<MountPoint> mountPoints = getMountPointsFromSystemEnv();
        final Iterator<MountPoint> mountPointIterator = mountPoints.iterator();
        MountPoint externalMountPoint = null;

        while (mountPointIterator.hasNext() && (externalMountPoint == null)) {
            final MountPoint mountPoint = mountPointIterator.next();
            externalMountPoint = (mountPoint.getStorageType().equals(MountPoint.StorageType.EXTERNAL) ? mountPoint : null);
        }

        // fallback: parse file 'vold.fstab' and try to find the secondary external storage
        if (externalMountPoint == null) {
            mountPoints.clear();
            mountPoints.addAll(getMountPointsFromVold());

            while (mountPointIterator.hasNext() && (externalMountPoint == null)) {
                MountPoint mountPoint = mountPointIterator.next();
                externalMountPoint = (mountPoint.getStorageType().equals(MountPoint.StorageType.EXTERNAL) ? mountPoint : null);
            }
        }

        return externalMountPoint;
    }

    /**
     * Retrieves a {@code List} of all available {@link sc.sn.sdcardreader.model.MountPoint}s
     *
     * @return a {@code List} of available {@link sc.sn.sdcardreader.model.MountPoint}s
     *
     * @see #getMountPointsFromSystemEnv()
     * @see #getMountPointsFromVold()
     */
    public static List<MountPoint> getMountPoints() {
        final List<MountPoint> mountPoints = getMountPointsFromSystemEnv();

        // fallback: parse file 'vold.fstab' and try to find all external storage
        if (mountPoints.size() == 1) {
            final List<MountPoint> mountPointsFromVold = getMountPointsFromVold();

            for (MountPoint mountPoint : mountPointsFromVold) {
                if (!mountPoint.getStorageType().equals(MountPoint.StorageType.INTERNAL)) {
                    mountPoints.add(mountPoint);
                }
            }
        }

        return mountPoints;
    }

    /**
     * Pretty format a storage size.
     *
     * @param storageSize the the storage size in bytes to format
     *
     * @return a human representation of the storage size
     */
    @NonNull
    public static String formatStorageSize(Context context, long storageSize) {
        String storageSuffix = "b";
        float formatedStorageSize = storageSize;

        if (formatedStorageSize >= 1024) {
            storageSuffix = "kb";
            formatedStorageSize /= 1024f;

            if (formatedStorageSize >= 1024) {
                storageSuffix = "mb";
                formatedStorageSize /= 1024f;

                if (formatedStorageSize >= 1024) {
                    storageSuffix = "gb";
                    formatedStorageSize /= 1024f;
                }
            }
        }

        int stringResource = context.getResources().getIdentifier("storage_size_" + storageSuffix,
                                                                  "string",
                                                                  context.getPackageName());

        if (stringResource == 0) {
            return context.getString(
                    R.string.storage_size_kb,
                    storageSize / 1024f
            );
        }

        return context.getString(
                stringResource,
                formatedStorageSize
        );
    }

    /**
     * Retrieves a {@code List} of {@link sc.sn.sdcardreader.model.MountPoint}s from
     * {@code 'vold.fstab'} system file.
     *
     * @return a {@code List} of available {@link sc.sn.sdcardreader.model.MountPoint}s
     */
    static List<MountPoint> getMountPointsFromVold() {
        final List<MountPoint> mountPoints = new ArrayList<>();
        final File voldFstabFile = new File("/system/etc/vold.fstab");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        if (voldFstabFile.exists()) {
            try {
                fileReader = new FileReader(voldFstabFile);
                bufferedReader = new BufferedReader(fileReader);
                String line;
                MountPoint.StorageType storageType = null;

                while ((line = bufferedReader.readLine()) != null) {
                    if (TextUtils.isEmpty(line)) {
                        continue;
                    }

                    line = line.trim();

                    // parse line comment
                    if (line.startsWith("#")) {
                        if (line.contains("internal")) {
                            storageType = MountPoint.StorageType.INTERNAL;
                        }
                        else if (line.contains("external")) {
                            storageType = MountPoint.StorageType.EXTERNAL;
                        }
                        else if (line.contains("usb")) {
                            storageType = MountPoint.StorageType.USB;
                        }
                        else {
                            // storage type not found from line comment. Continue anyway
                            storageType = null;
                        }
                    }

                    // parse 'media_type' only it the storage type was not found from line comment
                    if (line.startsWith("media_type") && (storageType == null)) {
                        String[] tokens = line.split("\\s");

                        if (tokens.length == 3) {
                            if (tokens[2].contains("usb")) {
                                storageType = MountPoint.StorageType.USB;
                            }
                        }
                    }

                    // parse 'dev_mount'
                    if (line.startsWith("dev_mount") && (storageType != null)) {
                        String[] tokens = line.split("\\s");

                        if (tokens.length >= 3) {
                            File mountPath = new File(tokens[2]);
                            String storageState = Environment.MEDIA_UNMOUNTED;

                            if (mountPath.isDirectory()) {
                                if (mountPath.canWrite()) {
                                    storageState = Environment.MEDIA_MOUNTED;
                                }
                                else if (mountPath.canRead()) {
                                    storageState = Environment.MEDIA_MOUNTED_READ_ONLY;
                                }

                                mountPoints.add(new MountPoint(tokens[2],
                                                               storageState,
                                                               storageType));
                            }
                        }
                    }
                }
            }
            catch (IOException ioe) {
                Log.w(MountPointUtils.class.getName(),
                      ioe.getMessage(),
                      ioe);
            }
            finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    }
                    catch (IOException ioe) {
                        Log.w(MountPointUtils.class.getName(),
                              ioe.getMessage(),
                              ioe);
                    }
                }

                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException ioe) {
                        Log.w(MountPointUtils.class.getName(),
                              ioe.getMessage(),
                              ioe);
                    }
                }
            }
        }

        return mountPoints;
    }

    /**
     * Retrieves a {@code List} of {@link sc.sn.sdcardreader.model.MountPoint}s from {@code System}
     * environment.
     *
     * @return a {@code List} of available {@link sc.sn.sdcardreader.model.MountPoint}s
     */
    @NonNull
    static List<MountPoint> getMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        final String externalStorage = System.getenv("EXTERNAL_STORAGE");

        if (TextUtils.isEmpty(externalStorage)) {
            mountPoints.add(new MountPoint(Environment.getExternalStorageDirectory().getAbsolutePath(),
                                           Environment.getExternalStorageState(),
                                           MountPoint.StorageType.INTERNAL));
        }
        else {
            mountPoints.add(new MountPoint(externalStorage,
                                           Environment.getExternalStorageState(),
                                           MountPoint.StorageType.INTERNAL));
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

                    mountPoints.add(new MountPoint(path,
                                                   storageState,
                                                   (firstSecondaryStorage) ? MountPoint.StorageType.EXTERNAL : MountPoint.StorageType.USB));
                    firstSecondaryStorage = false;
                }
            }
        }

        return mountPoints;
    }
}