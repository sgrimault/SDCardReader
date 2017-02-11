package sc.sn.sdcardreader.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import sc.sn.android.commons.ui.adapter.AbstractListAdapter;
import sc.sn.sdcardreader.R;

/**
 * Default {@code Adapter} of {@code File}.
 *
 * @author S. Grimault
 */
public class FileListAdapter
        extends AbstractListAdapter<File, FileListAdapter.ViewHolder> {

    private final OnFileItemListener mOnFileItemListener;

    public FileListAdapter(OnFileItemListener pOnFileItemListener) {
        this.mOnFileItemListener = pOnFileItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new ViewHolder
        return new ViewHolder(parent,
                              LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.list_item_file,
                                                     parent,
                                                     false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,
                                 int position) {
        holder.bind(getItem(position));
    }

    /**
     * Default {@code ViewHolder} used by {@link FileListAdapter}.
     *
     * @author S. Grimault
     */
    class ViewHolder
            extends RecyclerView.ViewHolder {

        private final RecyclerView mRecyclerView;
        private final TextView mTextViewFileName;

        ViewHolder(ViewGroup parent,
                   View itemView) {
            super(itemView);

            mRecyclerView = (RecyclerView) parent;
            mTextViewFileName = (TextView) itemView.findViewById(android.R.id.text1);
        }

        void bind(final File file) {
            mTextViewFileName.setText(file.getName());
            int spanCount = getSpanCountFromLayoutManager();

            if (file.isDirectory()) {
                if (spanCount > 1) {
                    mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(0,
                                                                              R.drawable.ic_action_file_folder,
                                                                              0,
                                                                              0);
                    mTextViewFileName.setGravity(Gravity.CENTER);
                }
                else {
                    mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file_folder,
                                                                              0,
                                                                              0,
                                                                              0);
                    mTextViewFileName.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
            else {
                if (spanCount > 1) {
                    mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(0,
                                                                              R.drawable.ic_action_file,
                                                                              0,
                                                                              0);
                    mTextViewFileName.setGravity(Gravity.CENTER);
                }
                else {
                    mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file,
                                                                              0,
                                                                              0,
                                                                              0);
                    mTextViewFileName.setGravity(Gravity.CENTER_VERTICAL);
                }
            }

            if (mOnFileItemListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mOnFileItemListener.onFileSelected(file);
                    }
                });
            }
        }

        private int getSpanCountFromLayoutManager() {
            return (mRecyclerView.getLayoutManager() instanceof GridLayoutManager) ? ((GridLayoutManager) mRecyclerView.getLayoutManager()).getSpanCount() : GridLayoutManager.DEFAULT_SPAN_COUNT;
        }
    }

    /**
     * Interface definition for a callback to be invoked when an item in the {@code RecyclerView}
     * has been clicked.
     *
     * @author S. Grimault
     */
    public interface OnFileItemListener {

        void onFileSelected(File file);
    }
}
