package sc.sn.sdcardreader.ui.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sc.sn.android.commons.model.MountPoint;
import sc.sn.android.commons.ui.adapter.AbstractListAdapter;
import sc.sn.android.commons.util.MountPointUtils;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.ui.widget.MountPointView;

/**
 * Default {@code Adapter} of {@link MountPoint}.
 *
 * @author S. Grimault
 */
public class MountPointAdapter
        extends AbstractListAdapter<MountPoint, MountPointAdapter.ViewHolder> {

    private final OnMountPointItemListener mOnMountPointItemListener;

    public MountPointAdapter(OnMountPointItemListener pOnMountPointItemListener) {
        this.mOnMountPointItemListener = pOnMountPointItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new ViewHolder
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.card_view_mount_point,
                                                     parent,
                                                     false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        holder.bind(getItem(position));
    }

    /**
     * Default {@code ViewHolder} used by {@link sc.sn.sdcardreader.ui.adapter.MountPointAdapter}.
     *
     * @author S. Grimault
     */
    class ViewHolder
            extends RecyclerView.ViewHolder {

        final MountPointView mMountPointView;

        ViewHolder(View itemView) {
            super(itemView);

            mMountPointView = (MountPointView) itemView.findViewById(R.id.mountPointView);
        }

        @SuppressLint("NewApi")
        void bind(final MountPoint mountPoint) {
            mMountPointView.setMountPoint(mountPoint);

            final boolean enabled = MountPointUtils.isMounted(mountPoint);

            if (enabled) {
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
                itemView.setOnClickListener(null);
            }

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

        void onMountPointSelected(MountPoint mountPoint);
    }
}
