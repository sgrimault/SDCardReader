package sc.sn.sdcardreader.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.model.MountPoint;
import sc.sn.sdcardreader.ui.adapter.MountPointArrayAdapter;
import sc.sn.sdcardreader.util.FileUtils;

/**
 * A fragment representing a list of {@link sc.sn.sdcardreader.model.MountPoint}s.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMountPointListFragmentListener}
 * interface.
 *
 * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
 */
public class MountPointListFragment extends ListFragment {

    private OnMountPointListFragmentListener mListener;
    private MountPointArrayAdapter mAdapter;

    public static MountPointListFragment newInstance() {
        final MountPointListFragment fragment = new MountPointListFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MountPointListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MountPointArrayAdapter(getActivity());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(
                view,
                savedInstanceState
        );

        setListAdapter(mAdapter);
        setEmptyText(getString(R.string.no_data));
    }

    @Override
    public void onResume() {
        super.onResume();

        mAdapter.clear();

        for (MountPoint mountPoint : FileUtils.getMountPointsFromSystemEnv()) {
            mAdapter.add(mountPoint);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnMountPointListFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + OnMountPointListFragmentListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (mListener != null) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onMountPointSelected(mAdapter.getItem(position));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * @author <a href="mailto:sebastien.grimault@makina-corpus.com">S. Grimault</a>
     */
    public interface OnMountPointListFragmentListener {
        public void onMountPointSelected(MountPoint mountPoint);
    }
}
