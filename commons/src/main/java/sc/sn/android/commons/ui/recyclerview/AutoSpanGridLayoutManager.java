package sc.sn.android.commons.ui.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Custom {@code GridLayoutManager} to set automatically the number of span.
 *
 * @author S. Grimault
 */
public class AutoSpanGridLayoutManager
        extends GridLayoutManager {

    private int mSpanWidth;

    public AutoSpanGridLayoutManager(Context context,
                                     int spanWidthResource) {
        super(context,
              DEFAULT_SPAN_COUNT);

        mSpanWidth = context.getResources()
                            .getDimensionPixelSize(spanWidthResource);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler,
                          RecyclerView.State state,
                          int widthSpec,
                          int heightSpec) {
        super.onMeasure(recycler,
                        state,
                        widthSpec,
                        heightSpec);

        int width = View.MeasureSpec.getSize(widthSpec);

        if (width != 0) {
            int spans = width / mSpanWidth;

            if (spans > 0) {
                setSpanCount(spans);
            }
        }
    }
}
