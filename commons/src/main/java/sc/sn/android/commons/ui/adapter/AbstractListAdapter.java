package sc.sn.android.commons.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base {@code RecyclerView.Adapter} that is backed by a {@code List} of arbitrary objects.
 *
 * @author S. Grimault
 */
public abstract class AbstractListAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private final List<T> mObjects = new ArrayList<>();

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    /**
     * Remove all elements from the {@code List}.
     */
    public void clear() {
        int itemCount = getItemCount();
        mObjects.clear();
        notifyItemRangeRemoved(
                0,
                itemCount
        );
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object the object to insert into the array
     * @param index the index at which the object must be inserted
     */
    public void insert(T object,
                       int index) {
        if ((index < 0) || (index > mObjects.size())) {
            add(object);
        }
        else {
            mObjects.add(
                    index,
                    object
            );
            notifyItemInserted(index);
        }
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object the object to add at the end of the array
     */
    public void add(T object) {
        if (mObjects.add(object)) {
            notifyItemInserted(mObjects.size() - 1);
        }
        else {
            notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified {@code Collection} at the end of the array.
     *
     * @param collection the {@code Collection} to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        int itemCount = getItemCount();

        if (mObjects.addAll(collection)) {
            notifyItemRangeInserted(
                    itemCount,
                    collection.size()
            );
        }
        else {
            notifyDataSetChanged();
        }
    }

    /**
     * Get the object item associated with the specified position in the data set.
     *
     * @param index index of the object to retrieve
     *
     * @return the object at the specified position
     */
    @Nullable
    public T getItem(int index) {
        if ((index < 0) || (index > mObjects.size() - 1)) {
            return null;
        }

        return mObjects.get(index);
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object the object to remove
     */
    public void remove(T object) {
        int index = mObjects.indexOf(object);

        if (mObjects.remove(object)) {
            notifyItemRemoved(index);
        }
    }

    /**
     * Removes the specified object at the specified index in the array.
     *
     * @param index the index of the object to remove
     */
    public void remove(int index) {
        if ((index < 0) || (index > mObjects.size() - 1)) {
            return;
        }

        if (mObjects.remove(index) != null) {
            notifyItemRemoved(index);
        }
    }
}
