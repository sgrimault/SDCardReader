package sc.sn.sdcardreader.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

import sc.sn.android.commons.model.MountPoint;
import sc.sn.android.commons.ui.fragment.RecyclerViewFragment;
import sc.sn.android.commons.util.PermissionUtils;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.loader.MountPointLoader;
import sc.sn.sdcardreader.ui.adapter.MountPointAdapter;

/**
 * A {@code Fragment} representing a {@code List} of {@link MountPoint}s.
 * <p/>
 * Activities containing this {@code Fragment} MUST implement the
 * {@link MountPointRecyclerViewFragment.OnMountPointRecyclerViewFragmentListener} interface.
 *
 * @author S. Grimault
 */
public class MountPointRecyclerViewFragment
        extends RecyclerViewFragment {

    private static final int LOADER_MOUNT_POINT = 0;
    private static final int REQUEST_EXTERNAL_STORAGE = 0;

    private MountPointAdapter mMountPointAdapter;

    private OnMountPointRecyclerViewFragmentListener mOnMountPointRecyclerViewFragmentListener;

    private final MountPointAdapter.OnMountPointItemListener mOnMountPointItemListener = new MountPointAdapter.OnMountPointItemListener() {

        @Override
        public void onMountPointSelected(MountPoint mountPoint) {
            if (mOnMountPointRecyclerViewFragmentListener != null) {
                mOnMountPointRecyclerViewFragmentListener.onMountPointSelected(mountPoint);
            }
        }
    };

    private final LoaderManager.LoaderCallbacks<List<MountPoint>> mMountPointLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<MountPoint>>() {

        @Override
        public Loader<List<MountPoint>> onCreateLoader(int id,
                                                       Bundle args) {
            return new MountPointLoader(getActivity());
        }

        @Override
        public void onLoadFinished(Loader<List<MountPoint>> loader,
                                   List<MountPoint> data) {

            mMountPointAdapter.clear();
            mMountPointAdapter.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<List<MountPoint>> loader) {
            // nothing to do ...
        }
    };

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment {@link sc.sn.sdcardreader.ui.fragment.MountPointRecyclerViewFragment}.
     */
    public static MountPointRecyclerViewFragment newInstance() {
        final MountPointRecyclerViewFragment fragment = new MountPointRecyclerViewFragment();
        final Bundle args = new Bundle();

        fragment.setArguments(args);

        return fragment;
    }

    public MountPointRecyclerViewFragment() {
        // required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMountPointAdapter = new MountPointAdapter(mOnMountPointItemListener);
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,
                            savedInstanceState);

        setRecyclerViewAdapter(mMountPointAdapter);
        getRecyclerView().getItemAnimator()
                         .setAddDuration(500);
        getRecyclerView().getItemAnimator()
                         .setChangeDuration(500);

        PermissionUtils.checkSelfPermissions(getContext(),
                                             new PermissionUtils.OnCheckSelfPermissionListener() {
                                                 @Override
                                                 public void onPermissionsGranted() {
                                                     loadMountPoints();
                                                 }

                                                 @Override
                                                 public void onRequestPermissions(@NonNull String... permissions) {
                                                     PermissionUtils.requestPermissions(MountPointRecyclerViewFragment.this,
                                                                                        mLayout,
                                                                                        R.string.snackbar_permission_external_storage_rationale,
                                                                                        REQUEST_EXTERNAL_STORAGE,
                                                                                        permissions);

                                                 }
                                             },
                                             Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (PermissionUtils.checkPermissions(grantResults)) {
                    Snackbar.make(mLayout,
                                  R.string.snackbar_permission_external_storage_available,
                                  Snackbar.LENGTH_SHORT)
                            .show();

                    loadMountPoints();
                }
                else {
                    Snackbar.make(mLayout,
                                  R.string.snackbar_permissions_not_granted,
                                  Snackbar.LENGTH_SHORT)
                            .show();

                    mMountPointAdapter.notifyDataSetChanged();
                }

                break;
            default:
                super.onRequestPermissionsResult(requestCode,
                                                 permissions,
                                                 grantResults);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnMountPointRecyclerViewFragmentListener = (OnMountPointRecyclerViewFragmentListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + OnMountPointRecyclerViewFragmentListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnMountPointRecyclerViewFragmentListener = null;
    }

    private void loadMountPoints() {
        getLoaderManager().initLoader(LOADER_MOUNT_POINT,
                                      getArguments(),
                                      mMountPointLoaderCallbacks);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * @author S. Grimault
     */
    public interface OnMountPointRecyclerViewFragmentListener {

        void onMountPointSelected(MountPoint mountPoint);
    }
}
