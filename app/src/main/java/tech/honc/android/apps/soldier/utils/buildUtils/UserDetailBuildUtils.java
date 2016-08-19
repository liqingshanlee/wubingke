package tech.honc.android.apps.soldier.utils.buildUtils;

import java.util.ArrayList;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.User;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang
 * on 2016/4/18.
 */
public class UserDetailBuildUtils {

  public static final int DYNAMIC_TAG = 100;

  public static ArrayList<SettingItems> BuildMaleDatas(User user) {
    ArrayList<SettingItems> settingItemses = new ArrayList<>();
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_RELATIVE_CELL)
        .data(
            (user.accountDetail != null ) ? user.accountDetail : null)
        .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_PHOTO_CELL)
        .imageList(user.albums != null && user.albums.size() != 0 ? user.albums : null)
        .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_DYNAMIC_CELL)
        .data(user.feed != null ? user.feed: null)
        .type(DYNAMIC_TAG)
        .build());
    settingItemses.add(
        new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_SIGNATURE_CELL)
            .content("个性签名")
            .icon(R.mipmap.ic_detail_sigture)
            .value(user.signature)
            .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_SERVICE_CELL)
        .data(user.army)
        .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_INFO_CELL)
        .data(user)
        .build());
    settingItemses.add(
        new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_SIGNATURE_CELL)
            .content("其他")
            .icon(R.mipmap.ic_detail_other)
            .value(user.signature)
            .build());
    return settingItemses;
  }

  public static ArrayList<SettingItems> BuildFemaleDatas(User user) {
    ArrayList<SettingItems> settingItemses = new ArrayList<>();
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_RELATIVE_CELL)
        .data(
            user.accountDetail != null? user.accountDetail
                : null)
        .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_PHOTO_CELL)
        .imageList(user.albums != null && user.albums.size() != 0 ? user.albums : null)
        .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_DYNAMIC_CELL)
        .data(user.feed != null ? user.feed: null)
        .build());
    settingItemses.add(
        new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_SIGNATURE_CELL)
            .content("个性签名")
            .icon(R.mipmap.ic_detail_sigture)
            .value(user.signature)
            .build());
    settingItemses.add(new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_INFO_CELL)
        .data(user)
        .build());
    settingItemses.add(
        new SettingItems.Builder().itemViewType(SettingItems.VIEW_TYPE_SIGNATURE_CELL)
            .content("其他")
            .icon(R.mipmap.ic_detail_other)
            .value(user.signature)
            .build());
    return settingItemses;
  }
}
