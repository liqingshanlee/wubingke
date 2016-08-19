package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.model.ArticleComment;

/**
 * Created by Administrator on 2016/4/21.
 */
public interface ArticleService {

  /**
   * 获取文章
   */
  @GET("/articles") Call<ArrayList<Article>> getArtice(@Query("max-id") int maxId);

  /**
   * 文章详情
   */
  @GET("/articles/detail") Call<Article> getArticleDetail(@Query("id") Integer id);

  /**
   * 文章评论集合
   */
  @GET("/articlecomments") Call<ArrayList<ArticleComment>> getArticleComment(
      @Query("article-id") Integer articleId, @Query("max-id") Integer maxId);

  /**
   * 发表评论
   */
  @FormUrlEncoded @POST("/articlecomments") Call<ArticleComment> sendArticleComment(
      @Field("article-id") Integer articleId, @Field("content") String content);
}
