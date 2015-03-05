package sc.sn.sdcardreader.ui.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Default implementation of {@code RecyclerView.OnItemTouchListener} to intercept touch events and
 * detect when an item of the {@code RecyclerView} has been clicked.
 *
 * @author S. Grimault
 */
public class RecyclerViewItemClickListener
        implements RecyclerView.OnItemTouchListener {

    private GestureDetector mGestureDetector;
    private final OnItemClickListener mOnItemClickListener;

    public RecyclerViewItemClickListener(
            Context context,
            OnItemClickListener listener) {
        mOnItemClickListener = listener;
        mGestureDetector = new GestureDetector(context,
                                               new GestureDetector.SimpleOnGestureListener() {

                                                   @Override
                                                   public boolean onSingleTapUp(MotionEvent e) {
                                                       return true;
                                                   }
                                               });
    }

    @Override
    public boolean onInterceptTouchEvent(
            RecyclerView rv,
            MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(),
                                               e.getY());

        if ((childView != null) && (childView.isClickable()) && (mOnItemClickListener != null) && mGestureDetector.onTouchEvent(e)) {
            mOnItemClickListener.onItemClick(childView,
                                             rv.getChildPosition(childView));
            return true;
        }

        return false;
    }

    @Override
    public void onTouchEvent(
            RecyclerView rv,
            MotionEvent e) {
        // nothing to do ...
    }

    /**
     * Interface definition for a callback to be invoked when an item in the {@code RecyclerView}
     * has been clicked.
     *
     * @author S. Grimault
     */
    public interface OnItemClickListener {

        /**
         * Called when an item in the {@code RecyclerView} has been clicked.
         *
         * @param view     The view within the {@code RecyclerView} that was clicked
         * @param position The position of the view in the {@code RecyclerView}.
         */
        void onItemClick(
                View view,
                int position);
    }
}
