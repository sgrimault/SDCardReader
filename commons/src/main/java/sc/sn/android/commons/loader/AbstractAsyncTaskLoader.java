package sc.sn.android.commons.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Base {@code Loader}.
 *
 * S. Grimault
 */
public abstract class AbstractAsyncTaskLoader<D>
        extends AsyncTaskLoader<D> {

    D result;

    protected AbstractAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (result == null) {
            forceLoad();
        }
        else {
            deliverResult(result);
        }
    }

    @Override
    public void deliverResult(D data) {
        result = data;
        super.deliverResult(data);
    }
}
