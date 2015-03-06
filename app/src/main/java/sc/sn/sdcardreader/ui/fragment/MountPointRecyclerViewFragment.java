package sc.sn.sdcardreader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.List;

import sc.sn.sdcardreader.loader.MountPointLoader;
import sc.sn.sdcardreader.model.MountPoint;
import sc.sn.sdcardreader.ui.adapter.MountPointAdapter;
import sc.sn.sdcardreader.ui.widget.recyclerview.SlideInOutBottomItemAnimator;

/**
 * A {@code Fragment} representing a {@code List} of {@link sc.sn.sdcardreader.model.MountPoint}s.
 * <p/>
 * Activities containing this {@code Fragment} MUST implement the {@link sc.sn.sdcardreader.ui.fragment.MountPointRecyclerViewFragment.OnMountPointRecyclerViewFragmentListener}
 * interface.
 *
 * @author S. Grimault
 */
public class MountPointRecyclerViewFragment extends AbstractRecyclerViewFragment {

    private static final int LOADER_MOUNT_POINT = 0;

    private MountPointAdapter mMountPointAdapter;

    private OnMountPointRecyclerViewFragmentListener mOnMountPointRecyclerViewFragmentListener;

    private MountPointAdapter.OnMountPointItemListener mOnMountPointItemListener = new MountPointAdapter.OnMountPointItemListener() {

        @Override
        public void onMountPointSelected(MountPoint mountPoint) {
            if (mOnMountPointRecyclerViewFragmentListener != null) {
                mOnMountPointRecyclerViewFragmentListener.onMountPointSelected(mountPoint);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<List<MountPoint>> mMountPointLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<MountPoint>>() {

        @Override
        public Loader<List<MountPoint>> onCreateLoader(
                int id,
                Bundle args) {
            return new MountPointLoader(getActivity());
        }

        @Override
        public void onLoadFinished(
                Loader<List<MountPoint>> loader,
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

        mMountPointAdapter = new MountPointAdapter(getActivity(), mOnMountPointItemListener);
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerViewAdapter(mMountPointAdapter);
        getRecyclerView().setItemAnimator(new SlideInOutBottomItemAnimator(getRecyclerView()));
        getRecyclerView().getItemAnimator().setAddDuration(500);
        getRecyclerView().getItemAnimator().setChangeDuration(500);
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(LOADER_MOUNT_POINT,
                                      getArguments(),
                                      mMountPointLoaderCallbacks);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnMountPointRecyclerViewFragmentListener = (OnMountPointRecyclerViewFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnMountPointRecyclerViewFragmentListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnMountPointRecyclerViewFragmentListener = null;
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

        public void onMountPointSelected(MountPoint mountPoint);
    }
}
