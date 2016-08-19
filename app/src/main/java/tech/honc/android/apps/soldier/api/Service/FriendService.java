package tech.honc.android.apps.soldier.api.Service;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import tech.honc.android.apps.soldier.model.Status;
import tech.honc.android.apps.soldier.model.User;

/**
 * Created by MrJiang on 2016/5/31.
 */
public interface FriendService {
  /**
   * 添加好友
   */
  @FormUrlEncoded @POST("/buddy/add") Call<Status> addFriends(@Field("im_id") String imId);

  /**
   * 删除好友
   */
  @FormUrlEncoded @POST("/buddy/delete") Call<Status> delFriends(@Field("im_id") String imId);

  /**
   * 获取好友
   */
  @GET("/buddy/myFriend") Call<ArrayList<User>> myFriends();
}
