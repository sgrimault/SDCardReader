package sc.sn.sdcardreader.content;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

/**
 * Unit tests about {@link sc.sn.sdcardreader.content.UserSharedPreferences} class.
 *
 * @author S. Grimault
 */
@RunWith(RobolectricTestRunner.class)
public class UserSharedPreferencesTest {

    UserSharedPreferences userSharedPreferences;

    @Before
    public void setUp() {
        userSharedPreferences = new UserSharedPreferences(RuntimeEnvironment.application);

        Assert.assertNotNull(userSharedPreferences);
    }

    @Test
    public void testGetRecyclerViewMode() {
        final UserSharedPreferences.RecyclerViewMode recyclerViewMode = userSharedPreferences.getRecyclerViewMode();

        Assert.assertNotNull(recyclerViewMode);
        Assert.assertEquals(UserSharedPreferences.RecyclerViewMode.LIST,
                            recyclerViewMode);
    }

    @Test
    public void testSetRecyclerViewMode() {
        userSharedPreferences.setRecyclerViewMode(UserSharedPreferences.RecyclerViewMode.GRID);

        final UserSharedPreferences.RecyclerViewMode recyclerViewMode = userSharedPreferences.getRecyclerViewMode();

        Assert.assertNotNull(recyclerViewMode);
        Assert.assertEquals(UserSharedPreferences.RecyclerViewMode.GRID,
                            recyclerViewMode);
    }
}