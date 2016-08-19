package tech.honc.android.apps.soldier.ui.viewholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.ui.widget.UserPhotoGalleryView;
import tech.honc.android.apps.soldier.utils.settings.SettingItems;

/**
 * Created by MrJiang on 4/17/2016.
 * 我的相册
 */
public class PhotoCellViewHolder extends BaseViewHolder {
  @Bind(R.id.photo_collections) UserPhotoGalleryView mPhotoCollections;

  public PhotoCellViewHolder(Context context, ViewGroup parent) {
    super(context,
        LayoutInflater.from(context).inflate(R.layout.list_item_muti_photo, parent, false));
    ButterKnife.bind(this, itemView);
    mPhotoCollections.setOnClickListener(this);
  }

  @Override protected void bindTo(SettingItems settingItem) {
    super.bindTo(settingItem);
    final ArrayList<Image> images = settingItem.mImageList;
    if (images != null && images.size() != 0) {
      mPhotoCollections.setVisibility(View.VISIBLE);
      mPhotoCollections.setData(images);
    } else {
      mPhotoCollections.setVisibility(View.GONE);
    }
  }
}
