package sc.sn.android.commons.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import sc.sn.android.commons.BuildConfig;
import sc.sn.android.commons.R;
import sc.sn.android.commons.model.MountPoint;

/**
 * Class helper about {@link MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointUtils {

    private static final String TAG = MountPointUtils.class.getName();

    /**
     * Return the primary external storage as {@link MountPoint}.
     *
     * @return the primary external storage
     */
    @NonNull
    public static MountPoint getInternalStorage() {
        final String externalStorage = System.getenv("EXTERNAL_STORAGE");

        if (TextUtils.isEmpty(externalStorage)) {
            final MountPoint mountPoint = new MountPoint(Environment.getExternalStorageDirectory()
                                                                    .getAbsolutePath(),
                                                         Environment.getExternalStorageState(),
                                                         MountPoint.StorageType.INTERNAL);

            if (BuildConfig.DEBUG) {
                Log.d(TAG,
                      "internal storage from API: " + mountPoint);
            }

            return mountPoint;
        }
        else {
            final MountPoint mountPoint = new MountPoint(externalStorage,
                                                         Environment.getExternalStorageState(),
                                                         MountPoint.StorageType.INTERNAL);

            if (BuildConfig.DEBUG) {
                Log.d(TAG,
                      "internal storage from system environment: " + mountPoint);
            }

            return mountPoint;
        }
    }

    /**
     * Return the secondary external storage as {@link MountPoint} if found.
     *
     * @param storageStates a set of storage states as filter if {@link MountPoint#getStorageState()}
     *                      matches at least one
     *
     * @return the secondary external storage or {@code null} if not found
     *
     * @see #getMountPoints()
     */
    @Nullable
    public static MountPoint getExternalStorage(@Nullable String... storageStates) {
        final List<MountPoint> mountPoints = getMountPoints();
        final Iterator<MountPoint> mountPointIterator = mountPoints.iterator();
        MountPoint externalMountPoint = null;

        while (mountPointIterator.hasNext() && (externalMountPoint == null)) {
            final MountPoint mountPoint = mountPointIterator.next();
            final boolean checkStorageState = storageStates == null || storageStates.length == 0 || Arrays.asList(storageStates)
                                                                                                          .contains(mountPoint.getStorageState());
            externalMountPoint = mountPoint.getStorageType()
                                           .equals(MountPoint.StorageType.EXTERNAL) && checkStorageState ? mountPoint : null;
        }

        if (BuildConfig.DEBUG) {
            if (externalMountPoint == null) {
                Log.d(TAG,
                      "external storage not found");
            }
            else {
                Log.d(TAG,
                      "external storage found: " + externalMountPoint);
            }
        }

        return externalMountPoint;
    }

    /**
     * Retrieves a {@code List} of all available {@link MountPoint}s
     *
     * @return a {@code List} of available {@link MountPoint}s
     *
     * @see #getMountPointsFromSystemEnv()
     * @see #getMountPointsFromVold()
     * @see #getMountPointsFromProcMounts()
     */
    @NonNull
    public static List<MountPoint> getMountPoints() {
        // avoid duplicate mount points found
        final Set<MountPoint> mountPoints = new HashSet<>();

        // first: add the primary external storage
        mountPoints.add(getInternalStorage());

        // then: try to find all MountPoints from System environment
        final List<MountPoint> mountPointsFromSystemEnv = getMountPointsFromSystemEnv();
        mountPoints.addAll(mountPointsFromSystemEnv);

        // fallback: try to find all externals storage from 'vold.fstab'
        if (mountPointsFromSystemEnv.isEmpty()) {
            final List<MountPoint> mountPointsFromVold = getMountPointsFromVold();
            final List<MountPoint> filteredMountPointsFromVold = new ArrayList<>();

            // keep only all secondary externals storage found
            for (MountPoint mountPoint : mountPointsFromVold) {
                if (!mountPoint.getStorageType()
                               .equals(MountPoint.StorageType.INTERNAL)) {
                    filteredMountPointsFromVold.add(mountPoint);
                }
            }

            mountPoints.addAll(filteredMountPointsFromVold);

            // fallback: try to find all externals storage from '/proc/mounts'
            if (filteredMountPointsFromVold.isEmpty()) {
                final List<MountPoint> mountPointsFromProcMounts = getMountPointsFromProcMounts();
                mountPoints.addAll(mountPointsFromProcMounts);
            }
        }

        // apply natural ordering using TreeSet
        return new ArrayList<>(new TreeSet<>(mountPoints));
    }

    /**
     * Check if the given {@link MountPoint} is mounted or not:
     * <ul>
     * <li>{@code Environment.MEDIA_MOUNTED}</li>
     * <li>{@code Environment.MEDIA_MOUNTED_READ_ONLY}</li>
     * </ul>
     *
     * @param mountPoint the given {@link MountPoint} to check
     *
     * @return {@code true} if the gieven {@link MountPoint} is mounted, {@code false} otherwise
     */
    public static boolean isMounted(@NonNull final MountPoint mountPoint) {
        return mountPoint.getStorageState()
                         .equals(Environment.MEDIA_MOUNTED) || mountPoint.getStorageState()
                                                                         .equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    /**
     * Pretty format a storage size.
     *
     * @param context     the current context
     * @param storageSize the storage size in bytes to format
     *
     * @return a human representation of the storage size
     */
    @NonNull
    public static String formatStorageSize(Context context,
                                           long storageSize) {
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

        int stringResource = context.getResources()
                                    .getIdentifier("storage_size_" + storageSuffix,
                                                   "string",
                                                   context.getPackageName());

        if (stringResource == 0) {
            return context.getString(R.string.storage_size_kb,
                                     storageSize / 1024f);
        }

        return context.getString(stringResource,
                                 formatedStorageSize);
    }

    /**
     * Pretty format the storage status.
     *
     * @param context the current context
     * @param status  the storage status
     *
     * @return a human representation of the storage status
     */
    @NonNull
    public static String formatStorageStatus(Context context,
                                             @NonNull final String status) {
        int stringResource = context.getResources()
                                    .getIdentifier("storage_status_" + status,
                                                   "string",
                                                   context.getPackageName());

        if (stringResource == 0) {
            return context.getString(R.string.storage_status_unmounted);
        }

        return context.getString(stringResource);
    }

    /**
     * Retrieves a {@code List} of {@link MountPoint}s from {@code System}
     * environment.
     *
     * @return a {@code List} of available {@link MountPoint}s
     */
    @NonNull
    private static List<MountPoint> getMountPointsFromSystemEnv() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        String secondaryStorage = System.getenv("SECONDARY_STORAGE");

        if (TextUtils.isEmpty(secondaryStorage)) {
            secondaryStorage = System.getenv("EXTERNAL_SDCARD_STORAGE");
        }

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

                    final MountPoint mountPoint = new MountPoint(path,
                                                                 storageState,
                                                                 (firstSecondaryStorage) ? MountPoint.StorageType.EXTERNAL : MountPoint.StorageType.USB);

                    if (BuildConfig.DEBUG) {
                        Log.d(TAG,
                              "mount point found from system environment: " + mountPoint);
                    }

                    mountPoints.add(mountPoint);
                    firstSecondaryStorage = false;
                }
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG,
                  "mount points found from system environment: " + mountPoints.size());
        }

        return mountPoints;
    }

    /**
     * Retrieves a {@code List} of {@link MountPoint}s from
     * {@code 'vold.fstab'} system file.
     *
     * @return a {@code List} of available {@link MountPoint}s
     */
    @NonNull
    private static List<MountPoint> getMountPointsFromVold() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        try {
            final Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (TextUtils.isEmpty(line)) {
                    continue;
                }

                line = line.trim();

                MountPoint.StorageType storageType = null;

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

                            final MountPoint mountPoint = new MountPoint(tokens[2],
                                                                         storageState,
                                                                         storageType);

                            if (BuildConfig.DEBUG) {
                                Log.d(TAG,
                                      "mount point found from 'vold.fstab': " + mountPoint);
                            }

                            mountPoints.add(mountPoint);
                        }
                    }
                }
            }

            scanner.close();
        }
        catch (FileNotFoundException fnfe) {
            Log.w(TAG,
                  fnfe.getMessage());
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG,
                  "mount points found from 'vold.fstab': " + mountPoints.size());
        }

        return mountPoints;
    }

    /**
     * Retrieves a {@code List} of {@link MountPoint}s from
     * {@code '/proc/mounts'} system file.
     *
     * @return a {@code List} of available {@link MountPoint}s
     */
    @NonNull
    private static List<MountPoint> getMountPointsFromProcMounts() {
        final List<MountPoint> mountPoints = new ArrayList<>();

        try {
            final Scanner scanner = new Scanner(new File("/proc/mounts"));

            while (scanner.hasNext()) {
                final String line = scanner.nextLine();

                if (line.startsWith("/dev/block/vold") || line.startsWith("/dev/fuse")) {
                    final String[] tokens = line.split("\\s");

                    if (tokens.length >= 2) {
                        final File mountPath = new File(tokens[1]);
                        String storageState = Environment.MEDIA_UNMOUNTED;

                        if (mountPath.isDirectory()) {
                            if (mountPath.canWrite()) {
                                storageState = Environment.MEDIA_MOUNTED;
                            }
                            else if (mountPath.canRead()) {
                                storageState = Environment.MEDIA_MOUNTED_READ_ONLY;
                            }

                            final MountPoint mountPoint = new MountPoint(tokens[1],
                                                                         storageState,
                                                                         MountPoint.StorageType.EXTERNAL);

                            if (BuildConfig.DEBUG) {
                                Log.d(TAG,
                                      "mount point found from '/proc/mounts': " + mountPoint);
                            }

                            mountPoints.add(mountPoint);
                        }
                    }
                }
            }

            scanner.close();
        }
        catch (FileNotFoundException fnfe) {
            Log.w(TAG,
                  fnfe.getMessage());
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG,
                  "mount points found from '/proc/mounts': " + mountPoints.size());
        }

        return mountPoints;
    }
}
