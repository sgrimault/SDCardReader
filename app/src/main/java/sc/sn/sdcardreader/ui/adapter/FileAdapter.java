package sc.sn.sdcardreader.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import sc.sn.sdcardreader.R;

/**
 * Default {@code Adapter} of {@code File}.
 *
 * @author S. Grimault
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private final List<File> mFiles = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        // create a new ViewHolder
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                              .inflate(android.R.layout.simple_list_item_1,
                                       parent,
                                       false));
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position) {
        holder.bindFile(mFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public void clear() {
        mFiles.clear();
        notifyDataSetChanged();
    }

    public void addAll(@NonNull final Collection<File> files) {
        mFiles.addAll(files);
        notifyDataSetChanged();
    }

    /**
     * Default {@code ViewHolder} used by {@link sc.sn.sdcardreader.ui.adapter.FileAdapter}.
     *
     * @author S. Grimault
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextViewFileName;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewFileName = (TextView) itemView.findViewById(android.R.id.text1);
        }

        public void bindFile(final File file) {
            mTextViewFileName.setText(file.getName());

            if (file.isDirectory()) {
                mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file_folder, 0, 0, 0);
            }
            else {
                mTextViewFileName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_file, 0, 0, 0);
            }
        }
    }
}
