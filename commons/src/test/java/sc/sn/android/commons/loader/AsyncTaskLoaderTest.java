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
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Unit tests about {@link AbstractAsyncTaskLoader}.
 *
 * @author S. Grimault
 */
@RunWith(RobolectricTestRunner.class)
public class AsyncTaskLoaderTest {

    private FragmentActivity activity;
    private DummyAsyncTaskLoader dummyAsyncTaskLoader;

    @Mock
    private LoaderManager.LoaderCallbacks<String> dummyAsyncTaskLoaderCallbacks;

    @Before
    public void setUp() throws
                        Exception {
        initMocks(this);

        activity = buildActivity(FragmentActivity.class).create()
                                                        .start()
                                                        .resume()
                                                        .get();

        dummyAsyncTaskLoader = spy(new DummyAsyncTaskLoader(application));
        doReturn(dummyAsyncTaskLoader).when(dummyAsyncTaskLoaderCallbacks)
                                      .onCreateLoader(anyInt(),
                                                      (Bundle) any());
    }

    @Test
    public void testInitLoader() {
        Assert.assertNull(dummyAsyncTaskLoader.result);

        Loader<String> loader = activity.getSupportLoaderManager()
                                        .initLoader(0,
                                                    null,
                                                    dummyAsyncTaskLoaderCallbacks);

        assertNotNull(loader);

        verify(dummyAsyncTaskLoader,
               times(1)).onStartLoading();
        verify(dummyAsyncTaskLoader,
               times(1)).forceLoad();
    }

    /**
     * Dummy {@link AbstractAsyncTaskLoader} for testing purpose only.
     *
     * @author S. Grimault
     */
    class DummyAsyncTaskLoader
            extends AbstractAsyncTaskLoader<String> {

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