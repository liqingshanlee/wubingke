package tech.honc.android.apps.soldier.utils.imagebrowser;

import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.imagepipeline.image.ImageInfo;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.PhotoService;
import tech.honc.android.apps.soldier.model.Image;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.ui.activity.BaseActivity;
import tech.honc.android.apps.soldier.utils.imagebrowser.photoview.PhotoDraweeView;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;

/**
 * Created by MrJiang on 2016/5/4.
 */
public class PhotoBDActivity extends BaseActivity {

  public static String IMAGE_LIST = "list_image";
  public static String IMAGE_POSITION = "position_image";
  public static final String FLAG_URL_STRING = "flag_url_string";

  @Bind(R.id.view_pager) MultiTouchViewPager mViewPager;
  private List<Image> mList;
  private List<String> mUrlList;
  private int mCurrentPosition;
  private PhotoService mPhotoService;
  private TouchImageAdapter mTouchImageAdapter;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_photo_browser);
    mPhotoService = ApiService.createPhotoService();
    mTouchImageAdapter = new TouchImageAdapter();
    setUpActionBar();
    getData();
    android.support.v7.app.ActionBar actionBar = getSupportActionBar();
  }

  private void setUpActionBar() {
    ActionBar actionBar = getSupportActionBar();
    assert actionBar != null;
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(true);
  }

  //@Override public boolean onCreateOptionsMenu(Menu menu) {
  //  MenuInflater inflater = getMenuInflater();
  //  inflater.inflate(R.menu.menu_delete, menu);
  //  return super.onCreateOptionsMenu(menu);
  //}

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        break;
      //case R.id.menu_delete:
      //  //写一个方法回调，删除当前image图片
      //  deleteImage(mList.get(mCurrentPosition - 1).id);
      //  break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void getData() {
    Intent intent = getIntent();
    mList = intent.getParcelableArrayListExtra(IMAGE_LIST);
    if (mList == null || mList.size() == 0) {
      mUrlList = intent.getStringArrayListExtra(FLAG_URL_STRING);
    }
    mCurrentPosition = intent.getIntExtra(IMAGE_POSITION, 0);
    mViewPager.setAdapter(mTouchImageAdapter);
    mViewPager.setCurrentItem(mCurrentPosition);
  }

  public void deleteImage(Integer id) {
    Call<Status> call = mPhotoService.deletePhoto(id);
    call.enqueue(new Callback<Status>() {
      @Override public void onResponse(Call<Status> call, Response<Status> response) {
        if (response.isSuccessful()) {
          ViewGroup vg = (ViewGroup) getWindow().getDecorView();
          mTouchImageAdapter.destroyItem(vg, mViewPager.getCurrentItem(), mViewPager.getTag());
          mTouchImageAdapter.notifyDataSetChanged();
          SnackBarUtil.showText(PhotoBDActivity.this, "删除成功");
        } else {
          SnackBarUtil.showText(PhotoBDActivity.this, "删除失败");
        }
      }

      @Override public void onFailure(Call<Status> call, Throwable t) {
        SnackBarUtil.showText(PhotoBDActivity.this, "删除失败");
      }
    });
  }

  class TouchImageAdapter extends PagerAdapter {
    @Override public int getCount() {
      if (mList != null) {
        return mList.size();
      }
      if (mUrlList != null) {
        return mUrlList.size();
      }
      return 0;
    }

    @Override public View instantiateItem(ViewGroup container, int position) {
      final PhotoDraweeView photoDraweeView = new PhotoDraweeView(container.getContext());

      PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder();
      controllerBuilder.setTapToRetryEnabled(true);
      //controllerBuilder.setOldController(photoDraweeView.getController());
      String key = "tvRecord" + position;
      photoDraweeView.setTag(key);
      if (mList != null) {
        mCurrentPosition = position;
        controllerBuilder.setUri(mList.get(position).uri());
      } else {
        mCurrentPosition = position;
        controllerBuilder.setUri(Uri.parse(mUrlList.get(position)));
      }

      GenericDraweeHierarchy hierarchy = photoDraweeView.getHierarchy();
      //
      //hierarchy.setRetryImage(getDrawable(R.drawable.icon_retry),
      //    ScalingUtils.ScaleType.CENTER_CROP);
      //hierarchy.setProgressBarImage(getDrawable(R.drawable.icon_progress_bar),
      //    ScalingUtils.ScaleType.CENTER_INSIDE);
      //hierarchy.setFailureImage(getDrawable(R.drawable.ic_placeholder_banner),
      //    ScalingUtils.ScaleType.CENTER_CROP);
      //hierarchy.setRoundingParams();
      hierarchy.setFadeDuration(500);

      hierarchy.setProgressBarImage(new ProgressBarDrawable());
      photoDraweeView.setHierarchy(hierarchy);
      controllerBuilder.setOldController(photoDraweeView.getController());
      controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {

        @Override
        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
          super.onFinalImageSet(id, imageInfo, animatable);
          if (imageInfo == null) {
            return;
          }
          photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
        }
      });
      photoDraweeView.setController(controllerBuilder.build());

      try {
        container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return photoDraweeView;
    }

    @Override public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }
}
