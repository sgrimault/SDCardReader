package sc.sn.android.commons.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static android.view.LayoutInflater.from;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit tests about {@link AbstractListAdapter}.
 *
 * @author S. Grimault
 */
@RunWith(RobolectricTestRunner.class)
public class ListAdapterTest {

    private StringListAdapter stringListAdapter;

    @Mock
    private RecyclerView.AdapterDataObserver adapterDataObserver;

    @Before
    public void setUp() throws
                        Exception {
        initMocks(this);

        stringListAdapter = spy(new StringListAdapter());
        stringListAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Test
    public void testClear() {
        final List<String> values = asList("value1",
                                           "value2");

        stringListAdapter.addAll(values);
        stringListAdapter.clear();

        verify(stringListAdapter,
               times(1)).notifyItemRangeRemoved(0,
                                                values.size());
        verify(adapterDataObserver,
               times(1)).onItemRangeRemoved(0,
                                            values.size());

        assertEquals(0,
                     stringListAdapter.getItemCount());
    }

    @Test
    public void testInsert() {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final String value = "valueToInsert";

        stringListAdapter.insert(value,
                                 0);

        verify(stringListAdapter,
               times(1)).notifyItemInserted(0);
        verify(adapterDataObserver,
               times(1)).onItemRangeInserted(0,
                                             1);
        verify(stringListAdapter,
               never()).notifyDataSetChanged();

        assertEquals(1,
                     stringListAdapter.getItemCount());

        stringListAdapter.insert(value,
                                 2);
        verify(stringListAdapter,
               atLeastOnce()).add(value);

        stringListAdapter.insert(value,
                                 -1);
        verify(stringListAdapter,
               atLeastOnce()).add(value);
    }

    @Test
    public void testUpdate() throws
                             Exception {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final String value1 = "value1";
        final String value2 = "value2";
        final String value3 = "value3";

        stringListAdapter.add(value1);
        stringListAdapter.add(value2);
        stringListAdapter.add(value3);

        assertEquals(3,
                     stringListAdapter.getItemCount());
        assertEquals(value1,
                     stringListAdapter.getItem(0));
        assertEquals(value2,
                     stringListAdapter.getItem(1));
        assertEquals(value3,
                     stringListAdapter.getItem(2));

        final String value2Updated = "value2Updated";

        stringListAdapter.update(value2Updated,
                                 1);

        assertEquals(3,
                     stringListAdapter.getItemCount());
        assertEquals(value1,
                     stringListAdapter.getItem(0));
        assertEquals(value2Updated,
                     stringListAdapter.getItem(1));
        assertEquals(value3,
                     stringListAdapter.getItem(2));
    }

    @Test
    public void testAdd() {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final String value = "valueToAdd";
        stringListAdapter.add(value);

        verify(stringListAdapter,
               times(1)).notifyItemInserted(0);
        verify(adapterDataObserver,
               times(1)).onItemRangeInserted(0,
                                             1);
        verify(stringListAdapter,
               never()).notifyDataSetChanged();

        assertEquals(1,
                     stringListAdapter.getItemCount());
    }

    @Test
    public void testAddAll() {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final List<String> values = asList("value1",
                                           "value2");
        stringListAdapter.addAll(values);

        verify(stringListAdapter,
               times(1)).notifyItemRangeInserted(0,
                                                 values.size());
        verify(adapterDataObserver,
               times(1)).onItemRangeInserted(0,
                                             values.size());
        verify(stringListAdapter,
               never()).notifyDataSetChanged();

        assertEquals(2,
                     stringListAdapter.getItemCount());
    }

    @Test
    public void testGetItem() {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final String valueNotFound = stringListAdapter.getItem(0);
        assertNull(valueNotFound);

        final List<String> values = asList("value1",
                                           "value2");

        stringListAdapter.addAll(values);
        assertEquals(values.get(0),
                     stringListAdapter.getItem(0));
        assertEquals(values.get(1),
                     stringListAdapter.getItem(1));
    }

    @Test
    public void testGetItemPosition() throws
                                      Exception {
        final String value1 = "value1";
        final String value2 = "value2";
        final String value3 = "value3";

        stringListAdapter.add(value1);
        stringListAdapter.add(value2);
        stringListAdapter.add(value3);

        assertEquals(0,
                     stringListAdapter.getItemPosition(value1));
        assertEquals(1,
                     stringListAdapter.getItemPosition(value2));
        assertEquals(2,
                     stringListAdapter.getItemPosition(value3));
    }

    @Test
    public void testRemove() {
        assertEquals(0,
                     stringListAdapter.getItemCount());

        final String value = "valueToRemove";

        stringListAdapter.remove(0);
        verify(stringListAdapter,
               never()).notifyItemRemoved(0);
        verify(adapterDataObserver,
               never()).onItemRangeRemoved(0,
                                           1);

        stringListAdapter.remove(value);
        verify(stringListAdapter,
               never()).notifyItemRemoved(0);
        verify(adapterDataObserver,
               never()).onItemRangeRemoved(0,
                                           1);

        stringListAdapter.add(value);
        stringListAdapter.remove(0);
        verify(stringListAdapter,
               atLeastOnce()).notifyItemRemoved(0);
        verify(adapterDataObserver,
               atLeastOnce()).onItemRangeRemoved(0,
                                                 1);

        stringListAdapter.add(value);
        stringListAdapter.remove(value);
        verify(stringListAdapter,
               atLeastOnce()).notifyItemRemoved(0);
        verify(adapterDataObserver,
               atLeastOnce()).onItemRangeRemoved(0,
                                                 1);
    }

    @Test
    public void testFilter() throws
                             Exception {
        final String item1 = "it1";
        final String item2 = "item2";
        final String item3 = "item3";
        final String item4 = "value4";
        final String item5 = "value5";

        stringListAdapter.addAll(asList(item1,
                                        item2,
                                        item3,
                                        item4,
                                        item5));

        assertEquals(5,
                     stringListAdapter.getItemCount());

        stringListAdapter.getFilter()
                         .filter("it");

        assertEquals(3,
                     stringListAdapter.getItemCount());

        stringListAdapter.getFilter()
                         .filter("ite");

        assertEquals(2,
                     stringListAdapter.getItemCount());

        stringListAdapter.getFilter()
                         .filter(null);

        assertEquals(5,
                     stringListAdapter.getItemCount());
    }

    /**
     * Simple {@link AbstractListAdapter} using {@code String} as values.
     *
     * @author S. Grimault
     */
    class StringListAdapter
            extends AbstractListAdapter<String, StringListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new ViewHolder
            return new ViewHolder(from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,
                                                                    parent,
                                                                    false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder,
                                     int position) {
            holder.bind(getItem(position));
        }

        /**
         * Default {@code ViewHolder} used by {@link ListAdapterTest.StringListAdapter}.
         *
         * @author S. Grimault
         */
        class ViewHolder
                extends RecyclerView.ViewHolder {

            final TextView mTextView;

            ViewHolder(View itemView) {
                super(itemView);

                mTextView = (TextView) itemView.findViewById(android.R.id.text1);
            }

            void bind(final String value) {
                mTextView.setText(value);
            }
        }
    }
}