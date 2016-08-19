package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.AccountDetail;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/17/2016.
 * 关注标题
 */
public class RelativeViewHolder extends BaseViewHolder {
  @Bind(R.id.fans) TextView mFans;
  @Bind(R.id.detail_attentions) TextView mDetailAttentions;
  @Bind(R.id.detail_helper) TextView mDetailHelper;
  @Bind(R.id.detail_help) TextView mDetailHelp;
  private AccountDetail mAccountDetail;

  public RelativeViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_relation_info, parent, false));
    try {
      ButterKnife.bind(this, itemView);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    AccountDetail detail = (AccountDetail) settingItem.mData;
    mAccountDetail = detail;
    if (detail != null) {
      mFans.setText(detail.followers != null ? detail.followers + "" : "");
      mDetailAttentions.setText(detail.followings != null ? detail.followings + "" : "");
      mDetailHelper.setText(detail.helps != null ? detail.helps + "" : "");
      mDetailHelp.setText(detail.forHelps != null ? detail.forHelps + "" : "");
    }
  }

  @OnClick({
      R.id.contain_fans, R.id.container_attentions, R.id.container_helper, R.id.container_help
  }) public void onClick(View v) {
    switch (v.getId()) {
      case R.id.contain_fans:
        if (mAccountDetail != null && mAccountDetail.accountId != null) {
          Navigator.startOthersFansActivity(getActivity(), mAccountDetail.accountId);
        }
        break;
      case R.id.container_attentions:
        if (mAccountDetail != null && mAccountDetail.accountId != null) {
          Navigator.startOtherAttentionsActivity(getActivity(), mAccountDetail.accountId);
        }
        break;
      case R.id.container_helper:
        if (mAccountDetail != null && mAccountDetail.accountId != null) {
          Navigator.startOtherHelperActivity(getActivity(), mAccountDetail.accountId);
        }
        break;
      case R.id.container_help:
        if (mAccountDetail != null && mAccountDetail.accountId != null) {
          Navigator.startOtherHelpActivity(getActivity(), mAccountDetail.accountId);
        }
        break;
    }
  }
}
