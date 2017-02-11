package sc.sn.sdcardreader.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import sc.sn.android.commons.ui.fragment.RecyclerViewFragment;
import sc.sn.android.commons.ui.recyclerview.AutoSpanGridLayoutManager;
import sc.sn.android.commons.ui.recyclerview.DividerItemDecoration;
import sc.sn.sdcardreader.R;
import sc.sn.sdcardreader.content.UserSharedPreferences;
import sc.sn.sdcardreader.loader.FileLoader;
import sc.sn.sdcardreader.ui.adapter.FileListAdapter;

/**
 * A {@code Fragment} representing a {@code List} of {@code File}s.
 * <p/>
 * Activities containing this {@code Fragment} MUST implement the {@link FileRecyclerViewFragment.OnFileRecyclerViewFragmentListener}
 * interface.
 *
 * @author S. Grimault
 */
public class FileRecyclerViewFragment
        extends RecyclerViewFragment {

    private static final int LOADER_FILE = 0;

    private static final String ARG_CURRENT_FILE = "ARG_CURRENT_FILE";

    private FileListAdapter mFileListAdapter;

    private OnFileRecyclerViewFragmentListener mOnFileRecyclerViewFragmentListener;

    private final FileListAdapter.OnFileItemListener mOnFileItemListener = new FileListAdapter.OnFileItemListener() {

        @Override
        public void onFileSelected(File file) {
            if (mOnFileRecyclerViewFragmentListener != null) {
                mOnFileRecyclerViewFragmentListener.onFileSelected(file);
            }
        }
    };

    private final LoaderManager.LoaderCallbacks<List<File>> mFileLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<File>>() {

        @Override
        public Loader<List<File>> onCreateLoader(int id,
                                                 Bundle args) {
            return new FileLoader(getActivity(),
                                  (File) args.getSerializable(ARG_CURRENT_FILE));
        }

        @Override
        public void onLoadFinished(Loader<List<File>> loader,
                                   List<File> data) {
            mFileListAdapter.clear();
            mFileListAdapter.addAll(data);
        }

        @Override
        public void onLoaderReset(Loader<List<File>> loader) {
            // nothing to do ...
        }
    };

    private DividerItemDecoration mDividerItemDecoration;

    private UserSharedPreferences mUserSharedPreferences;

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment {@link sc.sn.sdcardreader.ui.fragment.FileRecyclerViewFragment}.
     */
    public static FileRecyclerViewFragment newInstance(File currentPath) {
        final FileRecyclerViewFragment fragment = new FileRecyclerViewFragment();
        final Bundle args = new Bundle();
        args.putSerializable(ARG_CURRENT_FILE,
                             currentPath);

        fragment.setArguments(args);

        return fragment;
    }

    public FileRecyclerViewFragment() {
        // required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mUserSharedPreferences = new UserSharedPreferences(getActivity());

        mFileListAdapter = new FileListAdapter(mOnFileItemListener);
        mDividerItemDecoration = new DividerItemDecoration(getActivity(),
                                                           LinearLayoutManager.VERTICAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view_file,
                                container,
                                false);
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,
                            savedInstanceState);

        setEmptyText(getString(R.string.file_no_data));
        updateListMode();
        setRecyclerViewAdapter(mFileListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,
                                    MenuInflater inflater) {
        super.onCreateOptionsMenu(menu,
                                  inflater);

        inflater.inflate(R.menu.menu_item_view_mode,
                         menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        final UserSharedPreferences.RecyclerViewMode recyclerViewMode = mUserSharedPreferences.getRecyclerViewMode();

        int stringResource = getResources().getIdentifier("view_" + recyclerViewMode.name()
                                                                                    .toLowerCase(),
                                                          "string",
                                                          getActivity().getPackageName());
        int drawableResource = getResources().getIdentifier("ic_action_view_" + recyclerViewMode.name()
                                                                                                .toLowerCase(),
                                                            "drawable",
                                                            getActivity().getPackageName());

        if (stringResource == 0) {
            stringResource = R.string.view_list;
        }

        if (drawableResource == 0) {
            drawableResource = R.drawable.ic_action_view_list;
        }

        menu.findItem(R.id.menu_item_view_mode)
            .setTitle(getString(R.string.menu_item_view_mode,
                                getString(stringResource)))
            .setIcon(drawableResource);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_view_mode:
                mUserSharedPreferences.setRecyclerViewMode((mUserSharedPreferences.getRecyclerViewMode() == UserSharedPreferences.RecyclerViewMode.LIST) ? UserSharedPreferences.RecyclerViewMode.GRID : UserSharedPreferences.RecyclerViewMode.LIST);
                ActivityCompat.invalidateOptionsMenu(getActivity());
                updateListMode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getLoaderManager().initLoader(LOADER_FILE,
                                      getArguments(),
                                      mFileLoaderCallbacks);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnFileRecyclerViewFragmentListener = (OnFileRecyclerViewFragmentListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + OnFileRecyclerViewFragmentListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mOnFileRecyclerViewFragmentListener = null;
    }

    private void updateListMode() {
        switch (mUserSharedPreferences.getRecyclerViewMode()) {
            case LIST:
                getRecyclerView().setLayoutManager(new GridLayoutManager(getActivity(),
                                                                         1));
                getRecyclerView().addItemDecoration(mDividerItemDecoration);
                break;
            case GRID:
                getRecyclerView().setLayoutManager(new AutoSpanGridLayoutManager(getActivity(),
                                                                                 R.dimen.list_item_file_grid_width));
                getRecyclerView().removeItemDecoration(mDividerItemDecoration);
                break;
        }
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

        void onFileSelected(File file);
    }
}
