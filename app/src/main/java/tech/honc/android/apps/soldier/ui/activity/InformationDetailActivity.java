package tech.honc.android.apps.soldier.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import support.ui.adapters.EasyRecyclerAdapter;
import support.ui.adapters.EasyViewHolder;
import tech.honc.android.apps.soldier.Navigator;
import tech.honc.android.apps.soldier.R;
import tech.honc.android.apps.soldier.api.ApiService;
import tech.honc.android.apps.soldier.api.Service.ArticleService;
import tech.honc.android.apps.soldier.api.Service.CollectionService;
import tech.honc.android.apps.soldier.api.Service.LikeService;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.model.ArticleComment;
import tech.honc.android.apps.soldier.model.Collection;
import tech.honc.android.apps.soldier.model.Likes;
import tech.honc.android.apps.soldier.ui.viewholder.ArticleCommentViewHolder;
import tech.honc.android.apps.soldier.ui.widget.MyScrollview;
import tech.honc.android.apps.soldier.utils.toolsutils.DateFormat;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginDialog;
import tech.honc.android.apps.soldier.utils.toolsutils.LoginNavigationsUtil;
import tech.honc.android.apps.soldier.utils.toolsutils.SnackBarUtil;
import zhou.widget.RichText;

/**
 * Created by MrJiang
 * on 2016/4/25.
 * 资讯详情
 */
public class InformationDetailActivity extends BaseActivity
    implements EasyViewHolder.OnItemClickListener
{

  @Bind(R.id.comments_content) EditText mCommentsContent;
  @Bind(R.id.info_detail_title) TextView mInfoDetailTitle;
  @Bind(R.id.info_detail_type) TextView mInfoDetailType;
  @Bind(R.id.info_detail_time) TextView mInfoDetailTime;
  @Bind(R.id.info_detail_like) TextView mInfoDetailLike;
  @Bind(R.id.info_detail_comments) TextView mInfoDetailComments;
  @Bind(R.id.info_text_collection) TextView mInfoTextCollection;
  @Bind(R.id.info_detail_image) SimpleDraweeView mInfoDetailImage;
  @Bind(R.id.info_detail_text) RichText mInfoDetailText;
  @Bind(R.id.info_detail_comment_in) TextView mInfoDetailCommentIn;
  @Bind(R.id.info_detail_line) View mInfoDetailLine;
  @Bind(R.id.info_detail_recycler) RecyclerView mInfoDetailRecycler;
  @Bind(R.id.info_detail_scroll) MyScrollview mMyScrollview;
  @Bind(R.id.info_detail_like_image) ImageView mInfoDetailLikeImage;
  @Bind(R.id.info_detail_comments_image) ImageView mInfoDetailCommentsImage;
  @Bind(R.id.info_text_collection_image) ImageView mInfoTextCollectionImage;

  private ArticleService mArticleService;
  private LikeService mLikeService;
  private CollectionService mCollectionService;
  private EasyRecyclerAdapter mCommentAdapter;
  private ArrayList<ArticleComment> mArticleComments;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_information_detail);
    ButterKnife.bind(this);
    init();
    getArticleDetail();
    getCommentDetail();
    setUpAdapter();
    setUpRecyclerView();
  }

  private void init() {
    mArticleService = ApiService.createArticleService();
    mLikeService = ApiService.createLikeService();
    mCollectionService = ApiService.createCollectionService();
    mArticleComments = new ArrayList<>();
  }

  public int getIntentArticleId() {
    return getIntent().getIntExtra("id", 0);
  }

  private void setUpRecyclerView() {
    mInfoDetailRecycler.setHasFixedSize(true);
    mInfoDetailRecycler.setLayoutManager(new LinearLayoutManager(this));
    mInfoDetailRecycler.addItemDecoration(
        new HorizontalDividerItemDecoration.Builder(this).size(1).showLastDivider().build());
    mInfoDetailRecycler.setAdapter(mCommentAdapter);
  }

  private void setUpAdapter() {
    mCommentAdapter = new EasyRecyclerAdapter(this);
    mCommentAdapter.bind(ArticleComment.class, ArticleCommentViewHolder.class);
    mCommentAdapter.setOnClickListener(this);
    mCommentAdapter.appendAll(mArticleComments);
  }

  private void startUpdate() {
    mCommentAdapter.clear();
    mCommentAdapter.appendAll(mArticleComments);
    mCommentAdapter.notifyDataSetChanged();
  }

  @SuppressLint("SetTextI18n") private void setUpView(Article article) {
    mInfoDetailTitle.setText(article.title != null ? article.title : "");
    mInfoDetailType.setText("类别: " + (article.categoryName != null ? article.categoryName : ""));
    mInfoDetailTime.setText(
        DateFormat.getRelativeTime(article.publishedAt) + " " + DateFormat.getCommonTime(
            article.publishedAt));
    mInfoDetailLike.setText("(" + article.likeTimes + ")");
    mInfoDetailComments.setText("(" + article.commentTimes + ")");
    mInfoTextCollection.setText("(" + article.collectionTimes + ")");
    mInfoDetailImage.setImageURI(article.uri());
    mInfoDetailText.setRichText(article.content);
    if (article.isLiked) {
      mInfoDetailLikeImage.setImageResource(R.mipmap.ic_like_red);
    } else {
      mInfoDetailLikeImage.setImageResource(R.mipmap.ic_praise);
    }
    if (article.isCollected) {
      mInfoTextCollectionImage.setImageResource(R.mipmap.ic_collect_red);
    } else {
      mInfoTextCollectionImage.setImageResource(R.mipmap.ic_collections);
    }
    if (article.commentTimes == 0) {
      mInfoDetailCommentIn.setVisibility(View.GONE);
      mInfoDetailLine.setVisibility(View.GONE);
    }
  }

  private void getCommentDetail() {
    Call<ArrayList<ArticleComment>> callComment =
        mArticleService.getArticleComment(getIntentArticleId(), null);
    callComment.enqueue(new Callback<ArrayList<ArticleComment>>()
    {
      @Override public void onResponse(Call<ArrayList<ArticleComment>> call,
          Response<ArrayList<ArticleComment>> response) {
        if (response.isSuccessful()) {
          mArticleComments = response.body();
          startUpdate();
          if (response.body() != null && response.body().size() > 0) {
            mInfoDetailCommentIn.setVisibility(View.VISIBLE);
            mInfoDetailLine.setVisibility(View.VISIBLE);
          }
        } else {
          showErrorInfo();
        }
      }

      @Override public void onFailure(Call<ArrayList<ArticleComment>> call, Throwable t) {
        showErrorInfo();
      }
    });
  }

  private void getArticleDetail() {
    Call<Article> call = mArticleService.getArticleDetail(getIntentArticleId());
    call.enqueue(new Callback<Article>()
    {
      @Override public void onResponse(Call<Article> call, Response<Article> response) {
        if (response.isSuccessful()) {
          setUpView(response.body());
        } else {
          showErrorInfo();
        }
      }

      @Override public void onFailure(Call<Article> call, Throwable t) {
        showErrorInfo();
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();
    startUpdate();
  }

  public void showErrorInfo() {
    SnackBarUtil.showText(this, "发生了错误");
  }

  @Override public void onItemClick(int position, View view) {
    ArticleComment comment = (ArticleComment) mCommentAdapter.get(position);
    if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
      LoginDialog dialog = LoginDialog.getInstance();
      dialog.init(this);
      dialog.showDialog();
      return;
    }
    if (comment != null && comment.account != null && comment.account.id != null) {
      Navigator.startUserDetailActivty(InformationDetailActivity.this, comment.account.id);
    }
  }

  @OnClick({ R.id.send_mge, R.id.container_like, R.id.info_detail_collections })
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.send_mge:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        String content = mCommentsContent.getText().toString();
        if (!content.equals("")) {
          sendComment(content);
        }
        break;
      case R.id.container_like:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        sendLikes();
        break;
      case R.id.info_detail_collections:
        if (LoginNavigationsUtil.navigationActivity() == LoginNavigationsUtil.TAG_NO_REGISTER) {
          LoginDialog dialog = LoginDialog.getInstance();
          dialog.init(this);
          dialog.showDialog();
          return;
        }
        sendCollections();
        break;
    }
  }

  /**
   * 发评论
   */
  public void sendComment(String content) {
    hideSoftInputMethod();
    Call<ArticleComment> call = mArticleService.sendArticleComment(getIntentArticleId(), content);
    call.enqueue(new Callback<ArticleComment>()
    {
      @Override
      public void onResponse(Call<ArticleComment> call, Response<ArticleComment> response) {
        if (response.isSuccessful()) {
          mArticleComments.add(response.body());
          startUpdate();
          getArticleDetail();
        } else {
          showErrorInfo();
        }
        setEditTextNull();
      }

      @Override public void onFailure(Call<ArticleComment> call, Throwable t) {
        setEditTextNull();
        showErrorInfo();
      }
    });
  }

  public void sendLikes() {
    Call<Likes> call = mLikeService.likesCall(getIntentArticleId());
    call.enqueue(new Callback<Likes>()
    {
      @Override public void onResponse(Call<Likes> call, Response<Likes> response) {
        if (response.isSuccessful()) {
          getArticleDetail();
        } else {
          showErrorInfo();
        }
      }

      @Override public void onFailure(Call<Likes> call, Throwable t) {
        showHud();
      }
    });
  }

  public void sendCollections() {
    Call<Collection> call = mCollectionService.collectionCall(getIntentArticleId());
    call.enqueue(new Callback<Collection>()
    {
      @Override public void onResponse(Call<Collection> call, Response<Collection> response) {
        if (response.isSuccessful()) {
          getArticleDetail();
        } else {
          showErrorInfo();
        }
      }

      @Override public void onFailure(Call<Collection> call, Throwable t) {
        showErrorInfo();
      }
    });
  }

  public void setEditTextNull() {
    mCommentsContent.setText("");
  }
}
