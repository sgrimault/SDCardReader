package sc.sn.sdcardreader.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Describes a mount point storage.
 *
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class MountPoint implements Parcelable {

    private final String mountPath;
    private final String storageState;
    private final StorageType storageType;

    public MountPoint(
            String mountPath,
            String storageState,
            StorageType storageType) {
        this.mountPath = mountPath;
        this.storageState = storageState;
        this.storageType = storageType;
    }

    public MountPoint(Parcel source) {
        this.mountPath = source.readString();
        this.storageState = source.readString();
        this.storageType = (StorageType) source.readSerializable();
    }

    public String getMountPath() {
        return mountPath;
    }

    public String getStorageState() {
        return storageState;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(
            Parcel dest,
            int flags) {
        dest.writeString(mountPath);
        dest.writeString(storageState);
        dest.writeSerializable(storageType);
    }

    /**
     * Describes a storage type.
     *
     * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
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
