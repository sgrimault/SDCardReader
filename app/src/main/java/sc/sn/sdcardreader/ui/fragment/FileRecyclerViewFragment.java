package sc.sn.sdcardreader.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.io.File;
import java.util.List;

import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.loader.FileLoader;
import sc.sn.sdcardreader.ui.adapter.FileListAdapter;
import sc.sn.sdcardreader.ui.widget.recyclerview.DividerItemDecoration;

/**
 * A {@code Fragment} representing a {@code List} of {@code File}s.
 * <p/>
 * Activities containing this {@code Fragment} MUST implement the {@link sc.sn.sdcardreader.ui.fragment.FileRecyclerViewFragment.OnFileRecyclerViewFragmentListener}
 * interface.
 *
 * @author S. Grimault
 */
public class FileRecyclerViewFragment extends AbstractRecyclerViewFragment {

    private static final int LOADER_FILE = 0;

    private static final String ARG_CURRENT_FILE = "ARG_CURRENT_FILE";

    private FileListAdapter mFileListAdapter;

    private OnFileRecyclerViewFragmentListener mOnFileRecyclerViewFragmentListener;

    private FileListAdapter.OnFileItemListener mOnFileItemListener = new FileListAdapter.OnFileItemListener() {

        @Override
        public void onFileSelected(File file) {
            if (mOnFileRecyclerViewFragmentListener != null) {
                mOnFileRecyclerViewFragmentListener.onFileSelected(file);
            }
        }
    };

    private LoaderManager.LoaderCallbacks<List<File>> mFileLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<File>>() {

        @Override
        public Loader<List<File>> onCreateLoader(
                int id,
                Bundle args) {
            return new FileLoader(getActivity(), (File) args.getSerializable(ARG_CURRENT_FILE));
        }

        @Override
        public void onLoadFinished(
                Loader<List<File>> loader,
                List<File> data) {
            mFileListAdapter.clear();
            mFileListAdapter.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<List<File>> loader) {
            // nothing to do ...
        }
    };

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment {@link sc.sn.sdcardreader.ui.fragment.FileRecyclerViewFragment}.
     */
    public static FileRecyclerViewFragment newInstance(File currentPath) {
        final FileRecyclerViewFragment fragment = new FileRecyclerViewFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_FILE, currentPath);

        fragment.setArguments(args);

        return fragment;
    }

    public FileRecyclerViewFragment() {
        // required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFileListAdapter = new FileListAdapter(mOnFileItemListener);
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setEmptyText(getString(R.string.file_no_data));

        setRecyclerViewAdapter(mFileListAdapter);
        getRecyclerView().addItemDecoration(
                new DividerItemDecoration(
                        getActivity(),
                        LinearLayoutManager.VERTICAL
                )
        );
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(LOADER_FILE,
                                      getArguments(),
                                      mFileLoaderCallbacks);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mOnFileRecyclerViewFragmentListener = (OnFileRecyclerViewFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement " + OnFileRecyclerViewFragmentListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnFileRecyclerViewFragmentListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     * @author S. Grimault
     */
    public interface OnFileRecyclerViewFragmentListener {

        public void onFileSelected(File file);
    }
}
