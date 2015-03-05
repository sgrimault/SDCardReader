package sc.sn.sdcardreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.io.File;
import java.util.List;

import sc.sn.sdcardreader.loader.FileLoader;
import sc.sn.sdcardreader.ui.adapter.FileAdapter;
import sc.sn.sdcardreader.ui.widget.recyclerview.DividerItemDecoration;

/**
 * A {@code Fragment} representing a list of {@code File}s.
 *
 * @author S. Grimault
 */
public class FileRecyclerViewFragment extends AbstractRecyclerViewFragment {

    private static final int LOADER_FILE = 0;

    private static final String ARG_CURRENT_FILE = "ARG_CURRENT_FILE";

    private FileAdapter mFileAdapter;

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
            mFileAdapter.clear();
            mFileAdapter.addAll(data);
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

        mFileAdapter = new FileAdapter();
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setRecyclerViewAdapter(mFileAdapter);
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
}
