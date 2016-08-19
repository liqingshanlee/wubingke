package tech.honc.android.apps.soldier.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import support.ui.adapters.EasyViewHolder;
import support.ui.adapters.debounced.DebouncedOnClickListener;
import support.ui.adapters.debounced.DebouncedOnLongClickListener;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.model.Image;

/**
 * 用户相册
 * 这个差点把我调疯了
 */
public class UserPhotoGalleryView extends RecyclerView {
  private static final int spanCount = 3;
  private SimpleAdapter mAdapter;
  private int mItemSize;
  private ArrayList<Image> mData;
  private Context mContext;

  public UserPhotoGalleryView(Context context) {
    this(context, null);
  }

  public UserPhotoGalleryView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public UserPhotoGalleryView(Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    mContext = context;
    initialize();
  }

  private void initialize() {
    setNestedScrollingEnabled(false);
    int screenWidth = StarterKitApp.appInfo().screenWidth;
    int spanSize = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_padding_8);
    int margin =
        StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_margin_20);
    int avatarSize =
        StarterKitApp.appResources().getDimensionPixelSize(R.dimen.photo_user_avatar_size);
    int innerMargin = StarterKitApp.appResources().getDimensionPixelSize(R.dimen.view_margin_8);
    mItemSize = (screenWidth - (spanCount - 1) * spanSize - 2 * margin) / spanCount;
    setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(getContext()).color(Color.TRANSPARENT)
            .size(spanSize / 2)
            .build());
    mAdapter = new SimpleAdapter();
    setAdapter(mAdapter);
  }

  public void setOnClickListener(final EasyViewHolder.OnItemClickListener listener) {
    mAdapter.setOnClickListener(listener);
  }

  public void setLongOnClickListener(final EasyViewHolder.OnItemLongClickListener listener) {
    mAdapter.setOnLongClickListener(listener);
  }

  public void setData(ArrayList<Image> data) {
    mData = data;
    if (data.size() >= 8) {
      ArrayList<Image> listData = new ArrayList<>();
      for (int i = 0; i < 8; i++) {
        listData.add(data.get(i));
      }
      mAdapter.setData(listData);
    }
    else {
      if (data.size() != 0) {
        mAdapter.setData(data);
      }
    }
  }

  /**
   * RecyclerView适配器
   */
  private class SimpleAdapter extends Adapter<CommonViewHolder> {
    private ArrayList<Image> mData;//图片的容器
    private static final int VIEW_TYPE_IMAGE = 0;
    private static final int VIEW_TYPE_ADD = 1;
    private static final int VIEW_TYPE_SHADOW = 3;
    private DebouncedOnClickListener itemClickListener;
    private DebouncedOnLongClickListener itemLongOnClickListener;

    public SimpleAdapter() {
      mData = new ArrayList<>();
    }

    public void setData(ArrayList<Image> data) {
      mData.clear();
      mData.addAll(data);
      notifyDataSetChanged();
    }

    @Override public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      CommonViewHolder commonViewHolder = null;
      EasyViewHolder.OnItemClickListener itemClickListener;
      switch (viewType) {
        case VIEW_TYPE_ADD:
          commonViewHolder = new AddViewHolder(getContext(), parent);
          break;
        case VIEW_TYPE_IMAGE:
          commonViewHolder = new SimpleViewHolder(getContext(), parent);
          break;
        case VIEW_TYPE_SHADOW:
          commonViewHolder = new shadowCellViewHolder(getContext(), parent);
      }
      assert commonViewHolder != null;
      commonViewHolder.setItemSize(mItemSize);
      // 取消数据绑定
      //bindListeners(commonViewHolder);
      //bindLongOnClickListener(commonViewHolder);
      return commonViewHolder;
    }

    @Override public void onBindViewHolder(CommonViewHolder holder, int position) {
      if (getItemViewType(position) == VIEW_TYPE_ADD) {
        holder.bind(null, position);
      } else {
        if (mData.size() != 0 && position < mData.size()) {
          Image image = mData.get(position);
          holder.bind(image, position);
        }
      }
    }

    @Override public int getItemViewType(int position) {
      if (isAddButton(position)) {
        return VIEW_TYPE_ADD;
      } else if (mData.size() != 0) {
        return VIEW_TYPE_IMAGE;
      }
      return VIEW_TYPE_SHADOW;
    }

    public boolean isAddButton(int position) {
      return mData.size() != 0 && mData.size() == position && mData.size() >= 8;
    }

    @Override public int getItemCount() {
      if (mData.size() >= 8) {
        return mData.size() + 1;
      }
      return mData.size();
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

    public void setOnLongClickListener(final EasyViewHolder.OnItemLongClickListener listener) {
      this.itemLongOnClickListener = new DebouncedOnLongClickListener() {
        @Override public boolean onDebouncedClick(View v, int position) {
          if (listener != null) {
            listener.onLongItemClicked(position, v);
          }
          return true;
        }
      };
    }

    private void bindListeners(CommonViewHolder cellViewHolder) {
      if (cellViewHolder != null) {
        cellViewHolder.setItemClickListener(itemClickListener);
      }
    }

    private void bindLongOnClickListener(CommonViewHolder commonViewHolder) {
      if (commonViewHolder != null) {
        commonViewHolder.setItemLongOnClickListener(itemLongOnClickListener);
      }
    }
  }

  /**
   * 抽象类，所有与网络图片的ViewHolder都继承自此基类
   */
  static abstract class CommonViewHolder extends ViewHolder
      implements OnClickListener, OnLongClickListener {

    protected int itemSize;
    private EasyViewHolder.OnItemClickListener itemClickListener;
    private EasyViewHolder.OnItemLongClickListener itemLongClickListener;

    public CommonViewHolder(View itemView) {
      super(itemView);
      bindListeners();
    }

    public void setItemClickListener(EasyViewHolder.OnItemClickListener itemClickListener) {
      this.itemClickListener = itemClickListener;
    }

    public void setItemLongOnClickListener(EasyViewHolder.OnItemLongClickListener listener) {
      this.itemLongClickListener = listener;
    }

    private void bindListeners() {
      itemView.setOnClickListener(this);
      itemView.setOnLongClickListener(this);
    }

    @Override public void onClick(View v) {
      if (itemClickListener == null) return;
      itemClickListener.onItemClick(getAdapterPosition(), v);
    }

    @Override public boolean onLongClick(View v) {
      if (itemLongClickListener == null) return false;
      itemLongClickListener.onLongItemClicked(getAdapterPosition(), v);
      return true;
    }

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    public void bind(Image image, int position) {
    }
  }

  class AddViewHolder extends CommonViewHolder {
    @Bind(R.id.text_frame_more) TextView addImageView;

    private AddViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_photo_more, parent, false));
      ButterKnife.bind(this, itemView);
    }

    AddViewHolder create(Context context, ViewGroup parent) {
      return new AddViewHolder(context, parent);
    }

    public void bind(Image image, final int position) {
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) addImageView.getLayoutParams();
      params.width = itemSize;
      params.height = itemSize;
      addImageView.setLayoutParams(params);
      addImageView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          Navigator.startPhotoBrowser((Activity) mContext, mData, position);
        }
      });
    }
  }

  /**
   * 网络图片装载ViewHolder
   */
  class SimpleViewHolder extends CommonViewHolder {
    public Image image;
    protected int itemSize;
    @Bind(R.id.image_feed_thumbnail) SimpleDraweeView mThumbnailView;

    private SimpleViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_photo_image, parent, false));
      ButterKnife.bind(this, itemView);
    }

    SimpleViewHolder create(Context context, ViewGroup parent) {
      return new SimpleViewHolder(context, parent);
    }

    public void setItemSize(int itemSize) {
      this.itemSize = itemSize;
    }

    public void bind(Image image, final int position) {
      this.image = image;
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThumbnailView.getLayoutParams();
      params.width = itemSize;
      params.height = itemSize;
      mThumbnailView.setLayoutParams(params);
      ResizeOptions options = new ResizeOptions(itemSize, itemSize);
      displayImage(image, mThumbnailView, options);
      mThumbnailView.setOnClickListener(new OnClickListener() {
        @Override public void onClick(View v) {
          Navigator.startPhotoBrowser((Activity) mContext, mData, position);
        }
      });
    }

    private void displayImage(Image image, SimpleDraweeView simpleDraweeView,
        ResizeOptions options) {
      ImageRequest request =
          ImageRequestBuilder.newBuilderWithSource(image.uri()).setResizeOptions(options).build();
      PipelineDraweeController controller =
          (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
              .setOldController(simpleDraweeView.getController())
              .setImageRequest(request)
              .build();
      simpleDraweeView.setController(controller);
    }
  }

  class shadowCellViewHolder extends CommonViewHolder {

    public shadowCellViewHolder(Context context, ViewGroup parent) {
      super(LayoutInflater.from(context).inflate(R.layout.list_item_shadow, parent, false));
    }

    @Override public void setItemSize(int itemSize) {
      super.setItemSize(itemSize);
    }
  }

  @Override public void setHasFixedSize(boolean hasFixedSize) {
    super.setHasFixedSize(true);
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    return super.dispatchTouchEvent(ev);
  }
}
