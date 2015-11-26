package sc.sn.android.commons.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests about {@link sc.sn.android.commons.ui.adapter.AbstractListAdapter}.
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
        MockitoAnnotations.initMocks(this);

        stringListAdapter = Mockito.spy(new StringListAdapter());
        stringListAdapter.registerAdapterDataObserver(adapterDataObserver);
    }

    @Test
    public void testClear() {
        final List<String> values = Arrays.asList("value1",
                                                  "value2");

        stringListAdapter.addAll(values);
        stringListAdapter.clear();

        Mockito.verify(stringListAdapter,
                       Mockito.times(1))
               .notifyItemRangeRemoved(0,
                                       values.size());
        Mockito.verify(adapterDataObserver,
                       Mockito.times(1))
               .onItemRangeRemoved(0,
                                   values.size());

        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());
    }

    @Test
    public void testInsert() {
        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());

        final String value = "valueToInsert";

        stringListAdapter.insert(value,
                                 0);

        Mockito.verify(stringListAdapter,
                       Mockito.times(1))
               .notifyItemInserted(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.times(1))
               .onItemRangeInserted(0,
                                    1);
        Mockito.verify(stringListAdapter,
                       Mockito.never())
               .notifyDataSetChanged();

        Assert.assertEquals(1,
                            stringListAdapter.getItemCount());

        stringListAdapter.insert(value,
                                 2);
        Mockito.verify(stringListAdapter,
                       Mockito.atLeastOnce())
               .add(value);

        stringListAdapter.insert(value,
                                 -1);
        Mockito.verify(stringListAdapter,
                       Mockito.atLeastOnce())
               .add(value);
    }

    @Test
    public void testAdd() {
        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());

        final String value = "valueToAdd";
        stringListAdapter.add(value);

        Mockito.verify(stringListAdapter,
                       Mockito.times(1))
               .notifyItemInserted(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.times(1))
               .onItemRangeInserted(0,
                                    1);
        Mockito.verify(stringListAdapter,
                       Mockito.never())
               .notifyDataSetChanged();

        Assert.assertEquals(1,
                            stringListAdapter.getItemCount());
    }

    @Test
    public void testAddAll() {
        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());

        final List<String> values = Arrays.asList("value1",
                                                  "value2");
        stringListAdapter.addAll(values);

        Mockito.verify(stringListAdapter,
                       Mockito.times(1))
               .notifyItemRangeInserted(0,
                                        values.size());
        Mockito.verify(adapterDataObserver,
                       Mockito.times(1))
               .onItemRangeInserted(0,
                                    values.size());
        Mockito.verify(stringListAdapter,
                       Mockito.never())
               .notifyDataSetChanged();

        Assert.assertEquals(2,
                            stringListAdapter.getItemCount());
    }

    @Test
    public void testGetItem() {
        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());

        final String valueNotFound = stringListAdapter.getItem(0);
        Assert.assertNull(valueNotFound);

        final List<String> values = Arrays.asList("value1",
                                                  "value2");

        stringListAdapter.addAll(values);
        Assert.assertEquals(values.get(0),
                            stringListAdapter.getItem(0));
        Assert.assertEquals(values.get(1),
                            stringListAdapter.getItem(1));
    }

    @Test
    public void testRemove() {
        Assert.assertEquals(0,
                            stringListAdapter.getItemCount());

        final String value = "valueToRemove";

        stringListAdapter.remove(0);
        Mockito.verify(stringListAdapter,
                       Mockito.never())
               .notifyItemRemoved(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.never())
               .onItemRangeRemoved(0,
                                   1);

        stringListAdapter.remove(value);
        Mockito.verify(stringListAdapter,
                       Mockito.never())
               .notifyItemRemoved(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.never())
               .onItemRangeRemoved(0,
                                   1);

        stringListAdapter.add(value);
        stringListAdapter.remove(0);
        Mockito.verify(stringListAdapter,
                       Mockito.atLeastOnce())
               .notifyItemRemoved(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.atLeastOnce())
               .onItemRangeRemoved(0,
                                   1);

        stringListAdapter.add(value);
        stringListAdapter.remove(value);
        Mockito.verify(stringListAdapter,
                       Mockito.atLeastOnce())
               .notifyItemRemoved(0);
        Mockito.verify(adapterDataObserver,
                       Mockito.atLeastOnce())
               .onItemRangeRemoved(0,
                                   1);
    }

    /**
     * Simple {@link sc.sn.android.commons.ui.adapter.AbstractListAdapter} using {@code String} as values.
     *
     * @author S. Grimault
     */
    class StringListAdapter
            extends AbstractListAdapter<String, StringListAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new ViewHolder
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                                                .inflate(android.R.layout.simple_list_item_1,
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

            public void bind(final String value) {
                mTextView.setText(value);
            }
        }
    }
}