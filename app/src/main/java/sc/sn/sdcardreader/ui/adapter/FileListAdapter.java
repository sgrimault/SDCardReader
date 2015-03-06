package sc.sn.sdcardreader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

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
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        // create a new ViewHolder
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                              .inflate(R.layout.list_item_file,
                                       parent,
                                       false));
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {
        holder.bind(getItem(position));
    }

    /**
     * Default {@code ViewHolder} used by {@link FileListAdapter}.
     *
     * @author S. Grimault
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewFileName;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewFileName = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bind(final File file) {
            mTextViewFileName.setText(file.getName());

            if (file.isDirectory()) {
                mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file_folder, 0, 0, 0);
            }
            else {
                mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file, 0, 0, 0);
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
    }

    /**
     * Interface definition for a callback to be invoked when an item in the {@code RecyclerView}
     * has been clicked.
     *
     * @author S. Grimault
     */
    public interface OnFileItemListener {

        public void onFileSelected(File file);
    }
}
