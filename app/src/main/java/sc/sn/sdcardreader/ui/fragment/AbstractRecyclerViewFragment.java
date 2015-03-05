package sc.sn.sdcardreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import sc.sn.sdcardreader.R;

/**
 * Base {@code Fragment} about {@code RecyclerView}.
 *
 * @author S. Grimault
 */
public abstract class AbstractRecyclerViewFragment extends Fragment {

    private View mProgressView;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        // inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = view.findViewById(android.R.id.progress);

        mRecyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager as default layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerViewAdapter(@Nullable RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);

        if (adapter != null) {
            // start out with a progress indicator
            setListShown(false);

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    super.onChanged();

                    setListShown(true);
                }
            });
        }
    }

    private void setListShown(boolean shown) {
        setListShown(shown,
                     true);
    }

    private void setListShown(
            boolean shown,
            boolean animate) {
        if ((mRecyclerView.getVisibility() == View.VISIBLE) == shown) {
            return;
        }

        if (shown) {
            if (animate) {
                mProgressView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }

            mProgressView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        else {
            if (animate) {
                mProgressView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mRecyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }

            mProgressView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
