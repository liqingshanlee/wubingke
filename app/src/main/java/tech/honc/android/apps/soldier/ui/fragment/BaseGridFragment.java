package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.paginate.recycler.LoadingListItemSpanLookup;
import com.smartydroid.android.starter.kit.StarterKit;
import com.smartydroid.android.starter.kit.app.StarterKeysFragment;
import com.smartydroid.android.starter.kit.model.ErrorModel;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import com.smartydroid.android.starter.kit.utilities.ViewUtils;
import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.ui.decorations.ItemOffsetDecoration;

/**
 * Created by MrJiang on 2016/4/19.
 */
public abstract class BaseGridFragment<E extends Entity> extends StarterKeysFragment<E> {

  @Override protected int getFragmentLayout() {
    return R.layout.fragment_grids;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public Paginate buildPaginate() {
    return Paginate.with(getRecyclerView(), this)
        .setLoadingTriggerThreshold(StarterKit.getLoadingTriggerThreshold())
        .addLoadingListItem(true)
        .setLoadingListItemCreator(new CustomLoadingListItemCreator())
        .setLoadingListItemSpanSizeLookup(new LoadingListItemSpanLookup() {
          @Override public int getSpanSize() {
            return 2;
          }
        })
        .build();
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ItemOffsetDecoration itemDecoration =
        new ItemOffsetDecoration(getContext(), R.dimen.view_margin_4);
    getRecyclerView().addItemDecoration(itemDecoration);
  }

  @Override public RecyclerView.LayoutManager buildLayoutManager() {
    return new GridLayoutManager(getContext(), 2);
  }

  @Override public void errorNotFound(ErrorModel errorModel) {
    super.errorNotFound(errorModel);
    getContentPresenter().buildEmptyImageView(R.mipmap.ic_default_empty).buildEmptyTitle("什么都没有");
  }

  @Override protected void buildEmpty(ErrorModel errorModel) {
    getContentPresenter().buildEmptyImageView(R.mipmap.ic_default_empty).buildEmptyTitle("什么都没有");
  }

  @Override public void eNetUnReach(Throwable t, ErrorModel errorModel) {
    getContentPresenter().buildErrorImageView(R.mipmap.ic_defalut_not_wifi)
        .buildErrorTitle("网络连接失败")
        .buildErrorSubtitle("请检查你的手机是否联网")
        .shouldDisplayErrorImageView(true)
        .shouldDisplayErrorTitle(true);
  }

  @Override public void errorUnauthorized(ErrorModel errorModel) {
    getContentPresenter().buildErrorImageView(R.mipmap.ic_default_loading)
        .buildErrorTitle("你没有权限,请先登陆")
        .shouldDisplayErrorImageView(true)
        .shouldDisplayErrorSubtitle(false);
  }


  private class CustomLoadingListItemCreator
      implements LoadingListItemCreator, View.OnClickListener {
    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(parent.getContext());
      View view = inflater.inflate(com.smartydroid.android.starter.kit.R.layout.list_item_loading, parent, false);
      return new VH(view);
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      VH vh = (VH) holder;

      if (getPagePaginator().hasError()) {
        ViewUtils.setGone(vh.progressBar, true);
        ViewUtils.setGone(vh.textLoading, false);
        vh.textLoading.setText(com.smartydroid.android.starter.kit.R.string.starter_loadingmore_failure);
        vh.itemView.setOnClickListener(this);
      } else {
        ViewUtils.setGone(vh.progressBar, false);
        ViewUtils.setGone(vh.textLoading, true);
      }

      // This is how you can make full span if you are using StaggeredGridLayoutManager
      if (getRecyclerView().getLayoutManager() instanceof StaggeredGridLayoutManager) {
        StaggeredGridLayoutManager.LayoutParams params =
            (StaggeredGridLayoutManager.LayoutParams) vh.itemView.getLayoutParams();
        params.setFullSpan(true);
      }
    }

    @Override public void onClick(View v) {
      onLoadMore();
    }
  }

  static class VH extends RecyclerView.ViewHolder {
    TextView textLoading;
    CircularProgressBar progressBar;

    public VH(View itemView) {
      super(itemView);
      textLoading = ViewUtils.getView(itemView, com.smartydroid.android.starter.kit.R.id.text_loading);
      progressBar = ViewUtils.getView(itemView, com.smartydroid.android.starter.kit.R.id.circular_progress_bar);
    }
  }
}
