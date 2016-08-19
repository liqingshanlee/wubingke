package tech.honc.android.apps.soldier.ui.fragment;

import android.os.Bundle;
import android.view.View;
import com.smartydroid.android.starter.kit.app.StarterKeysFragment;
import com.smartydroid.android.starter.kit.model.ErrorModel;
import com.smartydroid.android.starter.kit.model.entity.Entity;
import tech.honc.android.apps.soldier.R;

public abstract class BaseNotDividerKeysFragment<E extends Entity> extends StarterKeysFragment<E> {

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override public void errorNotFound(ErrorModel errorModel) {
    super.errorNotFound(errorModel);
    getContentPresenter().buildEmptyImageView(R.mipmap.ic_default_empty)
        .buildEmptyTitle("什么都没有");
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
}
