package sc.sn.android.commons.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;

import sc.sn.android.commons.CustomRobolectricRunner;

/**
 * Unit tests about {@link sc.sn.android.commons.loader.AbstractAsyncTaskLoader}.
 *
 * @author S. Grimault
 */
@RunWith(CustomRobolectricRunner.class)
public class AsyncTaskLoaderTest {

    private FragmentActivity activity;
    private DummyAsyncTaskLoader dummyAsyncTaskLoader;

    @Mock
    private LoaderManager.LoaderCallbacks<String> dummyAsyncTaskLoaderCallbacks;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        activity = Robolectric.buildActivity(FragmentActivity.class)
                              .create()
                              .start()
                              .resume()
                              .get();

        dummyAsyncTaskLoader = Mockito.spy(new DummyAsyncTaskLoader(Robolectric.application));
        Mockito.doReturn(dummyAsyncTaskLoader).when(dummyAsyncTaskLoaderCallbacks).onCreateLoader(Mockito.anyInt(), (Bundle) Mockito.anyObject());
    }
    @Test
    public void testInitLoader() {
        Assert.assertNull(dummyAsyncTaskLoader.result);

        Loader<String> loader = activity.getSupportLoaderManager().initLoader(
                0,
                null,
                dummyAsyncTaskLoaderCallbacks
        );

        Assert.assertNotNull(loader);

        Mockito.verify(dummyAsyncTaskLoader, Mockito.times(1)).onStartLoading();
        Mockito.verify(dummyAsyncTaskLoader, Mockito.times(1)).forceLoad();
    }

    /**
     * Dummy {@link sc.sn.android.commons.loader.AbstractAsyncTaskLoader} for testing purpose only.
     *
     * @author S. Grimault
     */
    class DummyAsyncTaskLoader extends AbstractAsyncTaskLoader<String> {

        final static String RESULT = "result";

        DummyAsyncTaskLoader(Context context) {
            super(context);
        }

        @Override
        public String loadInBackground() {
            return RESULT;
        }
    }
}