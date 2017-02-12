package sc.sn.sdcardreader.ui.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.NumberFormat;

import sc.sn.android.commons.model.MountPoint;
import sc.sn.android.commons.util.DeviceUtils;
import sc.sn.android.commons.util.MountPointUtils;
import sc.sn.sdcardreader.R;

/**
 * Custom {@code View} about {@link MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointView
        extends RelativeLayout {

    private final ImageView mImageViewStorageType;
    private final TextView mTextViewStoragePath;
    private final TextView mTextViewStorageStatus;
    private final TextView mTextViewStorageSize;
    private final TextView mTextViewStorageUsed;
    private final ProgressBar mProgressBarStorageUsed;

    public MountPointView(Context context) {
        this(context,
             null);
    }

    public MountPointView(Context context,
                          AttributeSet attrs) {
        super(context,
              attrs);

        inflate(context,
                R.layout.view_mount_point,
                this);

        mImageViewStorageType = (ImageView) findViewById(R.id.imageViewStorageType);
        mTextViewStoragePath = (TextView) findViewById(R.id.textViewStoragePath);
        mTextViewStorageStatus = (TextView) findViewById(R.id.textViewStorageStatus);
        mTextViewStorageSize = (TextView) findViewById(R.id.textViewStorageSize);
        mTextViewStorageUsed = (TextView) findViewById(R.id.textViewStorageUsed);
        mProgressBarStorageUsed = (ProgressBar) findViewById(R.id.progressBarStorageUsed);

        if (isInEditMode()) {
            setMountPoint(new MountPoint(Environment.getExternalStorageDirectory()
                                                    .getPath(),
                                         Environment.MEDIA_MOUNTED,
                                         MountPoint.StorageType.INTERNAL));
        }
    }

    public void setMountPoint(@NonNull final MountPoint mountPoint) {
        mTextViewStoragePath.setText(mountPoint.getMountPath());
        mTextViewStorageStatus.setText(MountPointUtils.formatStorageStatus(getContext(),
                                                                           mountPoint.getStorageState()));

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

        final boolean mounted = MountPointUtils.isMounted(mountPoint);

        switch (mountPoint.getStorageState()) {
            case Environment.MEDIA_MOUNTED:
                mTextViewStorageStatus.setTextColor(ContextCompat.getColor(getContext(),
                                                                           R.color.mount_point_mounted));
                break;
            case Environment.MEDIA_MOUNTED_READ_ONLY:
                mTextViewStorageStatus.setTextColor(ContextCompat.getColor(getContext(),
                                                                           R.color.mount_point_mounted_ro));
                break;
            case Environment.MEDIA_UNMOUNTED:
                mTextViewStorageStatus.setTextColor(ContextCompat.getColor(getContext(),
                                                                           R.color.mount_point_unmounted));
                break;
            default:
                final TypedArray typedArray = getContext().getTheme()
                                                          .obtainStyledAttributes(R.style.AppTheme,
                                                                                  new int[] {android.R.attr.textColorSecondary});
                int textColorPrimaryResource = typedArray.getResourceId(0,
                                                                        0);

                if (textColorPrimaryResource == 0) {
                    mTextViewStorageStatus.setTextColor(Color.BLACK);
                }
                else {
                    mTextViewStorageStatus.setTextColor(ContextCompat.getColor(getContext(),
                                                                               textColorPrimaryResource));
                }

                typedArray.recycle();
                break;
        }

        if (mounted) {
            mTextViewStorageSize.setText(MountPointUtils.formatStorageSize(getContext(),
                                                                           mountPoint.getTotalSpace() - mountPoint.getFreeSpace()) +
                                                 " / " +
                                                 MountPointUtils.formatStorageSize(getContext(),
                                                                                   mountPoint.getTotalSpace()));
            mTextViewStorageSize.setVisibility(View.VISIBLE);

            double storageUsedFraction = (mountPoint.getTotalSpace() > 0) ? (double) (mountPoint.getTotalSpace() - mountPoint.getFreeSpace()) / mountPoint.getTotalSpace() : 0;

            mTextViewStorageUsed.setText(NumberFormat.getPercentInstance()
                                                     .format(storageUsedFraction));
            mTextViewStorageUsed.setVisibility(View.VISIBLE);

            mProgressBarStorageUsed.setVisibility(View.VISIBLE);

            if (DeviceUtils.isPostHoneycomb()) {
                // animate the progress bar
                final ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBarStorageUsed,
                                                                      "progress",
                                                                      Double.valueOf(storageUsedFraction * 100)
                                                                            .intValue());
                animation.setStartDelay(500); // 0.5 second
                animation.setDuration(500); // 0.5 second
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }
            else {
                mProgressBarStorageUsed.setProgress(Double.valueOf(storageUsedFraction * 100)
                                                          .intValue());
            }
        }
        else {
            mTextViewStorageSize.setVisibility(View.INVISIBLE);
            mTextViewStorageUsed.setVisibility(View.INVISIBLE);
            mProgressBarStorageUsed.setVisibility(View.INVISIBLE);
        }
    }
    /*
    @SuppressLint("NewApi")
    public void setDetailsVisibility(final int visibility) {
        if (DeviceUtils.isPostHoneycomb()) {
            final Animator.AnimatorListener animatorListenerForTextViewStorageSize = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mTextViewStorageSize.setVisibility(visibility);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };

            if (mTextViewStorageSize.getVisibility() == visibility) {

            }
            else {
                mTextViewStorageSize.animate()
                                    .alpha((visibility == VISIBLE) ? 1f : 0f)
                                    .setDuration(getResources().getInteger(android.R.integer.config_longAnimTime))
                                    .setListener(animatorListenerForTextViewStorageSize);
            }
        }
        else {

        }
    }
    */
}
