package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import tech.honc.android.apps.soldier.model.Comments;
import tech.honc.android.apps.soldier.model.Feed;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.TaskComment;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by Administrator on 2016/4/19.
 */
public interface FeedService {

  /**
   * 获取同城动态
   */
  @GET("/feed/list") Call<ArrayList<Feed>> getCityFeeds(@Query("max-id") int maxId,
      @Query("city") String city);

  /**
   * 获取好友动态
   */
  @GET("/feed/friendFeeds") Call<ArrayList<Feed>> getFriendFeeds(@Query("max_id") int maxId);

  /**
   * 获取动态详情（同城/交友）
   */
  @GET("/feed/detail") Call<Feed> getDynamicDetails(@Query("feed_id") int feedId);

  /**
   * 获取附近任务动态
   * latitude 纬度
   * longitude 经度
   */
  @GET("/task/nearTasks") Call<ArrayList<Feed>> getTaskFeeds(@Query("latitude") double latitude,
      @Query("longitude") double longitude);

  /**
   * 发布普通动态
   */
  @FormUrlEncoded @POST("/feed/publish") Call<Status> releaseOrdinary(
      @Field("content") String content, @Field("pics[]") ArrayList<String> pics);

  /**
   * 获取点赞列表
   */

  @GET("/feed/likes") Call<ArrayList<User>> getLikes(@Query("feed_id") int feedId);

  /**
   * 获取评论列表
   */
  @GET("/feed/comment") Call<ArrayList<Comments>> getComments(@Query("feed_id") int feedId);

  /**
   * 发布任务
   * latitude 纬度
   * longitude 经度
   */

  @FormUrlEncoded @POST("/task/publish") Call<Status> releaseHelp(@Field("content") String content,
      @Field("latitude") double latitude, @Field("longitude") double longitude,
      @Field("pics[]") ArrayList<String> pics, @Field("address") String address,
      @Field("deadline") String deadline);

  /**
   * 获取用户所有动态
   */
  @GET("/feed/feeds") Call<ArrayList<Feed>> getUserFeeds(@Query("user_id") Integer userId,
      @Query("max_id") Integer maxId);

  /**
   * 点赞
   */
  @FormUrlEncoded @POST("/feed/like") Call<Status> toPraise(@Field("feed_id") int feedId);

  /**
   * 取消赞
   */
  @FormUrlEncoded @POST("/feed/cancel") Call<Status> cancelPraise(@Field("feed_id") int feedId);

  /**
   * 动态评论
   */
  @FormUrlEncoded @POST("/feed/comment") Call<Status> comment(@Field("feed_id") int feedId,
      @Field("content") String content, @Field("parent_id") int parentId);

  /**
   * 我的求助列表
   */
  @GET("/task/myTasks") Call<ArrayList<Feed>> myFeed(@Query("max_id") Integer maxId);

  /**
   * 获取任务详情
   */
  @GET("/task/detail") Call<Feed> getTaskDetails(@Query("task_id") int taskId);

  /**
   * 获取任务点赞列表
   */

  @GET("/task/interest") Call<ArrayList<User>> getTaskLikes(@Query("task_id") int taskId);

  /**
   * 获取任务帮助列表
   */
  @GET("/task/taskHelps") Call<ArrayList<TaskComment>> getTaskComments(
      @Query("task_id") int taskId);

  /**
   * 任务点赞
   */
  @FormUrlEncoded @POST("/task/like") Call<Status> toTaskPraise(@Field("task_id") int taskId);

  /**
   * 任务取消赞
   */
  @FormUrlEncoded @POST("/task/cancel") Call<Status> cancelTaskPraise(@Field("task_id") int taskId);

  /**
   * 任务评论
   */
  @FormUrlEncoded @POST("/task/comment") Call<Status> TaskComment(@Field("task_id") int feedId,
      @Field("content") String content, @Field("type") String type);

  /**
   * 删除某条动态
   */
  @FormUrlEncoded @POST("/feed/del_feed") Call<Status> deleteDynamic(
      @Field("feed_id") Integer feedId);

  /**
   * 设置帮助有用
   */
  @FormUrlEncoded @POST("/task/setUseful") Call<Status> setFeedUseful(@Field("id") Integer helpId);

  /**
   * 别人的求助列表
   */
  @GET("/task/myTasks") Call<ArrayList<Feed>> otherFeed(@Query("id") Integer id);
}
