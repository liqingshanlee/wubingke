package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.Article;
import tech.honc.android.apps.soldier.model.Collection;

/**
 * Created by MrJiang on 2016/5/3.
 */
public interface CollectionService {
  /**
   * 收藏 或者 取消收藏
   */
  @FormUrlEncoded @POST("/articlecollections") Call<Collection> collectionCall(
      @Field("article-id") Integer articleId);

  /**
   * 用户收藏的文章
   */
  @GET("/articlecollections") Call<ArrayList<Article>> personalAllArticle(
      @Query("max-id") Integer maxId);
}
