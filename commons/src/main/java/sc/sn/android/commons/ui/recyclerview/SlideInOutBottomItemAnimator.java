package sc.sn.android.commons.ui.recyclerview;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @see <a href="https://github.com/gabrielemariotti/RecyclerViewItemAnimators">https://github.com/gabrielemariotti/RecyclerViewItemAnimators</a>
 */
public class SlideInOutBottomItemAnimator
        extends AbstractItemAnimator {

    private float mDeltaY;

    public SlideInOutBottomItemAnimator(RecyclerView recyclerView) {
        super(recyclerView);
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        ViewCompat.animate(view)
                  .cancel();
        ViewCompat.animate(view)
                  .setDuration(getRemoveDuration())
                  .translationY(+mDeltaY)
                  .setListener(
                          new VpaListenerAdapter() {

                              @Override
                              public void onAnimationEnd(View view) {
                                  ViewCompat.setTranslationY(
                                          view,
                                          +mDeltaY
                                  );
                                  dispatchRemoveFinished(holder);
                                  mRemoveAnimations.remove(holder);
                                  dispatchFinishedWhenDone();
                              }
                          }
                  )
                  .start();

        mRemoveAnimations.add(holder);
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        ViewCompat.animate(view)
                  .cancel();
        ViewCompat.animate(view)
                  .translationY(0)
                  .setDuration(getAddDuration())
                  .setListener(
                          new VpaListenerAdapter() {

                              @Override
                              public void onAnimationCancel(View view) {
                                  ViewCompat.setTranslationY(
                                          view,
                                          0
                                  );
                              }

                              @Override
                              public void onAnimationEnd(View view) {
                                  dispatchAddFinished(holder);
                                  mAddAnimations.remove(holder);
                                  dispatchFinishedWhenDone();
                              }
                          }
                  )
                  .start();

        mAddAnimations.add(holder);
    }

    @Override
    protected void preAnimateAdd(RecyclerView.ViewHolder holder) {
        retrieveItemPosition(holder);
        ViewCompat.setTranslationY(
                holder.itemView,
                +mDeltaY
        );
    }

    private void retrieveItemPosition(final RecyclerView.ViewHolder holder) {
        float originalY = mRecyclerView.getLayoutManager()
                                       .getDecoratedTop(holder.itemView);
        mDeltaY = mRecyclerView.getHeight() - originalY;
    }
}
