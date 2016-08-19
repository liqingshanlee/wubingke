package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 4/28/2016.
 */
public interface AttentionService {
  /**
   * 添加关注
   */
  @FormUrlEncoded @POST("/follow/follow") Call<Status> attentionUser(
      @Field("user_id") Integer userId);

  /**
   * 取消关注
   */
  @FormUrlEncoded @POST("/follow/cancel") Call<Status> cancelAttention(
      @Field("user_id") Integer userId);

  /**
   * 我关注的人
   */
  @GET("/follow/myFocus") Call<ArrayList<User>> getMyAttentions(@Query("max-id") Integer maxId);

  /**
   * 获取我的粉丝
   */
  @GET("/follow/myFollowers") Call<ArrayList<User>> getMyFans(@Query("max-id") Integer maxId);

  /**
   * 别人的粉丝
   */
  @GET("/follow/myFollowers") Call<ArrayList<User>> otherFans(@Query("id") Integer id);

  /**
   * 别人的关注
   */
  @GET("/follow/myFocus") Call<ArrayList<User>> othersAttentions(@Query("id") Integer maxId);
}
