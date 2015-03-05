package sc.sn.sdcardreader.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.model.MountPoint;
import sc.sn.sdcardreader.util.MountPointUtils;

/**
 * Default {@code Adapter} of {@link sc.sn.sdcardreader.model.MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointAdapter
        extends RecyclerView.Adapter<MountPointAdapter.ViewHolder> {

    private final Context mContext;
    private final OnMountPointItemListener mOnMountPointItemListener;
    private final List<MountPoint> mMountPoints = new ArrayList<>();

    public MountPointAdapter(Context pContext, OnMountPointItemListener pOnMountPointItemListener) {
        this.mContext = pContext;
        this.mOnMountPointItemListener = pOnMountPointItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        // create a new ViewHolder
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.card_view_mount_point,
                                                     parent,
                                                     false));
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {
        holder.bindMountPoint(mMountPoints.get(position));
    }

    @Override
    public int getItemCount() {
        return mMountPoints.size();
    }

    public void clear() {
        mMountPoints.clear();
        notifyDataSetChanged();
    }

    public void addAll(@NonNull final Collection<MountPoint> mountPoints) {
        mMountPoints.addAll(mountPoints);
        notifyDataSetChanged();
    }

    /**
     * Get the {@link sc.sn.sdcardreader.model.MountPoint} associated with the specified position in
     * the data set.
     *
     * @param position position of the item whose data we want within this {@link sc.sn.sdcardreader.ui.adapter.MountPointAdapter}
     *                 data set.
     *
     * @return the {@link sc.sn.sdcardreader.model.MountPoint} at the specified position
     */
    public MountPoint getItem(int position) {
        return mMountPoints.get(position);
    }

    /**
     * Default {@code ViewHolder} used by {@link sc.sn.sdcardreader.ui.adapter.MountPointAdapter}.
     *
     * @author S. Grimault
     */
    public class ViewHolder
            extends RecyclerView.ViewHolder {

        public final ImageView mImageViewStorageType;
        public final TextView mTextViewStoragePath;
        public final TextView mTextViewStorageStatus;
        public final TextView mTextViewStorageSize;
        public final TextView mTextViewStorageUsed;
        public final ProgressBar mProgressBarStorageUsed;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageViewStorageType = (ImageView) itemView.findViewById(R.id.imageViewStorageType);
            mTextViewStoragePath = (TextView) itemView.findViewById(R.id.textViewStoragePath);
            mTextViewStorageStatus = (TextView) itemView.findViewById(R.id.textViewStorageStatus);
            mTextViewStorageSize = (TextView) itemView.findViewById(R.id.textViewStorageSize);
            mTextViewStorageUsed = (TextView) itemView.findViewById(R.id.textViewStorageUsed);
            mProgressBarStorageUsed = (ProgressBar) itemView.findViewById(R.id.progressBarStorageUsed);
        }

        public void bindMountPoint(final MountPoint mountPoint) {
            mTextViewStoragePath.setText(mountPoint.getMountPath());
            mTextViewStorageStatus.setText(mountPoint.getStorageState());

            mProgressBarStorageUsed.setProgress(0);
            mProgressBarStorageUsed.setMax(100);

            mImageViewStorageType.setVisibility(View.VISIBLE);

            switch (mountPoint.getStorageType()) {
                case INTERNAL:
                    mImageViewStorageType.setImageResource(R.drawable.ic_action_hardware_phone_android);
                    break;
                case EXTERNAL:
                    mImageViewStorageType.setImageResource(R.drawable.ic_action_device_sd_storage);
                    break;
                case USB:
                    mImageViewStorageType.setImageResource(R.drawable.ic_action_device_usb);
                    break;
                default:
                    mImageViewStorageType.setVisibility(View.INVISIBLE);
                    break;
            }

            final boolean enabled = mountPoint.getStorageState().equals(Environment.MEDIA_MOUNTED) || mountPoint.getStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);

            switch (mountPoint.getStorageState()) {
                case Environment.MEDIA_MOUNTED:
                    mTextViewStorageStatus.setTextColor(mContext.getResources().getColor(R.color.mount_point_mounted));
                    break;
                case Environment.MEDIA_MOUNTED_READ_ONLY:
                    mTextViewStorageStatus.setTextColor(mContext.getResources().getColor(R.color.mount_point_mounted_ro));
                    break;
                case Environment.MEDIA_UNMOUNTED:
                    mTextViewStorageStatus.setTextColor(mContext.getResources().getColor(R.color.mount_point_unmounted));
                    break;
                default:
                    final TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(
                            R.style.AppTheme,
                            new int[] {android.R.attr.textColorSecondary}
                    );
                    int textColorPrimaryResource = typedArray.getResourceId(0, 0);

                    if (textColorPrimaryResource == 0) {
                        mTextViewStorageStatus.setTextColor(Color.BLACK);
                    }
                    else {
                        mTextViewStorageStatus.setTextColor(mContext.getResources().getColor(textColorPrimaryResource));
                    }

                    typedArray.recycle();
                    break;
            }

            if (enabled) {
                mTextViewStorageSize.setText(
                        MountPointUtils.formatStorageSize(
                                mContext,
                              mountPoint.getTotalSpace() - mountPoint.getFreeSpace()
                        ) +
                        " / " +
                        MountPointUtils.formatStorageSize(
                                mContext,
                                mountPoint.getTotalSpace()
                        )
                );
                mTextViewStorageSize.setVisibility(View.VISIBLE);

                double storageUsedFraction = (double) (mountPoint.getTotalSpace() - mountPoint.getFreeSpace()) / mountPoint.getTotalSpace();

                mTextViewStorageUsed.setText(NumberFormat.getPercentInstance().format(storageUsedFraction));
                mTextViewStorageUsed.setVisibility(View.VISIBLE);

                mProgressBarStorageUsed.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    // animate the progress bar
                    final ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBarStorageUsed,
                                                                          "progress",
                                                                          Double.valueOf(storageUsedFraction * 100).intValue());
                    animation.setStartDelay(500); // 0.5 second
                    animation.setDuration(500); // 0.5 second
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                }
                else {
                    mProgressBarStorageUsed.setProgress(Double.valueOf(storageUsedFraction * 100).intValue());
                }

                if (mOnMountPointItemListener != null) {
                    itemView.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            mOnMountPointItemListener.onMountPointSelected(mountPoint);
                        }
                    });
                }
            }
            else {
                mTextViewStorageSize.setVisibility(View.INVISIBLE);
                mTextViewStorageUsed.setVisibility(View.INVISIBLE);
                mProgressBarStorageUsed.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(null);
            }

            //itemView.setClickable(enabled);
            itemView.setEnabled(enabled);
        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in the {@code RecyclerView}
     * has been clicked.
     *
     * @author S. Grimault
     */
    public interface OnMountPointItemListener {

        public void onMountPointSelected(MountPoint mountPoint);
    }
}
