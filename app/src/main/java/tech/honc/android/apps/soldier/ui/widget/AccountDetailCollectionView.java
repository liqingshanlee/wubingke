package tech.honc.android.apps.soldier.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.smartydroid.android.starter.kit.utilities.ScreenUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import java.util.List;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Image;

public class AccountDetailCollectionView extends RecyclerView {
  private static final int spanCount = 3;
  private SimpleAdapter mAdapter;
  private int mItemSize;

  public AccountDetailCollectionView(Context context) {
    this(context, null);
  }

  public AccountDetailCollectionView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public AccountDetailCollectionView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  private void initialize() {
    setNestedScrollingEnabled(false);
    int screenWidth = StarterKitApp.appInfo().screenWidth;
    int spanSize = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_margin_4);
    int margin = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_margin_8);
    int paddingSize = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_margin_40);

    mItemSize = (screenWidth - (spanCount - 1) * spanSize - paddingSize) / spanCount;
    setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.TRANSPARENT)
            .size(spanSize / 2)
            .build());
    mAdapter = new SimpleAdapter();
    setAdapter(mAdapter);
  }

  // TODO: 4/17/2016  
  public void setData(ArrayList<Image> data) {
    mAdapter.setData(data);
  }

  static class SimpleViewHolder extends ViewHolder {
    public Image image;
    protected int itemSize;
    @Bind(R.id.image_feed_thumbnail) SimpleDraweeView mThumbnailView;
    @Bind(R.id.text_frame) TextView mTextFrame;

    private SimpleViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false));
      ButterKnife.bind(this, itemView);
    }

    static SimpleViewHolder create(Context context, ViewGroup parent) {
      return new SimpleViewHolder(context, parent);
    }

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    public void bind(Image image) {
      this.image = image;
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbnailView.getLayoutParams();
      //设置图片尺寸
      params.width = itemSize;
      params.height = itemSize;
      mThumbnailView.setLayoutParams(params);
      ResizeOptions options = new ResizeOptions(itemSize, itemSize);
      displayImage(image, mThumbnailView, options);
    }

    private void displayImage(Image image, SimpleDraweeView simpleDraweeView,
        ResizeOptions options) {
      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(
          image.uri(ScreenUtils.dp2px(80), ScreenUtils.dp2px(80)))
          .setResizeOptions(options)
          .build();
      PipelineDraweeController controller =
          (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
              .setOldController(simpleDraweeView.getController())
              .setImageRequest(request)
              .build();
      simpleDraweeView.setController(controller);
    }
  }

  /**
   * RecyclerView适配器
   */
  private class SimpleAdapter extends Adapter<ViewHolder> {
    private ArrayList<Image> mData;//图片的容器

    public SimpleAdapter() {
      mData = new ArrayList<>();
    }

    public void setData(ArrayList<Image> data) {
      mData.clear();
      mData.addAll(data);
      notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return SimpleViewHolder.create(getContext(), parent);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
      super.onBindViewHolder(holder, position, payloads);
    }

    @Override public void onBindViewHolder(ViewHolder holder,
        @SuppressLint("RecyclerView") final int position) {
      final Image image = mData.get(position);
      final SimpleViewHolder viewHolder = (SimpleViewHolder) holder;
      viewHolder.setItemSize(mItemSize);
      if (position < 9) {
        viewHolder.bind(image);
      } else {
        viewHolder.mTextFrame.setVisibility(GONE);
        viewHolder.mTextFrame.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
      }
      viewHolder.mThumbnailView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          Navigator.startPhotoBrowser((Activity) getContext(), mData, position);
        }
      });
    }

    @Override public int getItemCount() {
      return mData.size();
    }
  }

  @Override public boolean onTouchEvent(MotionEvent e) {
    return false;
  }
}
