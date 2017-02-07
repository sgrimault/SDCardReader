package sc.sn.sdcardreader.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import sc.sn.sdcardreader.content.UserSharedPreferences.RecyclerViewMode;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.RuntimeEnvironment.application;

/**
 * Unit tests about {@link UserSharedPreferences} class.
 *
 * @author S. Grimault
 */
@RunWith(RobolectricTestRunner.class)
public class UserSharedPreferencesTest {

    private UserSharedPreferences userSharedPreferences;

    @Before
    public void setUp() {
        userSharedPreferences = new UserSharedPreferences(application);

        assertNotNull(userSharedPreferences);
    }

    @Test
    public void testGetRecyclerViewMode() {
        final RecyclerViewMode recyclerViewMode = userSharedPreferences.getRecyclerViewMode();

        assertNotNull(recyclerViewMode);
        assertEquals(RecyclerViewMode.LIST,
                     recyclerViewMode);
    }

    @Test
    public void testSetRecyclerViewMode() {
        userSharedPreferences.setRecyclerViewMode(RecyclerViewMode.GRID);

        final RecyclerViewMode recyclerViewMode = userSharedPreferences.getRecyclerViewMode();

        assertNotNull(recyclerViewMode);
        assertEquals(RecyclerViewMode.GRID,
                     recyclerViewMode);
    }
}