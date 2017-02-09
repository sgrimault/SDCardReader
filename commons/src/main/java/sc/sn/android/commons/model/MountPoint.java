package sc.sn.android.commons.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Describes a mount point storage.
 *
 * @author S. Grimault
 */
public class MountPoint
        implements Parcelable {

    private final String mountPath;
    private final String storageState;
    private final StorageType storageType;

    public MountPoint(@NonNull final String mountPath,
                      @NonNull final String storageState,
                      @NonNull final StorageType storageType) {
        this.mountPath = mountPath;
        this.storageState = storageState;
        this.storageType = storageType;
    }

    private MountPoint(Parcel source) {
        this.mountPath = source.readString();
        this.storageState = source.readString();
        this.storageType = (StorageType) source.readSerializable();
    }

    @NonNull
    public String getMountPath() {
        return mountPath;
    }

    @NonNull
    public String getStorageState() {
        return storageState;
    }

    @NonNull
    public StorageType getStorageType() {
        return storageType;
    }

    /**
     * Returns the total size in bytes of the partition containing this path.
     *
     * @return the total size in bytes of the partition containing this path or 0 if this path does not exist
     *
     * @see File#getTotalSpace()
     */
    public long getTotalSpace() {
        return new File(mountPath).getTotalSpace();
    }

    /**
     * Returns the number of free bytes on the partition containing this path.
     *
     * @return the number of free bytes on the partition containing this path or 0 if this path does not exist
     *
     * @see File#getFreeSpace()
     */
    public long getFreeSpace() {
        return new File(mountPath).getFreeSpace();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest,
                              int flags) {
        dest.writeString(mountPath);
        dest.writeString(storageState);
        dest.writeSerializable(storageType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MountPoint that = (MountPoint) o;

        return mountPath.equals(that.mountPath);
    }

    @Override
    public int hashCode() {
        return mountPath.hashCode();
    }

    @Override
    public String toString() {
        return "MountPoint{" +
                "mountPath='" + mountPath + '\'' +
                ", storageState='" + storageState + '\'' +
                ", storageType=" + storageType +
                '}';
    }

    /**
     * Describes a storage type.
     *
     * @author S. Grimault
     */
    public enum StorageType {
        /**
         * Internal storage.
         */
        INTERNAL,

        /**
         * External storage.
         */
        EXTERNAL,

        /**
         * USB storage.
         */
        USB
    }

    public static final Creator<MountPoint> CREATOR = new Creator<MountPoint>() {

        @Override
        public MountPoint createFromParcel(Parcel source) {
            return new MountPoint(source);
        }

        @Override
        public MountPoint[] newArray(int size) {
            return new MountPoint[size];
        }
    };
}
