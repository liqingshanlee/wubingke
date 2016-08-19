package tech.honc.android.apps.soldier.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.smartydroid.android.starter.kit.app.StarterKitApp;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import mediapicker.MediaItem;
import support.ui.adapters.EasyViewHolder;
import support.ui.adapters.debounced.DebouncedOnClickListener;
import tech.honc.android.apps.soldier.R;

/**
 * Created by Administrator on 2016/4/18.
 */
public class PublishFeedCollectionView extends RecyclerView {
  private static final int spanCount = 4;
  private SimpleAdapter mAdapter;
  private int mItemSize;

  public PublishFeedCollectionView(Context context) {
    this(context, null);
  }

  public PublishFeedCollectionView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PublishFeedCollectionView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    initialize();
  }

  private void initialize() {
    setNestedScrollingEnabled(false);
    int screenWidth = StarterKitApp.appInfo().screenWidth;
    int spanSize = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.span_size);
    int margin =
        StarterKitApp.appResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
    mItemSize = (screenWidth - (spanCount - 1) * spanSize - 2 * margin) / spanCount;
    setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.TRANSPARENT)
            .size(spanSize)
            .build());
    mAdapter = new SimpleAdapter();
    setAdapter(mAdapter);
  }

  public void addAll(ArrayList<MediaItem> data) {
    mAdapter.addAll(data);
  }

  public void setOnClickListener(final EasyViewHolder.OnItemClickListener listener) {
    mAdapter.setOnClickListener(listener);
  }

  static abstract class MyViewHolder extends ViewHolder implements View.OnClickListener
  {

    protected int itemSize;
    private EasyViewHolder.OnItemClickListener itemClickListener;

    public MyViewHolder(View itemView) {
      super(itemView);
      bindListeners();
    }

    public void setItemClickListener(EasyViewHolder.OnItemClickListener itemClickListener) {
      this.itemClickListener = itemClickListener;
    }

    private void bindListeners() {
      itemView.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
      if (itemClickListener == null) return;
      itemClickListener.onItemClick(getAdapterPosition(), v);
    }

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    public void bind(MediaItem mediaItem) {
    }
  }

  static class AddViewHolder extends MyViewHolder {
    @Bind(R.id.image_publish_add) ImageView addImageView;

    private AddViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_add, parent, false));
      ButterKnife.bind(this, itemView);
    }

    static AddViewHolder create(Context context, ViewGroup parent) {
      return new AddViewHolder(context, parent);
    }

    public void bind(MediaItem mediaItem) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) addImageView.getLayoutParams();
      params.width = itemSize;
      params.height = itemSize;
      addImageView.setLayoutParams(params);
    }
  }

  static class SimpleViewHolder extends MyViewHolder {
    public MediaItem mediaItem;
    @Bind(R.id.image_feed_thumbnail) SimpleDraweeView mThumbnailView;

    private SimpleViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_image, parent, false));
      ButterKnife.bind(this, itemView);
    }

    static SimpleViewHolder create(Context context, ViewGroup parent) {
      return new SimpleViewHolder(context, parent);
    }

    public void bind(MediaItem mediaItem) {
      this.mediaItem = mediaItem;
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbnailView.getLayoutParams();
      params.width = itemSize;
      params.height = itemSize;
      mThumbnailView.setLayoutParams(params);

      ResizeOptions options = new ResizeOptions(itemSize, itemSize);
      displayImage(mediaItem, mThumbnailView, options);
    }

    private void displayImage(MediaItem mediaItem, SimpleDraweeView simpleDraweeView,
        ResizeOptions options) {
      ImageRequest request = ImageRequestBuilder.newBuilderWithSource(mediaItem.getUriOrigin())
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

    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_ADD = 1;

    private ArrayList<MediaItem> mData;//图片的容器
    private EasyViewHolder.OnItemClickListener itemClickListener;

    public SimpleAdapter() {
      mData = new ArrayList<>();
    }

    public void addAll(ArrayList<MediaItem> data) {
      mData.clear();
      mData.addAll(data);
      notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      MyViewHolder viewHolder;
      if (viewType == VIEW_TYPE_ADD) {
        viewHolder = AddViewHolder.create(getContext(), parent);
      } else {
        viewHolder = SimpleViewHolder.create(getContext(), parent);
      }
      viewHolder.setItemSize(mItemSize);
      bindListeners(viewHolder);
      return viewHolder;
    }

    @Override public void onBindViewHolder(ViewHolder holder, final int position) {
      MyViewHolder viewHolder = (MyViewHolder) holder;
      if (getItemViewType(position) == VIEW_TYPE_IMAGE) {
        final MediaItem mediaItem = mData.get(position);
        viewHolder.bind(mediaItem);
      } else {
        viewHolder.bind(null);
      }
    }

    @Override public int getItemViewType(int position) {
      if (isAddButton(position)) {
        return VIEW_TYPE_ADD;
      }
      return VIEW_TYPE_IMAGE;
    }

    public boolean isAddButton(int position) {
      return mData == null || mData.size() <= 0 || mData.size() == position;
    }

    @Override public int getItemCount() {
      return mData.size() + 1;
    }

    public void setOnClickListener(final EasyViewHolder.OnItemClickListener listener) {
      this.itemClickListener = new DebouncedOnClickListener() {
        @Override public boolean onDebouncedClick(View v, int position) {
          if (listener != null) {
            listener.onItemClick(position, v);
          }
          return true;
        }
      };
    }

    private void bindListeners(MyViewHolder cellViewHolder) {
      if (cellViewHolder != null) {
        cellViewHolder.setItemClickListener(itemClickListener);
      }
    }
  }

}
